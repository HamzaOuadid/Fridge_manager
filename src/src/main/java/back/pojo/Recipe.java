package back.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public class Recipe {
  private int id;
  private String title;
  private String image;
  private String imageType;
  private int likes;
  private int usedIngredientCount;
  private int missedIngredientCount;
  private List<Ingredient> missedIngredients;
  private List<Ingredient> usedIngredients;
  private List<Ingredient> unusedIngredients;

  public Recipe(int id, String name, String image) {
    this.id = id;
    this.title = name;
    this.image = image;
  }

  public Recipe(int id, String name) {
    this.id = id;
    this.title = name;
  }

  @JsonCreator
  public Recipe(
      @JsonProperty("id") int id,
      @JsonProperty("title") String title,
      @JsonProperty("image") String image,
      @JsonProperty("imageType") String imageType,
      @JsonProperty("usedIngredientCount") int usedIngredientCount,
      @JsonProperty("missedIngredientCount") int missedIngredientCount,
      @JsonProperty("missedIngredients") List<Ingredient> missedIngredients,
      @JsonProperty("usedIngredients") List<Ingredient> usedIngredients,
      @JsonProperty("unusedIngredients") List<Ingredient> unusedIngredients,
      @JsonProperty("Likes") int likes) {
    this.id = id;
    this.title = title;
    this.image = image;
    this.imageType = imageType;
    this.usedIngredientCount = usedIngredientCount;
    this.missedIngredientCount = missedIngredientCount;
    this.missedIngredients = missedIngredients;
    this.usedIngredients = usedIngredients;
    this.likes = likes;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  public int getUsedIngredientCount() {
    return usedIngredientCount;
  }

  public void setUsedIngredientCount(int usedIngredientCount) {
    this.usedIngredientCount = usedIngredientCount;
  }

  public int getMissedIngredientCount() {
    return missedIngredientCount;
  }

  public void setMissedIngredientCount(int missedIngredientCount) {
    this.missedIngredientCount = missedIngredientCount;
  }

  public List<Ingredient> getMissedIngredients() {
    return missedIngredients;
  }

  public void setMissedIngredients(List<Ingredient> missedIngredients) {
    this.missedIngredients =
        (missedIngredients != null) ? missedIngredients : Collections.emptyList();
  }

  public List<Ingredient> getUsedIngredients() {
    return usedIngredients;
  }

  public void setUsedIngredients(List<Ingredient> usedIngredients) {
    this.usedIngredients = (usedIngredients != null) ? usedIngredients : Collections.emptyList();
  }

  public List<Ingredient> getUnusedIngredients() {
    return unusedIngredients;
  }

  public void setUnusedIngredients(List<Ingredient> unusedIngredients) {
    this.unusedIngredients =
        (unusedIngredients != null) ? unusedIngredients : Collections.emptyList();
  }

  // Inner class representing an ingredient within a recipe
  public static class Ingredient {
    private String aisle;
    private float amount;
    private int id;
    private String image;
    private List<String> meta;
    private String name;
    private String original;
    private String originalName;
    private String unit;
    private String unitLong;
    private String unitShort;
    private String extendedName;

    @JsonCreator
    public Ingredient(
        @JsonProperty("aisle") String aisle,
        @JsonProperty("amount") float amount,
        @JsonProperty("id") int id,
        @JsonProperty("image") String image,
        @JsonProperty("meta") List<String> meta,
        @JsonProperty("name") String name,
        @JsonProperty("original") String original,
        @JsonProperty("originalName") String originalName,
        @JsonProperty("unit") String unit,
        @JsonProperty("unitLong") String unitLong,
        @JsonProperty("unitShort") String unitShort,
        @JsonProperty("extendedName") String extendedNamen) {
      this.aisle = aisle;
      this.amount = amount;
      this.id = id;
      this.image = image;
      this.meta = (meta != null) ? meta : Collections.emptyList();
      this.name = name;
      this.original = original;
      this.originalName = originalName;
      this.unit = unit;
      this.unitLong = unitLong;
      this.unitShort = unitShort;
    }

    public Ingredient(double aDouble, String text, String text1, String text2) {
      this.amount = (float) aDouble;
      this.unit = text;
      this.name = text1;
      this.image = text2;
    }

    public String getAisle() {
      return aisle;
    }

    public void setAisle(String aisle) {
      this.aisle = aisle;
    }

    public float getAmount() {
      return amount;
    }

    public void setAmount(float amount) {
      this.amount = amount;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
    }

    public List<String> getMeta() {
      return meta;
    }

    public void setMeta(List<String> meta) {
      this.meta = (meta != null) ? meta : Collections.emptyList();
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getOriginal() {
      return original;
    }

    public void setOriginal(String original) {
      this.original = original;
    }

    public String getOriginalName() {
      return originalName;
    }

    public void setOriginalName(String originalName) {
      this.originalName = originalName;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

    public String getUnitLong() {
      return unitLong;
    }

    public void setUnitLong(String unitLong) {
      this.unitLong = unitLong;
      this.unitShort = unitShort;
    }

    public String getUnitShort() {
      return unitShort;
    }

    public void setUnitShort(String unitShort) {
      this.unitShort = unitShort;
    }

    public String getExtendedName() {
      return extendedName;
    }

    public void setExtendedName(String extendedName) {
      this.extendedName = extendedName;
    }

    @Override
    public String toString() {
      return "Ingredient{"
          + "aisle='"
          + aisle
          + '\''
          + ", amount="
          + amount
          + ", id="
          + id
          + ", image='"
          + image
          + '\''
          + ", meta="
          + meta
          + ", name='"
          + name
          + '\''
          + ", original='"
          + original
          + '\''
          + ", originalName='"
          + originalName
          + '\''
          + ", unit='"
          + unit
          + '\''
          + ", unitLong='"
          + unitLong
          + '\''
          + ", unitShort='"
          + unitShort
          + '\''
          + '}';
    }
  }

  @Override
  public String toString() {
    return "Recipe{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", image='"
        + image
        + '\''
        + ", imageType='"
        + imageType
        + '\''
        + ", usedIngredientCount="
        + usedIngredientCount
        + ", missedIngredientCount="
        + missedIngredientCount
        + ", missedIngredients="
        + missedIngredients
        + ", usedIngredients="
        + usedIngredients
        + '}';
  }
}
