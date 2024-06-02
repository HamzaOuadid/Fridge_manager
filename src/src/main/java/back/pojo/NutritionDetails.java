package back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionDetails {
  private List<NutrientDetail> nutrients;
  private List<Property> properties;
  private List<Flavonoid> flavonoids;
  private CaloricBreakdown caloricBreakdown;

  // Getters and setters for NutritionDetails
  public List<NutrientDetail> getNutrients() {
    return nutrients;
  }

  public void setNutrients(List<NutrientDetail> nutrients) {
    this.nutrients = nutrients;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  public List<Flavonoid> getFlavonoids() {
    return flavonoids;
  }

  public void setFlavonoids(List<Flavonoid> flavonoids) {
    this.flavonoids = flavonoids;
  }

  public CaloricBreakdown getCaloricBreakdown() {
    return caloricBreakdown;
  }

  public void setCaloricBreakdown(CaloricBreakdown caloricBreakdown) {
    this.caloricBreakdown = caloricBreakdown;
  }

  public static class NutrientDetail {
    private String name;
    private double amount;
    private String unit;
    private double percentOfDailyNeeds;

    // Getters and setters for NutrientDetail
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public double getAmount() {
      return amount;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

    public double getPercentOfDailyNeeds() {
      return percentOfDailyNeeds;
    }

    public void setPercentOfDailyNeeds(double percentOfDailyNeeds) {
      this.percentOfDailyNeeds = percentOfDailyNeeds;
    }
  }

  public static class Property {
    private String name;
    private double amount;
    private String unit;

    // Getters and setters for Property
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public double getAmount() {
      return amount;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }
  }

  public static class Flavonoid {
    private String name;
    private double amount;
    private String unit;

    // Getters and setters for Flavonoid
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public double getAmount() {
      return amount;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }
  }

  public static class CaloricBreakdown {
    private double percentProtein;
    private double percentFat;
    private double percentCarbs;

    // Getters and setters for CaloricBreakdown
    public double getPercentProtein() {
      return percentProtein;
    }

    public void setPercentProtein(double percentProtein) {
      this.percentProtein = percentProtein;
    }

    public double getPercentFat() {
      return percentFat;
    }

    public void setPercentFat(double percentFat) {
      this.percentFat = percentFat;
    }

    public double getPercentCarbs() {
      return percentCarbs;
    }

    public void setPercentCarbs(double percentCarbs) {
      this.percentCarbs = percentCarbs;
    }
  }
}
