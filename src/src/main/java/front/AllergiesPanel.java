package front;

import back.pojo.Intolerance;
import back.services.IntoleranceService;
import back.services.UserService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class AllergiesPanel {
  private final int userId;

  private IntoleranceService intoleranceService;
  private UserService userService;
  private JPanel mainPanel;

  public AllergiesPanel(int userId) {
    this.userId = userId;
    intoleranceService = new IntoleranceService();
    userService = new UserService();
    mainPanel = new JPanel(new BorderLayout());
  }

  public JPanel getPanel() {
    mainPanel.removeAll();
    List<Intolerance> intoleranceList = intoleranceService.listIntolerances();
    List<Intolerance> intoleranceListUser = userService.getIntolerances(userId);
    // Création de textes et polices
    JLabel selection = new JLabel("Select your allergies :");
    JLabel allergiesConnues = new JLabel("List of available allergies :");

    // Esthétique de la fenêtre
    selection.setBorder(new EmptyBorder(0, 0, 20, 0));
    Font police = new Font("Arial", Font.ITALIC, 18);
    Font police2 = new Font("Arial", Font.BOLD, 24);
    allergiesConnues.setFont(police);
    selection.setFont(police2);

    mainPanel.add(selection);
    mainPanel.add(allergiesConnues);

    // Création de la liste d'allergies
    renderCheckboxesList(intoleranceList, intoleranceListUser, mainPanel);

    // Ajout des composants au conteneur

    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    return mainPanel;
  }

  private void renderCheckboxesList(
      List<Intolerance> intoleranceList,
      List<Intolerance> intoleranceListUser,
      JPanel panelPrincipal) {
    for (Intolerance intolerance : intoleranceList) {
      JCheckBox checkBox = new JCheckBox(intolerance.getName());
      for (Intolerance intoleranceUser : intoleranceListUser) {
        if (intoleranceUser.getId() == intolerance.getId()) {
          checkBox.setSelected(true);
        }
      }
      checkBox.addActionListener(
          e -> {
            if (checkBox.isSelected()) {
              userService.setIntolerance(userId, intolerance);
            } else {
              userService.deleteIntolerance(userId, intolerance);
            }
          });
      panelPrincipal.add(checkBox);
    }
  }
}
