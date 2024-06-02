package back.pojo;

public class Intolerance {

  private int id;
  private String name;

  public Intolerance(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Intolerance{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
