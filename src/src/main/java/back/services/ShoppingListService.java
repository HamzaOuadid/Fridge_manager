package back.services;

import back.pojo.Ingredient;
import back.pojo.Recipe;
import back.spoonacular.ApiRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.*;

public class ShoppingListService {

  private final String jdbcUrl = EnvironmentVariables.JDBC_URL;

  public void saveRecipeToShoppingList(Recipe recipe, int userId) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      createRecipe(connection, recipe, userId);
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteRecipe(int userId, String recipeName) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String request = "DELETE FROM shopping_list WHERE id_user = ? AND recipe_name = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(request);
      preparedStatement.setInt(1, userId);
      preparedStatement.setString(2, recipeName);
      preparedStatement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void createRecipe(Connection connection, Recipe recipe, int userId) throws SQLException {
    String request = "INSERT INTO shopping_list VALUES (?,?,?)";
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    preparedStatement.setInt(1, recipe.getId());
    preparedStatement.setInt(2, userId);
    preparedStatement.setString(3, recipe.getTitle());
    preparedStatement.executeUpdate();
  }

  public List<String> getRecipes(int userId) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      List<String> recipes = getRecipeName(connection, userId);
      connection.close();
      return recipes;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Ingredient> getIngredientsOfRecipes(int userId) {
    try {
      List<Ingredient> ingredients = new ArrayList<>();
      Connection connection = DriverManager.getConnection(jdbcUrl);
      List<Integer> recipesId = getRecipeId(connection, userId);
      for (Integer id : recipesId) {
        List<Ingredient> ingredientsFromRecipe = getRecipeInformation(id);
        ingredients.addAll(ingredientsFromRecipe);
      }
      connection.close();
      return ingredients;
    } catch (SQLException | JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getRecipeName(Connection connection, int userId) throws SQLException {
    List<String> nameList = new ArrayList<>();
    String request =
        "SELECT shopping_list.recipe_name FROM shopping_list WHERE shopping_list.id_user = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    preparedStatement.setInt(1, userId);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      String name = resultSet.getString("recipe_name");
      nameList.add(name);
    }
    return nameList;
  }

  private List<Integer> getRecipeId(Connection connection, int userId) throws SQLException {
    List<Integer> idList = new ArrayList<>();
    String request = "SELECT shopping_list.id_recipe FROM shopping_list WHERE id_user = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    preparedStatement.setInt(1, userId);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      int id = resultSet.getInt("id_recipe");
      idList.add(id);
    }
    return idList;
  }

  private List<Ingredient> getRecipeInformation(int recipeID) throws JsonProcessingException {
    ApiRequest apiRequest = new ApiRequest();
    String request =
        "https://api.spoonacular.com/recipes/"
            + recipeID
            + "/information?apiKey=cc8520d3dde345f4a72d504fcc88d08d";
    String response = apiRequest.request(request);
    try {
      List<Ingredient> ingredients = new ArrayList<>();
      JsonNode node = new ObjectMapper().readTree(response);
      if (node.has("extendedIngredients")) {
        Iterator<JsonNode> elements = node.get("extendedIngredients").elements();
        while (elements.hasNext()) {
          JsonNode ingredient = elements.next();
          String name = ingredient.get("name").asText();
          String unit = ingredient.get("unit").asText();
          double amount = ingredient.get("amount").asDouble();
          ingredients.add(new Ingredient(name, unit, amount));
        }
      }
      return ingredients;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  // Method to find missing ingredients
  public List<Ingredient> getMissingIngredients(
      List<Ingredient> fridgeIngredients, List<Ingredient> recipeIngredients) {
    List<Ingredient> missingIngredients = new ArrayList<>();
    for (Ingredient ingredient : recipeIngredients) {
      if (!fridgeIngredients.contains(ingredient)) {
        missingIngredients.add(ingredient);
      }
    }
    return mergeSimilarIngredients(missingIngredients);
  }

  // Compliquer a gérer avec les unités qui sont trop différents
  private List<Ingredient> mergeSimilarIngredients(List<Ingredient> ingredients) {
    List<Ingredient> mergedIngredients = new ArrayList<>();
    for (Ingredient ingredient : ingredients) {
      if (mergedIngredients.isEmpty()) {
        mergedIngredients.add(ingredient);
      } else {
        for (Ingredient mergedIngredient : mergedIngredients) {
          if (mergedIngredient.getName().equals(ingredient.getName())) {
            if (ingredient.getUnit().equals(mergedIngredient.getUnit())) {
              int index =
                  mergedIngredients.stream()
                      .map(Ingredient::getName)
                      .toList()
                      .indexOf(ingredient.getName());
              mergedIngredients.remove(index);
              mergedIngredient.setQuantity(
                  mergedIngredient.getQuantity() + ingredient.getQuantity());
              break;
            }
            mergedIngredients.add(mergedIngredient);
          } else {
            mergedIngredients.add(ingredient);
          }
          break;
        }
      }
    }
    return mergedIngredients.stream()
        .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
        .toList();
  }
}
