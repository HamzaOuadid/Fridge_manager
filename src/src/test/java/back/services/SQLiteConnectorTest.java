package back.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteConnectorTest {

  @BeforeAll
  public static void setUp() {
    EnvironmentVariables.JDBC_URL = "jdbc:sqlite:test_db.db";
  }

  @Test
  public void getUser() {
    SQLiteConnector sqLiteConnector = new SQLiteConnector();
    assertEquals("admin", sqLiteConnector.getUser("admin", "admin").getUsername());
  }

}