package bd.edu.seu.mealmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Parent;

public class MealManagement extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("Meal Management System - Login");
        primaryStage.setScene(new Scene(root, 450, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}