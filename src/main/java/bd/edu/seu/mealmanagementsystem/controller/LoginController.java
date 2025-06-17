package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.UserDao;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorMessageLabel;

    private UserDao userDao;

    public LoginController() {
        userDao = new UserDao();
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Username and password cannot be empty.");
            return;
        }

        // In a real application, you'd use a password hashing library like BCrypt.
        // For this project, we'll do a direct comparison.
        User user = userDao.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // Login successful
            errorMessageLabel.setText("Login successful!");
            // TODO: Navigate to the main application dashboard
            // For now, we'll just close the login window.

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close(); // Close the login window

            // Here you would typically load the main dashboard
            System.out.println("User " + user.getUsername() + " logged in successfully as " + user.getRole());

        } else {
            // Login failed
            errorMessageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    protected void handleRegisterLinkAction(ActionEvent event) {
        try {
            // Load the registration screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("Register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(registerRoot);
            stage.setScene(scene);
            stage.setTitle("Meal Management System - Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessageLabel.setText("Error: Could not load registration page.");
        }
    }
}