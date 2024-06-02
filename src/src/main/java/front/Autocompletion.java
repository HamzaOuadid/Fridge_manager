package front;

import back.services.IngredientService;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Autocompletion implements DocumentListener {

  private IngredientService ingredientService;

  private JTextField addTextField;

  public JPanel completion;

  public Autocompletion(
      JTextField addTextField, IngredientService ingredientService, JPanel addBarContainer) {
    this.addTextField = addTextField;
    this.ingredientService = ingredientService;
    this.completion = addBarContainer;
    this.completion.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.completion.setBackground(Color.decode("#B2DFDB"));
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    searchItems();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    searchItems();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    searchItems();
  }

  private void searchItems() {
    completion.removeAll();

    List<String> listMatch =
        ingredientService.isIngredientInList(addTextField.getText().toLowerCase());
    for (String element : listMatch) {
      JLabel label = new JLabel(element);
      label.setOpaque(true);
      label.setBackground(Color.decode("#00796B"));
      label.setForeground(Color.WHITE);
      label.setBorder(new EmptyBorder(5, 5, 5, 5));

      label.addMouseListener(
          new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              addTextField.setText(label.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
              // Changer le curseur lors du survol
              label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
              // Revenir au curseur par défaut lorsque la souris quitte
              label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
          });

      completion.add(label);
    }
    completion.revalidate();
    completion.repaint();
  }
}
