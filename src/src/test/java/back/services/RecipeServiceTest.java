package back.services;

import back.pojo.Recipe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RecipeServiceTest {

  @BeforeAll
  public static void setUp() {
    EnvironmentVariables.JDBC_URL = "jdbc:sqlite:test_db.db";
  }

  @BeforeEach
  public void setUpEach() {
    deleteRecipeFromFavorite();
    insertRecipeInFavorite();
  }

  @Test
  public void listFavoriteRecipes() {
    RecipeService recipeService = new RecipeService();
    assertEquals(1, recipeService.listFavorites(1).size());
  }

  @Test
  public void addRecipeToFavorites() {
    RecipeService recipeService = new RecipeService();
    Recipe recipe = new Recipe(2, "test");
    recipeService.addRecipeToFavorite(1, recipe);
    assertEquals(2, recipeService.listFavorites(1).size());
  }
  private void deleteRecipeFromFavorite() {
    try (var connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      var preparedStatement = connection.prepareStatement("DELETE FROM favorite_recipes;");
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void insertRecipeInFavorite() {
    try (var connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      var preparedStatement =
          connection.prepareStatement("INSERT INTO favorite_recipes (id_user, id_recipe, name) VALUES (?, ?, ?);");
      preparedStatement.setInt(1, 1);
      preparedStatement.setInt(2, 1);
      preparedStatement.setString(3, "test");
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}