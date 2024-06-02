package front;

import back.pojo.Ingredient;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlertFrame {

  private int maxDeltaTimeInDays;
  private List<Ingredient> ingredients;

  public AlertFrame(List<Ingredient> ingredients, int maxDeltaTime) {
    this.ingredients = new ArrayList<>(ingredients);
    this.maxDeltaTimeInDays = maxDeltaTime;
    generateAlertMessage();
  }

  private String[] generateAlertMessage() {
    LocalDate currentDate = LocalDate.now();
    StringBuilder almostExpiredText = new StringBuilder();
    StringBuilder alreadyExpiredText = new StringBuilder();

    Iterator<Ingredient> ingredientIterator = ingredients.iterator();

    while (ingredientIterator.hasNext()) {
      Ingredient ingredient = ingredientIterator.next();
      java.util.Date dateUtil = new java.util.Date();
      LocalDate datePeremption = ingredient.getExpirationDate().toLocalDate();
      Date actualDate = new Date(dateUtil.getTime());
      long differenceInDays = datePeremption.toEpochDay() - actualDate.toLocalDate().toEpochDay();

      if (differenceInDays <= maxDeltaTimeInDays) {
        if (differenceInDays >= 0) {
          almostExpiredText.append("- ");
          almostExpiredText.append(ingredient.getName());
          almostExpiredText.append("\n");
        } else {
          alreadyExpiredText.append("- ");
          alreadyExpiredText.append(ingredient.getName());
          alreadyExpiredText.append("\n");
        }
      }
    }
    return new String[] {String.valueOf(almostExpiredText), String.valueOf(alreadyExpiredText)};
  }

  public String getAlertMessage() {
    String[] alertMessages = generateAlertMessage();
    String displayedString = "";
    if (!alertMessages[0].equals("")) {
      String prefix = "The following ingredients expire in 3 days or less: \n";
      displayedString = displayedString + prefix + alertMessages[0];
    }
    if (!alertMessages[1].equals("")) {
      String prefix = "The following ingredients have expired: \n";
      displayedString = displayedString + prefix + alertMessages[1];
    }
    return displayedString;
  }
}
