package back.services;

public interface Services<T> {
  void create(T o);

  T read(int id);

  void update(T o);

  void delete(T o);
}
