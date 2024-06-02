package back.services;

import back.pojo.Recipe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListServiceTest {

  @BeforeAll
  public static void setUp() {
    EnvironmentVariables.JDBC_URL = "jdbc:sqlite:test_db.db";
  }
  @Test
  void saveRecipeToShoppingList() {
    Recipe recipe = new Recipe(1, "test");
    ShoppingListService shoppingListService = new ShoppingListService();
    shoppingListService.saveRecipeToShoppingList(recipe, 1);
    try (var connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      var preparedStatement =
          connection.prepareStatement("SELECT * FROM shopping_list WHERE id_user = ? AND recipe_name = ?");
      preparedStatement.setInt(1, 1);
      preparedStatement.setString(2, "test");
      var resultSet = preparedStatement.executeQuery();
      assertTrue(resultSet.next());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void deleteRecipe() {
    ShoppingListService shoppingListService = new ShoppingListService();
    shoppingListService.deleteRecipe(1, "test");
    try (var connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      var preparedStatement =
          connection.prepareStatement("SELECT * FROM shopping_list WHERE id_user = ? AND recipe_name = ?");
      preparedStatement.setInt(1, 1);
      preparedStatement.setString(2, "test");
      var resultSet = preparedStatement.executeQuery();
      assertFalse(resultSet.next());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void getRecipes() {
    ShoppingListService shoppingListService = new ShoppingListService();
    assertEquals(1, shoppingListService.getRecipes(1).size());
  }
}