package front;

import back.services.ShoppingListService;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ShoppingListRecipePanel {

  private final int userId;
  private final ShoppingListService shoppingListService;
  private final JPanel jpanel;

  public ShoppingListRecipePanel(int userId) {
    this.userId = userId;
    shoppingListService = new ShoppingListService();
    jpanel = new JPanel();
  }

  public JPanel getPanel() {
    jpanel.removeAll();
    List<String> recipes = shoppingListService.getRecipes(userId);
    if (recipes.size() <= 11) {
      jpanel.setLayout(new GridLayout(11, 1));
    } else {
      jpanel.setLayout(new GridLayout(recipes.size(), 1));
    }
    for (String recipe : recipes) {
      jpanel.add(getRecipeDetailPanel(recipe));
    }
    return jpanel;
  }

  public JPanel getRecipeDetailPanel(String recipesName) {
    JPanel recipePanel = new JPanel();
    recipePanel.setLayout(new BorderLayout());
    JLabel jLabel = new JLabel(recipesName);
    JButton removeRecipe = new JButton("Remove");
    removeRecipe.setFont(removeRecipe.getFont().deriveFont(12f));
    removeRecipe.setForeground(Color.WHITE);
    removeRecipe.setBackground(Color.decode("#E64A19"));
    removeRecipe.setMargin(new Insets(4, 4, 4, 4));
    removeRecipe.setCursor(new Cursor(Cursor.HAND_CURSOR));
    removeRecipe.addActionListener(
        e -> {
          shoppingListService.deleteRecipe(userId, recipesName);
          jpanel.remove(recipePanel);
          jpanel.revalidate();
          jpanel.repaint();
        });
    recipePanel.add(removeRecipe, BorderLayout.EAST);
    recipePanel.add(jLabel, BorderLayout.WEST);
    return recipePanel;
  }
}
