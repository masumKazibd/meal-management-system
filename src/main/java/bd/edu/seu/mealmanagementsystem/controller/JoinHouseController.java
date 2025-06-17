package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.Model.House;
import bd.edu.seu.mealmanagementsystem.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinHouseController {
    @FXML private ComboBox<House> houseComboBox;

    private List<House> houses = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance();
            ResultSet rs = conn.createStatement().executeQuery("SELECT house_id, house_name, gender_type FROM houses");
            while (rs.next()) {
                House house = new House(
                        rs.getInt("house_id"),
                        rs.getString("house_name"),
                        rs.getString("gender_type")
                );
                houses.add(house);
            }
            houseComboBox.getItems().addAll(houses);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleJoinHouse() {
        House selectedHouse = houseComboBox.getValue();
        if (selectedHouse == null) {
            showAlert("Please select a house.");
            return;
        }

        int houseId = selectedHouse.getHouseId();
        String gender = selectedHouse.getGenderType();

        // Pass houseId and gender to RegisterController
        // Your existing code to load register.fxml and setHouseInfo()
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
