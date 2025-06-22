package bd.edu.seu.mealmanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

import bd.edu.seu.mealmanagementsystem.Model.User;


public class DashboardController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button addExpenseButton;
    @FXML
    private AnchorPane contentArea;

    private User currentUser;

    private final String FXML_BASE_PATH = "/bd/edu/seu/mealmanagementsystem/";

    public void initData(User user) {
        currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName());

        if (user.getRole() == User.Role.ADMIN) {
            addExpenseButton.setVisible(true);
        }

        handleDashboardHomeButton(null);
    }

    @FXML
    private void handleDashboardHomeButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "DashboardHomeView.fxml");
    }

    @FXML
    private void handleMealPlanButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "MealPlanView.fxml");
    }

    @FXML
    private void handleMealAttendanceButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "MealAttendanceView.fxml");
    }

    @FXML
    private void handleExpensesButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "ExpensesView.fxml");
    }

    @FXML
    private void handlePaymentsButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "PaymentsView.fxml");
    }

    @FXML
    private void handleProfileButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "ProfileView.fxml");
    }

    @FXML
    private void handleAddExpenseButton(ActionEvent event) {
        loadView(FXML_BASE_PATH + "AddExpenseView.fxml");
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent loginRoot = FXMLLoader.load(getClass().getResource(FXML_BASE_PATH + "Login.fxml"));
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("Meal Management System - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            URL resourceUrl = getClass().getResource(fxmlPath);
            if (resourceUrl == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Node view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof AddExpenseController) {
                ((AddExpenseController) controller).initData(currentUser);
            } else if (controller instanceof DashboardHomeController) {
                ((DashboardHomeController) controller).initData(currentUser);
            } else if (controller instanceof MealPlanController) {
                ((MealPlanController) controller).initData(currentUser);
            }

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error: Could not load view '" + fxmlPath + "'.\n" + e.getMessage());
            contentArea.getChildren().setAll(errorLabel);
        }
    }
}