package back.services;

import back.pojo.Intolerance;
import back.pojo.NutritionDetails;
import back.pojo.Recipe;
import back.spoonacular.ApiRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {
  private ApiRequest apiRequest = new ApiRequest();

  private final String jdbcUrl = EnvironmentVariables.JDBC_URL;

  public List<Recipe> getRecipesByIngredients(List<String> ingredients) {
    try {
      String url = apiRequest.buildRecipesByIngredientsUrl(ingredients);
      String jsonResponse = apiRequest.request(url);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
    } catch (Exception e) {
      throw new RuntimeException("Error fetching recipes", e);
    }
  }

  public NutritionDetails getNutritionDetailsForRecipe(int recipeId) {
    try {
      String url = apiRequest.buildNutritionDetailsUrl(recipeId);
      System.out.println("Requesting URL: " + url);
      String jsonResponse = apiRequest.request(url);
      if (jsonResponse == null) {
        throw new RuntimeException("Received null response from API");
      }
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(jsonResponse, NutritionDetails.class);
    } catch (Exception e) {
      throw new RuntimeException("Error fetching nutrition details", e);
    }
  }

  public List<Recipe> getRecipeWithIntolerance(
      List<String> ingredients, List<Intolerance> intolerances) {
    try {
      List<Recipe> recipes = new ArrayList<>();
      String url = apiRequest.complexSearch(ingredients, intolerances);
      String jsonResponse = apiRequest.request(url);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(jsonResponse);
      JsonNode results = jsonNode.get("results");
      for (JsonNode result : results) {
        JsonNode missedIngredientsNode = result.get("missedIngredients");
        JsonNode usedIngredientsNode = result.get("usedIngredients");
        JsonNode unusedIngredientsNode = result.get("unusedIngredients");
        List<Recipe.Ingredient> missedIngredients = getIngredients(missedIngredientsNode);
        List<Recipe.Ingredient> usedIngredients = getIngredients(usedIngredientsNode);
        List<Recipe.Ingredient> unusedIngredients = getIngredients(unusedIngredientsNode);
        Recipe recipe =
            new Recipe(
                result.get("id").asInt(),
                result.get("title").asText(),
                result.get("image").asText(),
                result.get("imageType").asText(),
                result.get("usedIngredientCount").asInt(),
                result.get("missedIngredientCount").asInt(),
                missedIngredients,
                usedIngredients,
                unusedIngredients,
                result.get("aggregateLikes").asInt());
        recipes.add(recipe);
      }
      return recipes;
    } catch (Exception e) {
      throw new RuntimeException("Error fetching recipes", e);
    }
  }

  private List<Recipe.Ingredient> getIngredients(JsonNode ingredientsNode) {
    List<Recipe.Ingredient> ingredients = new ArrayList<>();
    for (JsonNode ingredientNode : ingredientsNode) {
      JsonNode amount = ingredientNode.get("amount");
      JsonNode unit = ingredientNode.get("unit");
      JsonNode name = ingredientNode.get("name");
      JsonNode image = ingredientNode.get("image");
      ingredients.add(
          new Recipe.Ingredient(amount.asDouble(), unit.asText(), name.asText(), image.asText()));
    }
    return ingredients;
  }

  public void addRecipeToFavorite(int userId, Recipe recipe) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String request =
          """
                      INSERT INTO favorite_recipes (id_recipe, id_user, name)
                      VALUES (?, ?, ?)""";
      var preparedStatement = connection.prepareStatement(request);
      preparedStatement.setInt(1, recipe.getId());
      preparedStatement.setInt(2, userId);
      preparedStatement.setString(3, recipe.getTitle());
      preparedStatement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Recipe> listFavorites(int userId) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      List<Recipe> recipes = findFavoritesByUser(connection, userId);
      connection.close();
      return recipes;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Recipe> findFavoritesByUser(Connection connection, int userId) throws SQLException {
    List<Recipe> recipes = new ArrayList<>();
    String request =
        """
                    SELECT fr.id_recipe, fr.name FROM favorite_recipes fr\s
                    WHERE fr.id_user = ?""";
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    preparedStatement.setInt(1, userId);
    var resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      Recipe recipe = new Recipe(resultSet.getInt("id_recipe"), resultSet.getString("name"));
      recipes.add(recipe);
    }
    return recipes;
  }

  public void deleteRecipeFromFavorites(int userId, Recipe recipe) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String request =
          """
                      DELETE FROM favorite_recipes
                      WHERE id_recipe = ? AND id_user = ?""";
      var preparedStatement = connection.prepareStatement(request);
      preparedStatement.setInt(1, recipe.getId());
      preparedStatement.setInt(2, userId);
      preparedStatement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
