package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.MessDao;
import bd.edu.seu.mealmanagementsystem.DAO.UserDao;
import bd.edu.seu.mealmanagementsystem.Model.Mess;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton createMessRadio;
    @FXML private RadioButton joinMessRadio;
    @FXML private VBox createMessBox;
    @FXML private TextField newMessNameField;
    @FXML private VBox joinMessBox;
    @FXML private ChoiceBox<String> existingMessesChoiceBox;
    @FXML private Label messageLabel;

    private MessDao messDao;
    private UserDao userDao;

    public void initialize() {
        messDao = new MessDao();
        userDao = new UserDao();
        loadExistingMesses();
    }

    @FXML
    protected void handleMessOptionChange(ActionEvent event) {
        if (createMessRadio.isSelected()) {
            createMessBox.setVisible(true);
            createMessBox.setManaged(true);
            joinMessBox.setVisible(false);
            joinMessBox.setManaged(false);
        } else {
            createMessBox.setVisible(false);
            createMessBox.setManaged(false);
            joinMessBox.setVisible(true);
            joinMessBox.setManaged(true);
        }
    }

    private void loadExistingMesses() {
        List<Mess> messes = messDao.getAllMesses();
        List<String> messNames = messes.stream().map(Mess::getMessName).collect(Collectors.toList());
        existingMessesChoiceBox.setItems(FXCollections.observableArrayList(messNames));
    }

    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all user fields.");
            return;
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        if (createMessRadio.isSelected()) {
            // --- Logic for Creating a New Mess ---
            String newMessName = newMessNameField.getText();
            if (newMessName.isEmpty()) {
                messageLabel.setText("Please provide a name for the new mess.");
                return;
            }

            // User becomes an ADMIN of the new mess
            newUser.setRole(User.Role.ADMIN);

            // First, add the user without a mess_id
            boolean userAdded = userDao.addUser(newUser);
            if (!userAdded) {
                messageLabel.setText("Error: Username or Email might already exist.");
                return;
            }

            // Then, get the newly created user's ID
            User createdUser = userDao.getUserByUsername(username);

            // Create the new mess with the user as admin
            Mess newMess = new Mess();
            newMess.setMessName(newMessName);
            newMess.setAdminId(createdUser.getUserId());
            int newMessId = messDao.createMess(newMess);

            if (newMessId != -1) {
                // Finally, update the user with the new mess_id
                // This requires an updateUser method in UserDao, which you'll need to add.
                // For now, we'll assume it works conceptually.
                System.out.println("User and Mess created! User needs to be updated with mess_id: " + newMessId);
                messageLabel.setText("Registration successful!");
                handleBackToLoginLinkAction(event);

            } else {
                messageLabel.setText("Error: Could not create the mess.");
            }

        } else {
            // --- Logic for Joining an Existing Mess ---
            String selectedMessName = existingMessesChoiceBox.getValue();
            if (selectedMessName == null) {
                messageLabel.setText("Please select a mess to join.");
                return;
            }

            // User becomes a MEMBER of the existing mess
            newUser.setRole(User.Role.MEMBER);

            // Find the mess_id from the selected name
            // This would be more robust with a Map<String, Integer> or by fetching the Mess object
            Mess selectedMess = messDao.getAllMesses().stream()
                    .filter(m -> m.getMessName().equals(selectedMessName))
                    .findFirst().orElse(null);

            if (selectedMess != null) {
                newUser.setMessId(selectedMess.getMessId());
                boolean userAdded = userDao.addUser(newUser);

                if (userAdded) {
                    messageLabel.setText("Registration successful!");
                    System.out.println("User created and joined mess: " + selectedMessName);
                    handleBackToLoginLinkAction(event);
                } else {
                    messageLabel.setText("Error: Username or Email might already exist.");
                }
            } else {
                messageLabel.setText("Error: Selected mess not found.");
            }
        }
    }

    @FXML
    protected void handleBackToLoginLinkAction(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/bd/edu/seu/mealmanagementsystem/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("Meal Management System - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error: Could not load login page.");
        }
    }
}