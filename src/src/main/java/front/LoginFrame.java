package front;

import back.pojo.Ingredient;
import back.pojo.User;
import back.services.IngredientService;
import back.services.LoginService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginFrame extends JFrame {
  private JTextField usernameField; // Champ de texte pour le nom d'utilisateur
  private JPasswordField passwordField; // Champ de texte pour le mot de passe

  // Constructeur de la classe LoginFrame
  public LoginFrame() {
    setTitle("Connexion"); // Titre de la fenêtre
    setDefaultCloseOperation(
        JFrame.EXIT_ON_CLOSE); // Fermer l'application lors de la fermeture de la fenêtre
    setSize(300, 150); // Définir la taille de la fenêtre
    setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

    // Création d'un panneau avec une disposition en grille de 3 lignes et 2 colonnes
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2));

    // Ajout d'une étiquette et d'un champ de texte pour le nom d'utilisateur
    panel.add(new JLabel("login:"));
    usernameField = new JTextField();
    panel.add(usernameField);

    // Ajout d'une étiquette et d'un champ de texte pour le mot de passe
    panel.add(new JLabel("Password:"));
    passwordField = new JPasswordField();
    passwordField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            // Vérifier si la touche pressée est la touche Entrée
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              attemptLogin(); // Appel de la méthode attemptLogin() lors de l'appui sur la touche
              // Entrée
            }
          }
        });
    panel.add(passwordField);

    // Création d'un bouton de connexion avec un écouteur d'événements
    JButton loginButton = new JButton("Log in");
    loginButton.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            // Vérifier si la touche pressée est la touche Entrée
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              attemptLogin(); // Appel de la méthode attemptLogin() lors de l'appui sur la touche
              // Entrée
            }
          }
        });
    loginButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            attemptLogin(); // Appel de la méthode attemptLogin() lors du clic sur le bouton
          }
        });

    panel.add(loginButton);

    // Ajout du panneau à la fenêtre
    add(panel);
    setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
  }

  // Méthode privée pour tenter la connexion
  private void attemptLogin() {
    String username = usernameField.getText(); // Récupération du nom d'utilisateur
    char[] passwordChars =
        passwordField
            .getPassword(); // Récupération du mot de passe sous forme de tableau de caractères
    String password =
        new String(passwordChars); // Conversion du tableau de caractères en chaîne de caractères

    User logedUser = LoginService.authenticate(username, password);

    // Appel de la méthode d'authentification du service LoginService
    if (logedUser != null && BCrypt.checkpw(password, logedUser.getHashedPassword())) {
      IngredientService ingredientService = new IngredientService();
      List<Ingredient> ingredients = ingredientService.List(logedUser.getId());
      AlertFrame alertFrame = new AlertFrame(ingredients, 3);

      WindowSelector windowSelector = new WindowSelector(logedUser.getId());
      this.dispose(); // On ferme la fenêtre lorsque qu'elle n'est plus utilisée

      String alertMessage = alertFrame.getAlertMessage();
      if (!alertMessage.equals("")) {
        JOptionPane.showMessageDialog(windowSelector, alertMessage);
      }
    } else {
      JOptionPane.showMessageDialog(
          this,
          "Wrong login and/or password, please try again",
          "Error",
          JOptionPane.ERROR_MESSAGE); // Affichage d'un message d'erreur en cas d'échec
    }
  }

  // Méthode principale pour tester la classe
  public void initLoginFrame() {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            new LoginFrame()
                .setVisible(true); // Création d'une instance de LoginFrame et rendu visible
          }
        });
  }
}
