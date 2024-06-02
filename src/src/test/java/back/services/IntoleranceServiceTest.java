package back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntoleranceServiceTest {

  @BeforeAll
  public static void setUp() {
    EnvironmentVariables.JDBC_URL = "jdbc:sqlite:test_db.db";
  }

  @BeforeEach
  public void setUpEach() {
    deleteIntolerance();
    insertIntolerance();
  }

  @Test
  public void listIntolerances() {
    IntoleranceService intoleranceService = new IntoleranceService();
    assertEquals(2, intoleranceService.listIntolerances().size());
  }

  private void insertIntolerance() {
    try (var connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      var preparedStatement =
          connection.prepareStatement("INSERT INTO intolerances (name) VALUES (?), (?);");
      preparedStatement.setString(1, "test");
      preparedStatement.setString(2, "test2");
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void deleteIntolerance() {
    try (var connection = DriverManager.getConnection(EnvironmentVariables.JDBC_URL)) {
      var preparedStatement = connection.prepareStatement("DELETE FROM intolerances;");
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
