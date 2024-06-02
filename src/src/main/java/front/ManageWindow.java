package front;

import back.pojo.Ingredient;
import back.services.IngredientService;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class ManageWindow extends JPanel {

  @Serial private static final long serialVersionUID = 1L;
  private String[] options = {"kg", "l", "ml"};
  private List<Ingredient> ingredients = new ArrayList<>();
  private java.util.Date dateUtil = new java.util.Date();

  private int user;

  public ManageWindow(int user) {
    Locale.setDefault(Locale.ENGLISH);

    this.user = user;

    try {
      UIManager.setLookAndFeel(new NimbusLookAndFeel());
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    this.setSize(1400, 800);
    IngredientService ingredientService = new IngredientService();
    this.ingredients = ingredientService.List(user);
    JPanel addBarContainer = new JPanel();
    JPanel ingredientList = this.ingredientContainerList();
    JScrollPane globalContainer = new JScrollPane(ingredientList);

    addBarContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
    JButton addButton = this.stylingButton("Add");
    JComboBox<String> comboBox = new JComboBox<>(this.options);
    JTextField addTextField = this.stylingTextField(40, "Add an ingredient to your fridge");
    JSpinner spinner = this.stylingSpinner();
    JDateChooser jDateChooser = this.stylingDateChooser();
    JPanel completion = new JPanel();
    addBarContainer.add(addTextField);
    addBarContainer.add(spinner);
    addBarContainer.add(comboBox);
    addBarContainer.add(jDateChooser);
    addBarContainer.add(addButton);

    addTextField
        .getDocument()
        .addDocumentListener(new Autocompletion(addTextField, ingredientService, completion));

    ActionListener addButtonListener =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            IngredientService ingredientService = new IngredientService();
            if (jDateChooser.getDate() == null) {
              JOptionPane.showMessageDialog(
                  addBarContainer,
                  "The date you provided has the wrong format.",
                  "Warning",
                  JOptionPane.INFORMATION_MESSAGE);
              return;
            }
            Date date = new Date(jDateChooser.getDate().getTime());
            String nom = addTextField.getText();
            float selectedValue = (int) spinner.getValue();
            String unit = (String) comboBox.getSelectedItem();
            if (nom.equals("Add an ingredient to your fridge")) {
              JOptionPane.showMessageDialog(
                  addBarContainer,
                  "Name your product !",
                  "Warning",
                  JOptionPane.INFORMATION_MESSAGE);
              return;
            }

            ingredients.add(new Ingredient(user, nom, selectedValue, unit, date));
            ingredientService.create(new Ingredient(user, nom, selectedValue, unit, date));
            updateIngredientsPanel(ingredientList);

            revalidate();
            repaint();
          }
        };

    for (Component component : addBarContainer.getComponents()) {
      if (component instanceof JComponent) {
        ((JComponent) component)
            .addKeyListener(
                new KeyAdapter() {
                  @Override
                  public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                      addButtonListener.actionPerformed(
                          new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }
                  }
                });
      }
    }

    Action addIngredientAction =
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            addButtonListener.actionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
          }
        };

    KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    jDateChooser
        .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(enterKey, "addIngredient");
    jDateChooser.getActionMap().put("addIngredient", addIngredientAction);

    addButton.addActionListener(addButtonListener);

    addBarContainer.setBackground(Color.decode("#B2DFDB"));
    add(addBarContainer);
    add(completion);
    add(globalContainer);
    setVisible(true);
  }

  private JPanel ingredientContainerList() {
    int numberLine = this.ingredients.size();
    if (this.ingredients.size() < 11) {
      numberLine = 11;
    }

    JPanel IngredientContainerList = new JPanel(new GridLayout(numberLine, 1));
    IngredientContainerList.setBackground(Color.decode("#E0F2F1"));
    IngredientContainerList.setBorder(null);

    for (Ingredient ingredient : this.ingredients) {
      IngredientContainerList.add(
          containerIngredient(
              ingredient.getName(),
              ingredient.getQuantity(),
              ingredient.getUnit(),
              ingredient.getExpirationDate()));
    }
    return IngredientContainerList;
  }

  private JPanel containerIngredient(
      String name, float quantity, String unit, Date expirationDate) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy", Locale.ENGLISH);

    JPanel containerIngredient = new JPanel(new FlowLayout(FlowLayout.LEFT, 130, 10));
    JPanel quantityUnitValue = new JPanel();
    JLabel title = new JLabel(name);
    JLabel quantityValue = new JLabel(String.valueOf(quantity));
    JLabel unitValue = new JLabel(unit);
    JLabel date = new JLabel("Expiration Date: " + expirationDate.toLocalDate().format(formatter));

    JButton buttonDelete = new JButton("Remove");
    JButton buttonEdit = new JButton("Edit");

    buttonDelete.setFont(buttonDelete.getFont().deriveFont(12f));
    buttonDelete.setForeground(Color.WHITE);
    buttonDelete.setBackground(Color.decode("#E64A19"));
    buttonDelete.setMargin(new Insets(4, 4, 4, 4));
    buttonDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

    buttonEdit.setFont(buttonDelete.getFont().deriveFont(12f));
    buttonEdit.setForeground(Color.WHITE);
    buttonEdit.setBackground(Color.decode("#379cfc"));
    buttonEdit.setMargin(new Insets(4, 4, 4, 4));
    buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

    quantityUnitValue.add(quantityValue);
    quantityUnitValue.add(unitValue);

    Date actualDate = new Date(dateUtil.getTime());
    long differenceInDays =
        expirationDate.toLocalDate().toEpochDay() - actualDate.toLocalDate().toEpochDay();

    if (differenceInDays > 3) {
      containerIngredient.setBackground(Color.decode("#80CBC4"));
      quantityUnitValue.setBackground(Color.decode("#80CBC4"));
    } else if (differenceInDays >= 0) {
      containerIngredient.setBackground(Color.decode("#FFA500"));
      quantityUnitValue.setBackground(Color.decode("#FFA500"));
    } else {
      containerIngredient.setBackground(Color.decode("#FF0000"));
      quantityUnitValue.setBackground(Color.decode("#FF0000"));
      quantityValue.setForeground(Color.WHITE);
      title.setForeground(Color.WHITE);
      unitValue.setForeground(Color.WHITE);
      date.setForeground(Color.WHITE);
    }

    containerIngredient.setBorder(new LineBorder(Color.decode("#26A69A"), 1));

    containerIngredient.add(title);
    containerIngredient.add(quantityUnitValue);
    containerIngredient.add(date);
    containerIngredient.add(buttonEdit);
    containerIngredient.add(buttonDelete);

    buttonDelete.addActionListener(
        new ActionListener() {

          public void actionPerformed(ActionEvent e) {

            Container parent = buttonDelete.getParent().getParent();
            if (parent instanceof JPanel) {

              JLabel label = (JLabel) containerIngredient.getComponent(0);

              for (int i = 0; i <= ingredients.size(); i++) {
                if (Objects.equals(ingredients.get(i).getName(), label.getText())) {
                  IngredientService ingredientService = new IngredientService();
                  ingredientService.delete(ingredients.get(i));
                  ingredients.remove(i);
                  parent.remove(containerIngredient);
                  parent.revalidate();
                  parent.repaint();
                  return;
                }
              }
            }
          }
        });

    buttonEdit.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {

            Container valueUnitContainer = (Container) containerIngredient.getComponent(1);
            if (buttonEdit.getText() == "Edit") {
              buttonEdit.setText("Validate");
              buttonEdit.setBackground(Color.decode("#2EA44F"));

              JLabel lastQvalue = (JLabel) valueUnitContainer.getComponent(0);
              JLabel lastQunit = (JLabel) valueUnitContainer.getComponent(1);

              JTextField unitAsField = new JTextField(lastQunit.getText());
              JTextField valueAsField = new JTextField(lastQvalue.getText());

              unitAsField.setPreferredSize(
                  new Dimension(30, unitAsField.getPreferredSize().height));
              valueAsField.setPreferredSize(
                  new Dimension(50, valueAsField.getPreferredSize().height));

              valueUnitContainer.remove(0);
              valueUnitContainer.remove(0);

              valueUnitContainer.add(unitAsField);
              valueUnitContainer.add(valueAsField);

            } else {
              JLabel ingredientName = (JLabel) containerIngredient.getComponent(0);

              for (int i = 0; i <= ingredients.size(); i++) {
                if (Objects.equals(ingredients.get(i).getName(), ingredientName.getText())) {

                  List<String> listeDeChaines = Arrays.asList(options);
                  JTextField newQunit = (JTextField) valueUnitContainer.getComponent(0);
                  JTextField newQvalue = (JTextField) valueUnitContainer.getComponent(1);

                  if (!listeDeChaines.contains(newQunit.getText())) {

                    JOptionPane.showMessageDialog(
                        containerIngredient,
                        "The provided unit does not match any of the supported ones.",
                        "Warning",
                        JOptionPane.INFORMATION_MESSAGE);

                  } else {
                    buttonEdit.setText("Edit");
                    buttonEdit.setBackground(Color.decode("#379cfc"));

                    valueUnitContainer.remove(0);
                    valueUnitContainer.remove(0);

                    valueUnitContainer.add(new JLabel(newQvalue.getText()));
                    valueUnitContainer.add(new JLabel(newQunit.getText()));

                    ingredients.get(i).setUnit(newQunit.getText());
                    ingredients.get(i).setQuantity(Float.parseFloat(newQvalue.getText()));

                    IngredientService ingredientService = new IngredientService();
                    ingredientService.update(ingredients.get(i));
                    ingredients = ingredientService.List(user);
                    return;
                  }
                }
              }
            }
          }
        });

    return containerIngredient;
  }

  /*
   * Related to component styling
   */

  private JTextField stylingTextField(int number, String placeholder) {
    JTextField textField = new JTextField(number);
    textField.setFont(textField.getFont().deriveFont(14f));
    textField.setBorder(BorderFactory.createLineBorder(Color.decode("#00796B")));
    textField.setColumns(number);
    textField.setText(placeholder);

    textField.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
              textField.setText("");
            }
          }

          @Override
          public void focusLost(FocusEvent e) {
            if (textField.getText().isEmpty()) {
              textField.setText(placeholder);
            }
          }
        });

    return textField;
  }

  private JButton stylingButton(String text) {
    JButton button = new JButton(text);
    button.setFont(button.getFont().deriveFont(12f));
    button.setForeground(Color.WHITE);
    button.setBackground(Color.decode("#00796B"));
    button.setBorderPainted(false);
    button.setMargin(new Insets(8, 50, 8, 50));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return button;
  }

  private JSpinner stylingSpinner() {
    SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 1000, 1);
    JSpinner spinner = new JSpinner(model);

    JComponent editor = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    editor.setFont(new Font("Arial", Font.BOLD, 14));
    editor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    return spinner;
  }

  private JDateChooser stylingDateChooser() {
    JDateChooser jDateChooser = new JDateChooser();
    jDateChooser.setDate(new Date(dateUtil.getTime()));
    jDateChooser.setPreferredSize(new Dimension(150, 30));
    return jDateChooser;
  }

  /*
   * Relate to logic part
   *
   */

  public void updateIngredientsPanel(JPanel ingredientPanel) {
    IngredientService ingredientService = new IngredientService();
    ingredients = ingredientService.List(this.user);

    int numberLine = this.ingredients.size();
    if (this.ingredients.size() < 11) {
      numberLine = 11;
    }

    ingredientPanel.setLayout(new GridLayout(numberLine, 1));

    ingredientPanel.removeAll();

    for (Ingredient ingredient : ingredients) {
      JPanel container =
          containerIngredient(
              ingredient.getName(),
              ingredient.getQuantity(),
              ingredient.getUnit(),
              ingredient.getExpirationDate());
      ingredientPanel.add(container);
    }

    ingredientPanel.revalidate();
    ingredientPanel.repaint();
  }
}
