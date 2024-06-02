package back.services;

import back.pojo.User;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class SQLiteConnector {
  private static final String JDBC_URL = EnvironmentVariables.JDBC_URL;

  public void addUser(User user) {
    try (Connection connection = DriverManager.getConnection(JDBC_URL);
        PreparedStatement preparedStatement =
            connection.prepareStatement("INSERT INTO users VALUES (?, ?)")) {
      String hashedPassword = BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt());
      preparedStatement.setString(1, user.getUsername());
      preparedStatement.setString(2, hashedPassword);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public User getUser(String username, String plainPassword) {
    try (Connection connection = DriverManager.getConnection(JDBC_URL);
        PreparedStatement preparedStatement =
            connection.prepareStatement("SELECT * FROM user WHERE name = ?")) {
      preparedStatement.setString(1, username);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        String storedHash =
            resultSet.getString(
                "mdp"); // Replace "mdp" with the actual column name for the hashed password
        if (BCrypt.checkpw(plainPassword, storedHash)) {
          return new User(resultSet.getString("name"), storedHash, resultSet.getInt("id"));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
