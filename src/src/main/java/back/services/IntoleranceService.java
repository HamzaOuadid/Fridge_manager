package back.services;

import back.pojo.Intolerance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IntoleranceService {

  private final String jdbcUrl = EnvironmentVariables.JDBC_URL;

  public List<Intolerance> listIntolerances() {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      List<Intolerance> intolerances = findIntolerances(connection);
      connection.close();
      return intolerances;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Intolerance> findIntolerances(Connection connection) throws SQLException {
    List<Intolerance> intolerances = new ArrayList<>();
    String request =
        """
                          SELECT i.name, i.id  FROM intolerances i\s
                    """;
    PreparedStatement preparedStatement = connection.prepareStatement(request);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      Intolerance intolerance =
          new Intolerance(resultSet.getInt("id"), resultSet.getString("name"));
      intolerances.add(intolerance);
    }
    return intolerances;
  }
}
