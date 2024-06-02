package back.services;

import static org.junit.jupiter.api.Assertions.*;

import back.pojo.Ingredient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngredientServiceTest {

  @BeforeAll
  public static void setUp() {
    EnvironmentVariables.JDBC_URL = "jdbc:sqlite:test_db.db";
  }

  @BeforeEach
  public void setUpEach() {
    deleteIngredient();
  }

  @Test
  public void create() throws SQLException {
    IngredientService ingredientService = new IngredientService();
    Ingredient resultIngredient = null;
    Ingredient ingredient = new Ingredient(1, 1, "test", 1, "test", null);
    ingredientService.create(ingredient);
    try (Connection connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM ingredients WHERE name = ?");
      preparedStatement.setString(1, "test");
      var resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        resultIngredient =
            new Ingredient(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getFloat("quantity"),
                resultSet.getString("unit"),
                resultSet.getDate("expiration_date"));
      }
      assertEquals(resultIngredient, ingredient);
    }
  }

  @Test
  public void update() throws SQLException {
    IngredientService ingredientService = new IngredientService();
    Ingredient resultIngredient = null;
    Ingredient ingredient = new Ingredient(1, 1, "test", 1, "test", null);
    ingredientService.create(ingredient);
    ingredient.setQuantity(2);
    ingredientService.update(ingredient);
    try (Connection connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM ingredients WHERE name = ?");
      preparedStatement.setString(1, "test");
      var resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        resultIngredient =
            new Ingredient(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getFloat("quantity"),
                resultSet.getString("unit"),
                resultSet.getDate("expiration_date"));
      }
      assert resultIngredient != null;
      assertEquals(resultIngredient.getQuantity(), ingredient.getQuantity());
    }
  }

  @Test
  public void delete() throws SQLException {
    IngredientService ingredientService = new IngredientService();
    Ingredient ingredient = new Ingredient(1, 1, "test", 1, "test", null);
    ingredientService.create(ingredient);
    ingredientService.delete(ingredient);
    try (Connection connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM ingredients WHERE name = ?");
      preparedStatement.setString(1, "test");
      var resultSet = preparedStatement.executeQuery();
      System.out.println(resultSet);
      assertFalse(resultSet.next());
    }
  }

  @Test
  public void list() {
    IngredientService ingredientService = new IngredientService();
    Ingredient ingredient = new Ingredient(1, 1, "test", 1, "test", null);
    ingredientService.create(ingredient);
    var result = ingredientService.List(1);
    assertEquals(result.get(0), ingredient);
  }

  private void deleteIngredient() {
    try (Connection connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ingredients");
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
