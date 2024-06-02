package front;

import java.awt.*;
import java.io.Serial;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class WindowSelector extends JFrame {
  @Serial private static final long serialVersionUID = 1L;
  private final int userId;

  private ManageWindow manageWindow;
  private RecipeFrame recipeFrame;
  private ShoppingListTabPanel shoppingListTabPanel;
  private FavoriesPanel favoriesPanel;

  private AllergiesPanel allergiesFrame;

  public WindowSelector(int userId) {

    this.userId = userId;
    setTitle("RecipeApp"); // Title of the window
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);

    SwingUtilities.invokeLater(
        () -> {
          try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
          } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
          }

          manageWindow = new ManageWindow(userId);
          recipeFrame = new RecipeFrame(userId);
          shoppingListTabPanel = new ShoppingListTabPanel(userId);
          favoriesPanel = new FavoriesPanel(userId);
          allergiesFrame = new AllergiesPanel(userId);
          manageWindow.setLayout(new BoxLayout(manageWindow, BoxLayout.Y_AXIS));

          JToolBar toolBar = stylingToolBar();

          add(toolBar, BorderLayout.WEST);
          setVisible(true);
        });
  }

  /* Switch screen method */
  private void switchScreen(String screenName) {
    if ("fridge".equals(screenName)) {
      remove(allergiesFrame.getPanel());
      remove(shoppingListTabPanel.getPanel());
      remove(recipeFrame.getPanel());
      manageWindow.setVisible(true);
      remove(favoriesPanel.getPanel());
      add(manageWindow);
    } else if ("recipe".equals(screenName)) {
      remove(allergiesFrame.getPanel());
      remove(shoppingListTabPanel.getPanel());
      manageWindow.setVisible(false);
      remove(favoriesPanel.getPanel());
      add(recipeFrame.getPanel()); // Show the recipe panel
    } else if ("shopping".equals(screenName)) {
      manageWindow.setVisible(false);
      remove(recipeFrame.getPanel());
      remove(allergiesFrame.getPanel());
      remove(favoriesPanel.getPanel());
      add(shoppingListTabPanel.getPanel());
    } else if ("favories".equals(screenName)) {
      manageWindow.setVisible(false);
      remove(recipeFrame.getPanel());
      remove(shoppingListTabPanel.getPanel());
      remove(allergiesFrame.getPanel());
      add(favoriesPanel.getPanel());
    } else if ("allergies".equals(screenName)) {
      manageWindow.setVisible(false);
      remove(recipeFrame.getPanel());
      remove(shoppingListTabPanel.getPanel());
      remove(favoriesPanel.getPanel());
      add(allergiesFrame.getPanel());
    }

    // Revalidate and repaint to reflect changes
    revalidate();
    repaint();
  }

  JToolBar stylingToolBar() {
    JToolBar toolBar = new JToolBar();

    toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    toolBar.setFloatable(false);
    toolBar.setBackground(Color.decode("#004D40"));

    /* Create nav bar categories, add one if needed */
    JPanel buttonLayout = new JPanel(new GridLayout(20, 1));
    buttonLayout.setBackground(Color.decode("#004D40"));
    JButton fridgeButton = this.stylingButton("Fridge");
    JButton recipeButton = this.stylingButton("Recipe");
    JButton shoppingListButton = stylingButton("Shopping list");
    JButton allergiesButton = stylingButton("Allergies");
    JButton favoriesButton = stylingButton("Favories");
    buttonLayout.add(fridgeButton);
    buttonLayout.add(recipeButton);
    buttonLayout.add(shoppingListButton);
    buttonLayout.add(favoriesButton);
    buttonLayout.add(allergiesButton);
    toolBar.add(buttonLayout);

    fridgeButton.addActionListener(e -> switchScreen("fridge"));
    recipeButton.addActionListener(e -> switchScreen("recipe"));
    shoppingListButton.addActionListener(e -> switchScreen("shopping"));
    favoriesButton.addActionListener(e -> switchScreen("favories"));
    allergiesButton.addActionListener(e -> switchScreen("allergies"));
    return toolBar;
  }

  JButton stylingButton(String text) {
    JButton button = new JButton(text);
    button.setFont(button.getFont().deriveFont(12f));
    button.setForeground(Color.WHITE);
    button.setBorderPainted(false);
    button.setMargin(new Insets(8, 30, 8, 30));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setContentAreaFilled(false);

    button.addMouseListener(
        new java.awt.event.MouseAdapter() {
          public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(Color.decode("#00695C")); // Color on hover
            button.setOpaque(true);
          }

          public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setOpaque(false); // Default color
          }
        });

    return button;
  }
}
