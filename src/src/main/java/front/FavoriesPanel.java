package front;

import back.pojo.Recipe;
import back.services.RecipeService;
import back.services.ShoppingListService;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class FavoriesPanel {

  private final int userId;
  private final JPanel mainPanel;
  private final JPanel recipesPanel;
  private final RecipeService recipeService;
  private final ShoppingListService shoppingListService;

  public FavoriesPanel(int userId) {
    this.userId = userId;
    mainPanel = new JPanel(new BorderLayout());
    recipesPanel = new JPanel(new GridLayout(0, 1));
    recipeService = new RecipeService();
    shoppingListService = new ShoppingListService();
  }

  public JPanel getPanel() {
    mainPanel.removeAll();
    JPanel titlePanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("List of favorites");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setBackground(Color.decode("#E64A19"));
    titlePanel.add(titleLabel, BorderLayout.WEST);
    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(getRecipesPanel(), BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
    return mainPanel;
  }

  private JScrollPane getRecipesPanel() {
    recipesPanel.removeAll();
    List<Recipe> recipes = recipeService.listFavorites(userId);
    for (Recipe recipe : recipes) {
      recipesPanel.add(renderRecipePanel(recipe));
    }
    recipesPanel.revalidate();
    recipesPanel.repaint();
    return new JScrollPane(recipesPanel);
  }

  private JPanel renderRecipePanel(Recipe recipe) {
    JPanel recipePanel = new JPanel();
    recipePanel.add(new JLabel(recipe.getTitle()));
    JButton removeButton = new JButton("Remove");
    removeButton.setBackground(Color.decode("#E64A19"));
    removeButton.setForeground(Color.WHITE);
    JButton addToShoppingListButton = new JButton("Add to shopping list");
    removeButton.addActionListener(
        e -> {
          recipeService.deleteRecipeFromFavorites(userId, recipe);
          recipesPanel.remove(recipePanel);
          recipesPanel.revalidate();
          recipesPanel.repaint();
        });
    addToShoppingListButton.addActionListener(
        e -> shoppingListService.saveRecipeToShoppingList(recipe, userId));
    recipePanel.add(addToShoppingListButton);
    recipePanel.add(removeButton);
    return recipePanel;
  }
}
