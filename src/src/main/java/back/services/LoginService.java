package back.services;

import back.pojo.User;

public class LoginService {
  public static User authenticate(String username, String password) {
    SQLiteConnector sqLiteConnector = new SQLiteConnector();
    return sqLiteConnector.getUser(username, password);
  }
}
