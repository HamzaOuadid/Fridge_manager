package back.services;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

  @BeforeAll
  public static void setUp() {
    EnvironmentVariables.JDBC_URL = "jdbc:sqlite:test_db.db";
  }

}