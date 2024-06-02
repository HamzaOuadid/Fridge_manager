package back.pojo;

import java.util.ArrayList;
import java.util.List;

public class User {
  private final String username;
  private final String hashedPassword;
  private final int id;
  private final List<Intolerance> intolerances = new ArrayList<>();

  public User(String username, String hashedPassword, int id) {
    this.username = username;
    this.hashedPassword = hashedPassword;
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public int getId() {
    return id;
  }
}
