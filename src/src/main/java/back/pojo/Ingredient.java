/*
 * File: Person.java
 * Author: Ghali MECHKOUR
 * Created: 19/11/2023
 * Description: Implements the class Ingredient
 */

package back.pojo;

import java.sql.Date;
import java.util.Objects;

/**
 * This class represents an ingredient It includes two constructors to handle if the calories
 * contained in an ingredient are unknown
 */
public class Ingredient {

  private int id;
  private int userId;
  private final String name;
  private float quantity;
  private String unit;
  private Date expirationDate;

  // Constructor : takes all parameters except the id. Said id will be set to null until it is
  // matched with the DB
  public Ingredient(int userId, String name, float quantity, String unit, Date expirationDate) {
    this.name = name;
    this.userId = userId;
    this.quantity = quantity;
    this.unit = unit;
    this.expirationDate = expirationDate;
  }

  public Ingredient(
      int id, int userId, String name, float quantity, String unit, Date expirationDate) {
    this.name = name;
    this.id = id;
    this.userId = userId;
    this.quantity = quantity;
    this.unit = unit;
    this.expirationDate = expirationDate;
  }

  public Ingredient(String name, float quantity, String unit) {
    this.name = name;
    this.quantity = quantity;
    this.unit = unit;
  }

  public Ingredient(String name, String unit, double amount) {
    this.name = name;
    this.unit = unit;
    this.quantity = (float) amount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public float getQuantity() {
    return quantity;
  }

  public void setQuantity(float quantity) {
    this.quantity = quantity;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Ingredient that)) return false;
    return id == that.id
        && userId == that.userId
        && Float.compare(that.quantity, quantity) == 0
        && Objects.equals(name, that.name)
        && Objects.equals(unit, that.unit)
        && Objects.equals(expirationDate, that.expirationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id, userId, quantity, unit, expirationDate);
  }

  @Override
  public String toString() {
    return "Ingredient{"
        + "name='"
        + name
        + '\''
        + ", id="
        + id
        + ", userId="
        + userId
        + ", quantity="
        + quantity
        + ", unit='"
        + unit
        + '\''
        + ", expirationDate="
        + expirationDate
        + '}';
  }
}
