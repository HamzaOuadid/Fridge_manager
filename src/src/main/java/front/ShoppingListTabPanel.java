package front;

import javax.swing.*;

public class ShoppingListTabPanel {

  ShoppingListIngredientsPanel ingredientsPanel;
  ShoppingListRecipePanel recipePanel;
  JTabbedPane jTabbedPane;

  public ShoppingListTabPanel(int userId) {
    jTabbedPane = new JTabbedPane();
    ingredientsPanel = new ShoppingListIngredientsPanel(userId);
    recipePanel = new ShoppingListRecipePanel(userId);
  }

  public JTabbedPane getPanel() {
    jTabbedPane.removeAll();
    jTabbedPane.add("Recipes", recipePanel.getPanel());
    jTabbedPane.add("Ingredients", ingredientsPanel.getPanel());
    return jTabbedPane;
  }
}
