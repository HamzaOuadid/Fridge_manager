package front;

import back.pojo.Ingredient;
import back.services.IngredientService;
import back.services.ShoppingListService;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

public class ShoppingListIngredientsPanel {

  private final int userId;
  private final ShoppingListService shoppingListService;
  private final IngredientService ingredientService;

  public ShoppingListIngredientsPanel(int userId) {
    this.userId = userId;
    shoppingListService = new ShoppingListService();
    ingredientService = new IngredientService();
  }

  public JScrollPane getPanel() {
    JPanel gridLayout = new JPanel(new BorderLayout());
    AtomicReference<List<Ingredient>> missingIngredients = new AtomicReference<>(new ArrayList<>());
    JButton generateShoppingList = new JButton("Generate ingredient list");
    gridLayout.add(generateShoppingList, BorderLayout.NORTH);
    JPanel recipePanel = new JPanel();
    gridLayout.add(recipePanel, BorderLayout.CENTER);
    generateShoppingList.addActionListener(
        e -> {
          recipePanel.removeAll();
          List<Ingredient> recipesIngredients = shoppingListService.getIngredientsOfRecipes(userId);
          List<Ingredient> fridgeIngredients = ingredientService.List(userId);
          if (recipesIngredients.isEmpty()) {
            recipePanel.removeAll();
            recipePanel.add(getAlertPanel());
            recipePanel.revalidate();
            recipePanel.repaint();
            return;
          }
          missingIngredients.set(
              shoppingListService.getMissingIngredients(fridgeIngredients, recipesIngredients));
          if (missingIngredients.get().size() <= 11) {
            recipePanel.setLayout(new GridLayout(0, 1));
          } else {
            recipePanel.setLayout(new GridLayout(0, 2));
          }
          for (Ingredient missingIngredient : missingIngredients.get()) {
            recipePanel.add(getIngredientsPanel(missingIngredient));
          }
          recipePanel.revalidate();
          recipePanel.repaint();
        });
    return new JScrollPane(gridLayout);
  }

  private JPanel getIngredientsPanel(Ingredient missingIngredients) {
    JPanel jPanel = new JPanel();
    JLabel name = new JLabel(missingIngredients.getName());
    JLabel amount = new JLabel(String.valueOf(missingIngredients.getQuantity()));
    JLabel unit = new JLabel(missingIngredients.getUnit());
    jPanel.add(name);
    jPanel.add(amount);
    jPanel.add(unit);
    return jPanel;
  }

  private JPanel getAlertPanel() {
    JPanel jPanel = new JPanel();
    JLabel jLabel = new JLabel("You do not have recipes in your shopping list");
    jLabel.setBackground(Color.decode("#E64A19"));
    jPanel.add(jLabel);
    return jPanel;
  }
}
