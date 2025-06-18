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

        User user = userDao.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bd/edu/seu/mealmanagementsystem/Dashboard.fxml"));
                Parent dashboardRoot = loader.load();

                // Get the controller instance from the loader
                DashboardController dashboardController = loader.getController();

                // Call a method on the dashboard controller to pass the logged-in user's data
                dashboardController.initData(user);

                // Create a new scene with the dashboard view
                Scene scene = new Scene(dashboardRoot);

                // Set the new scene on the stage and show it
                stage.setScene(scene);
                stage.setTitle("Meal Management Dashboard");
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                errorMessageLabel.setText("Error: Failed to load the dashboard.");
            }
        } else {
            // --- LOGIN FAILED ---
            errorMessageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    protected void handleRegisterLinkAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bd/edu/seu/mealmanagementsystem/register.fxml"));
            Parent registerRoot = loader.load();
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