package back.services;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUpdater {
  private static final String JDBC_URL = EnvironmentVariables.JDBC_URL;

  public static void updatePasswords() {
    try (Connection connection = DriverManager.getConnection(JDBC_URL);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id, name, mdp FROM user")) {

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String plainPassword = resultSet.getString("mdp");
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        try (PreparedStatement updateStatement =
            connection.prepareStatement("UPDATE user SET mdp = ? WHERE id = ?")) {
          updateStatement.setString(1, hashedPassword);
          updateStatement.setInt(2, id);
          updateStatement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    updatePasswords();
    System.out.println("Password update complete.");
  }
}
