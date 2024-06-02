package back.services;

import back.pojo.Ingredient;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientService implements Services<Ingredient> {
  private final String jdbcUrl = EnvironmentVariables.JDBC_URL;

  @Override
  public void create(Ingredient o) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      insertIngredient(connection, o);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Ingredient read(int id) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      Ingredient resultIngredient = searchIngredientById(connection, id);
      connection.close();
      return resultIngredient;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void update(Ingredient o) {
    try {
      int id = o.getId();
      Connection connection = DriverManager.getConnection(jdbcUrl);
      updateIngredientById(connection, id, o);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(Ingredient o) {
    int id = o.getId();
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      deleteIngredientById(connection, id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Ingredient> List(int userId) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      return listIngredientByUserId(connection, userId);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Ingredient> listIngredientByUserId(Connection connection, int userId)
      throws SQLException {
    List<Ingredient> ingredients = new ArrayList<>();
    String request = "SELECT * FROM ingredients WHERE user_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    preparedStatement.setInt(1, userId);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      Ingredient ingredient =
          new Ingredient(
              resultSet.getInt("id"),
              resultSet.getInt("user_id"),
              resultSet.getString("name"),
              resultSet.getFloat("quantity"),
              resultSet.getString("unit"),
              resultSet.getDate("expiration_date"));
      ingredients.add(ingredient);
    }
    return ingredients;
  }

  private static void insertIngredient(Connection connection, Ingredient ing) throws SQLException {

    String insertUserSql =
        "INSERT INTO ingredients (name, user_id, quantity, unit, expiration_date) "
            + "VALUES (?, ?, ?, ?, ?)";

    PreparedStatement preparedStatement = connection.prepareStatement(insertUserSql);
    preparedStatement.setString(1, ing.getName());
    preparedStatement.setInt(2, ing.getUserId());
    preparedStatement.setFloat(3, ing.getQuantity());
    preparedStatement.setString(4, ing.getUnit());
    preparedStatement.setDate(5, ing.getExpirationDate());

    int affectedRow = preparedStatement.executeUpdate();

    if (affectedRow > 0) {
      ResultSet results = preparedStatement.getGeneratedKeys();
      if (results.next()) {
        ing.setId(results.getInt(1));
      } else {
        System.err.println("Failed to retrieve generated ID, insertion must have failed");
      }
    }

    System.out.println("Ingredient data inserted successfully.");
  }

  private Ingredient searchIngredientById(Connection connection, int id) throws SQLException {
    String selectSql = "SELECT * FROM ingredients WHERE id = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
    preparedStatement.setInt(1, id);

    try (ResultSet resultSet = preparedStatement.executeQuery()) {
      if (resultSet.next()) {
        return new Ingredient(
            resultSet.getInt("id"),
            resultSet.getInt("user_id"),
            resultSet.getString("name"),
            resultSet.getFloat("quantity"),
            resultSet.getString("unit"),
            resultSet.getDate("expiration_date"));
      } else {
        return null;
      }
    }
  }

  private void deleteIngredientById(Connection connection, int id) throws SQLException {
    String deleteSql = "DELETE FROM ingredients WHERE id = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
    preparedStatement.setInt(1, id);
    preparedStatement.executeUpdate();
    System.out.println("Ingredient with ID " + id + " deleted successfully.");
  }

  private void updateIngredientById(Connection connection, int id, Ingredient ing)
      throws SQLException {
    String deleteSql =
        "UPDATE ingredients set id = ?,  user_id = ?, name = ?, quantity = ?, unit = ? ,expiration_date = ? WHERE id = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
    preparedStatement.setInt(1, id);
    preparedStatement.setInt(2, ing.getUserId());
    preparedStatement.setString(3, ing.getName());
    preparedStatement.setFloat(4, ing.getQuantity());
    preparedStatement.setString(5, ing.getUnit());
    preparedStatement.setDate(6, ing.getExpirationDate());
    preparedStatement.setInt(7, id);
    preparedStatement.executeUpdate();
    System.out.println("Ingredient with ID " + id + " updated successfully.");
  }

  public List<String> isIngredientInList(String startingString) {

    String csvFile = "ingredients.csv";
    String line = "";
    String csvSplitBy = ";";

    List<String> filteredLines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

      if (startingString.equals("")) {
        return filteredLines;
      }

      while ((line = br.readLine()) != null) {
        String data = line.split(csvSplitBy)[0];
        if (line.startsWith(startingString) && filteredLines.size() < 7) {
          filteredLines.add(data);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return filteredLines;
  }
}
