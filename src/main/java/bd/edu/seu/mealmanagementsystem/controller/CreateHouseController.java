package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.HelloApplication;
import bd.edu.seu.mealmanagementsystem.Model.House;
import bd.edu.seu.mealmanagementsystem.DAO.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.*;

public class CreateHouseController {
    @FXML private TextField houseNameField;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderToggleGroup;

    @FXML
    public void handleCreateHouse() {
        String houseName = houseNameField.getText().trim();
        RadioButton radioButton = (RadioButton) genderToggleGroup.getSelectedToggle();
        String gender = radioButton.getText();

        if (houseName.isEmpty() || genderToggleGroup.getSelectedToggle()  == null) {
            showAlert(Alert.AlertType.ERROR, "Please enter house name and select gender.");
            return;
        }


        try (Connection conn = DatabaseConnection.getInstance()) {
            // Check for duplicate house name
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM houses WHERE house_name = ?");
            checkStmt.setString(1, houseName);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                showAlert(Alert.AlertType.ERROR, "House name already exists. Please choose another name.");
                return;
            }

            // Insert house
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO houses (house_name, gender_type) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, houseName);
            insertStmt.setString(2, gender);
            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating house failed, no rows affected.");
            }

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int houseId = generatedKeys.getInt(1);
                House house = new House(houseId, houseName, gender);

                // Load register.fxml and pass house + role = admin
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bd/edu/seu/mealmanagementsystem/register.fxml"));
                Parent root = loader.load();


                RegisterController registerController = loader.getController();
                registerController.setHouseInfo(house, "admin");

                Stage stage = (Stage) houseNameField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                throw new SQLException("Creating house failed, no ID obtained.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
        HelloApplication.changeScene("register");
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.show();
    }
}
