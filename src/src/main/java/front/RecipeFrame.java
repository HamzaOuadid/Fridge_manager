package front;

import back.pojo.Ingredient;
import back.pojo.Intolerance;
import back.pojo.NutritionDetails;
import back.pojo.Recipe;
import back.services.IngredientService;
import back.services.RecipeService;
import back.services.ShoppingListService;
import back.services.UserService;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RecipeFrame {
  private CardLayout cardLayout;
  private JPanel cardPanel;
  private JPanel listPanel;
  private JPanel detailPanel;
  private JTable recipeTable;
  private DefaultTableModel tableModel;
  private JLabel imageLabel;
  private JTextArea detailTextArea;
  private JLabel titleLabel;
  private final RecipeService recipeService;
  private final IngredientService ingredientService;
  private final JPanel mainPanel;
  private final int userId;
  private Recipe selectedRecipe = null;
  private final ShoppingListService shoppingListService = new ShoppingListService();
  private final UserService userService = new UserService();
  private JCheckBox jCheckBox;

  private List<Recipe> recipes;

  public RecipeFrame(int userId) {
    List<Recipe> recipes = new ArrayList<>();
    this.userId = userId;
    this.mainPanel = new JPanel(new BorderLayout());
    recipeService = new RecipeService();
    ingredientService = new IngredientService();
    jCheckBox = new JCheckBox("in favorites");
    jCheckBox.setEnabled(false);
  }

  public JPanel getPanel() {
    mainPanel.removeAll();
    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);

    initListPanel();
    initDetailPanel();

    cardPanel.add(listPanel, "List");
    cardPanel.add(detailPanel, "Detail");
    mainPanel.add(cardPanel);
    return mainPanel;
  }

  private void initListPanel() {
    recipes = fetchAndPopulateRecipes();
    listPanel = new JPanel(new BorderLayout());
    JLabel listTitle = new JLabel("Our suggested recipes");
    listTitle.setFont(new Font("Arial", Font.BOLD, 24));
    listTitle.setHorizontalAlignment(SwingConstants.CENTER);
    listTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    String[] columnNames = {"ID", "Name", "Missed ingredients count", "Used ingredients count"};
    tableModel = new DefaultTableModel(columnNames, 0);
    recipeTable = new JTable(tableModel);
    recipeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    for (Recipe recipe : recipes) {
      tableModel.addRow(
          new Object[] {
            String.valueOf(recipe.getId()),
            recipe.getTitle(),
            recipe.getMissedIngredientCount(),
            recipe.getUsedIngredients().size()
          });
    }
    recipeTable.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            int selectedRow = recipeTable.getSelectedRow();
            if (selectedRow >= 0) {
              String idString = (String) tableModel.getValueAt(selectedRow, 0);
              int id = Integer.parseInt(idString);
              showRecipeDetails(id);
            }
          }
        });

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton sortByMissedIngredientsButton = getSortByMissedIngredientsButton();
    JButton sortByUsedIngredientsButton = getSortByUsedIngredientsButton();
    buttonPanel.add(sortByMissedIngredientsButton);
    buttonPanel.add(sortByUsedIngredientsButton);

    listPanel.add(listTitle, BorderLayout.NORTH);
    listPanel.add(buttonPanel, BorderLayout.SOUTH);
    listPanel.add(new JScrollPane(recipeTable), BorderLayout.CENTER);
  }

  private JButton getSortByUsedIngredientsButton() {
    JButton sortButton = new JButton("Sort by used ingredients");
    sortButton.setSize(100, 100);
    return getjButton(sortButton, Comparator.comparingInt(Recipe::getUsedIngredientCount));
  }

  private JButton getjButton(JButton sortButton, Comparator<Recipe> recipeComparator) {
    sortButton.setFont(new Font("Arial", Font.PLAIN, 14));
    sortButton.addActionListener(
        e -> {
          tableModel.setRowCount(0);
          recipes.sort(recipeComparator.reversed());
          for (Recipe recipe : recipes) {
            tableModel.addRow(
                new Object[] {
                  String.valueOf(recipe.getId()),
                  recipe.getTitle(),
                  recipe.getMissedIngredientCount(),
                  recipe.getUsedIngredients().size()
                });
            mainPanel.revalidate();
            mainPanel.repaint();
          }
        });
    return sortButton;
  }

  private JButton getSortByMissedIngredientsButton() {
    JButton sortButton = new JButton("Sort by missing ingredients");
    sortButton.setSize(100, 100);
    return getjButton(sortButton, Comparator.comparingInt(Recipe::getMissedIngredientCount));
  }

  private void initDetailPanel() {
    detailPanel = new JPanel(new BorderLayout(10, 10));
    titleLabel = new JLabel("", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

    imageLabel = new JLabel("", SwingConstants.CENTER);

    detailTextArea = new JTextArea(10, 30);
    detailTextArea.setEditable(false);
    detailTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

    JButton addToFavoriteButton = new JButton("Add to favorite");
    addToFavoriteButton.setFont(new Font("Arial", Font.PLAIN, 14));
    addToFavoriteButton.addActionListener(
        e -> {
          recipeService.addRecipeToFavorite(userId, selectedRecipe);
          jCheckBox.setSelected(true);
        });

    JButton addButton = new JButton("Add recipe to shopping list");
    addButton.setFont(new Font("Arial", Font.PLAIN, 14));
    addButton.addActionListener(
        e -> shoppingListService.saveRecipeToShoppingList(selectedRecipe, userId));

    JButton backButton = new JButton("Back to Recipes");
    backButton.setFont(new Font("Arial", Font.PLAIN, 14));
    backButton.addActionListener(e -> cardLayout.show(cardPanel, "List"));

    detailPanel.add(titleLabel, BorderLayout.NORTH);

    JPanel imagePanel = new JPanel(new BorderLayout());
    imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    imagePanel.add(imageLabel, BorderLayout.CENTER);

    detailPanel.add(imagePanel, BorderLayout.WEST);

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    textPanel.add(new JScrollPane(detailTextArea));

    detailPanel.add(textPanel, BorderLayout.CENTER);
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    southPanel.add(jCheckBox);
    southPanel.add(addToFavoriteButton);
    southPanel.add(addButton);
    southPanel.add(backButton);
    detailPanel.add(southPanel, BorderLayout.SOUTH);
  }

  private void showRecipeDetails(int recipeId) {
    int selectedRecipeIndex = findIndexById(recipes, recipeId);
    selectedRecipe = recipes.get(selectedRecipeIndex);
    List<Recipe> favoriteRecipe = recipeService.listFavorites(userId);
    if (selectedRecipe != null) {
      titleLabel.setText(selectedRecipe.getTitle());
      try {
        URL imageURL = new URL(selectedRecipe.getImage());
        BufferedImage image = ImageIO.read(imageURL);
        ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        imageLabel.setIcon(imageIcon);
      } catch (IOException e) {
        e.printStackTrace();
        // Handle any errors that occur while fetching the image
      }
      for (Recipe recipe : favoriteRecipe) {
        jCheckBox.setSelected(recipe.getId() == selectedRecipe.getId());
      }

      detailTextArea.setText(
          getRecipeDetails(selectedRecipe) + getNutritionDetails(selectedRecipe.getId()));
      cardLayout.show(cardPanel, "Detail");
    }
  }

  private int findIndexById(List<Recipe> recipes, int id) {
    for (int i = 0; i < recipes.size(); i++) {
      if (recipes.get(i).getId() == id) {
        return i;
      }
    }
    return -1;
  }

  private String getRecipeDetails(Recipe recipe) {
    StringBuilder details = new StringBuilder();
    details.append("ID: ").append(recipe.getId()).append("\n");
    details.append("Title: ").append(recipe.getTitle()).append("\n");
    details.append("Likes: ").append(recipe.getLikes()).append("\n");
    details.append("Used Ingredient Count: ").append(recipe.getUsedIngredientCount()).append("\n");
    details
        .append("Missed Ingredient Count: ")
        .append(recipe.getMissedIngredientCount())
        .append("\n");

    appendIngredientList(details, "Missed Ingredients: ", recipe.getMissedIngredients());
    appendIngredientList(details, "Used Ingredients: ", recipe.getUsedIngredients());
    appendIngredientList(details, "Unused Ingredients: ", recipe.getUnusedIngredients());
    return details.toString();
  }

  private String getNutritionDetails(int recipeId) {
    NutritionDetails nutritionDetails = recipeService.getNutritionDetailsForRecipe(recipeId);
    StringBuilder details = new StringBuilder("\n--- Nutrition Details ---\n");

    if (nutritionDetails != null && nutritionDetails.getNutrients() != null) {
      for (NutritionDetails.NutrientDetail nutrient : nutritionDetails.getNutrients()) {
        details
            .append(nutrient.getName())
            .append(": ")
            .append(nutrient.getAmount())
            .append(" ")
            .append(nutrient.getUnit())
            .append(" (")
            .append(nutrient.getPercentOfDailyNeeds())
            .append("% of daily need)\n");
      }
    } else {
      details.append("Nutrition details are not available.");
    }

    return details.toString();
  }

  private void appendIngredientList(
      StringBuilder details, String title, List<Recipe.Ingredient> ingredients) {
    details.append(title);
    if (ingredients == null || ingredients.isEmpty()) {
      details.append("None\n");
    } else {
      for (Recipe.Ingredient ingredient : ingredients) {
        details
            .append("- ")
            .append(ingredient.getName())
            .append(", Amount: ")
            .append(ingredient.getAmount())
            .append(" ")
            .append(ingredient.getUnit())
            .append("\n");
      }
    }
  }

  private void sortRecipesByMissingIngredients(List<Recipe> recipes) {

    // Clear the table

  }

  private List<Recipe> fetchAndPopulateRecipes() {
    Date actualDate = new Date(System.currentTimeMillis());
    List<Ingredient> ingredients = ingredientService.List(userId);
    List<Ingredient> ingredientsNotExpired =
        ingredients.stream()
            .filter(
                ingredient -> {
                  int result =
                      actualDate
                          .toLocalDate()
                          .compareTo(ingredient.getExpirationDate().toLocalDate());
                  return result <= 0;
                })
            .toList();
    System.out.println(ingredientsNotExpired);
    List<String> ingredientsString =
        ingredientsNotExpired.stream().map(Ingredient::getName).toList();
    List<Intolerance> intolerances = userService.getIntolerances(userId);

    if (intolerances.isEmpty()) {
      // If no intolerances, fetch recipes based on findbyingredients
      recipes = recipeService.getRecipesByIngredients(ingredientsString);
    } else {
      // If there are intolerances, fetch recipes with complex search considering both ingredients
      // and intolerances
      recipes = recipeService.getRecipeWithIntolerance(ingredientsString, intolerances);
    }

    sortRecipesByMissingIngredients(recipes);
    return recipes;
  }
}
