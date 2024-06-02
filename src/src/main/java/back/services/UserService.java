package back.services;

import back.pojo.Intolerance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

  private static final String JDBC_URL = EnvironmentVariables.JDBC_URL;

  public void setIntolerance(int userId, Intolerance intolerance) {
    try {
      Connection connection = DriverManager.getConnection(JDBC_URL);
      PreparedStatement preparedStatement =
          connection.prepareStatement(
              """
                              INSERT INTO intolerances_user (id_user, id_intolerances)
                              VALUES (?, ?)""");
      preparedStatement.setInt(1, userId);
      preparedStatement.setInt(2, intolerance.getId());
      preparedStatement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Intolerance> getIntolerances(int userId) {
    try {
      Connection connection = DriverManager.getConnection(JDBC_URL);
      List<Intolerance> intolerances = findIntolerancesByUser(connection, userId);
      connection.close();
      return intolerances;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteIntolerance(int userId, Intolerance intolerance) {
    try {
      Connection connection = DriverManager.getConnection(JDBC_URL);
      PreparedStatement preparedStatement =
          connection.prepareStatement(
              """
                              DELETE FROM intolerances_user
                              WHERE id_user = ? AND id_intolerances = ?""");
      preparedStatement.setInt(1, userId);
      preparedStatement.setInt(2, intolerance.getId());
      preparedStatement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Intolerance> findIntolerancesByUser(Connection connection, int userId)
      throws SQLException {
    List<Intolerance> intolerances = new ArrayList<>();
    String request =
        """
                    SELECT i.name, i.id  FROM intolerances i\s
                    JOIN intolerances_user iu\s
                    ON i.id  = iu.id_intolerances\s
                    WHERE iu.id_user = ?""";
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    preparedStatement.setInt(1, userId);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      Intolerance intolerance =
          new Intolerance(resultSet.getInt("id"), resultSet.getString("name"));
      intolerances.add(intolerance);
    }
    return intolerances;
  }
}
