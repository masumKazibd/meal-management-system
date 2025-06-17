/*package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.Model.House;
import bd.edu.seu.mealmanagementsystem.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderGroup;
    // Other fields...

    private House house;
    private String role;

    public void setHouseInfo(House house, String role) {
        this.house = house;
        this.role = role;
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String userGender = maleRadio.isSelected() ? "male" : femaleRadio.isSelected() ? "female" : "";

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phone.isEmpty() || dob == null || userGender.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
            return;
        }

        // Age validation (>= 16)
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 16) {
            showAlert(Alert.AlertType.ERROR, "You must be at least 16 years old.");
            return;
        }

        // Gender check against house
        /*if (!userGender.equalsIgnoreCase(house.getGenderType())) {
            showAlert(Alert.AlertType.ERROR, "User gender does not match house gender.");
            return;
        }*/

        // TODO: Insert user into DB with house_id and role (admin or member)
        // Example: saveUserToDatabase(...)

       /* showAlert(Alert.AlertType.INFORMATION, "Registration successful as " + role + " of house: " /*+ house.getHouseName()*/

/*import javafx.scene.control.Alert;);
    /

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.show();
    }
     try {
        Connection conn = DatabaseConnection.getInstance();

        // Determine if first user
        String role = "member"; // default
        ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM users");
        if (rs.next() && rs.getInt(1) == 0) {
            role = "admin"; // first user becomes admin
        }

        String sql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password); // (Hash in real apps)
        stmt.setString(3, fullName);
        stmt.setString(4, role);

        stmt.executeUpdate();
        showAlert(Alert.AlertType.INFORMATION, "Registration successful as " + role + "!");

        // Optionally clear the fields
        usernameField.clear();
        passwordField.clear();
        fullNameField.clear();

    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Username already exists or database error");
        e.printStackTrace();
    }
}*/
package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.Model.House;
import bd.edu.seu.mealmanagementsystem.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderGroup;

    private House house;
    private String role;

    public void setHouseInfo(House house, String role) {
        this.house = house;
        this.role = role;
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String gender = maleRadio.isSelected() ? "male" : "female";

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || dob == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
            return;
        }

        // Age validation (>= 16)
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 16) {
            showAlert(Alert.AlertType.ERROR, "You must be at least 16 years old.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getInstance();

            // Check if username exists
            if (usernameExists(conn, username)) {
                showAlert(Alert.AlertType.ERROR, "Username already exists.");
                return;
            }

            // Determine role (first user becomes admin)
            String userRole = isFirstUser(conn) ? "admin" : "member";

            // Insert user
            String sql = "INSERT INTO users (username, password, full_name, role, gender, dob, house_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Should hash in production
                stmt.setString(3, username); // Using username as full_name if no separate field
                stmt.setString(4, userRole);
                stmt.setString(5, gender);
                stmt.setDate(6, Date.valueOf(dob));

                // Handle house_id
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    // ... other parameters ...

                    // Handle house_id (Integer object version)
                    if (house != null) {
                        try {
                            stmt.setInt(7, house.getHouseId());
                        } catch (NullPointerException e) {
                            // If getHouseId() throws NPE, it's likely an Integer
                            stmt.setNull(7, Types.INTEGER);
                        }
                    } else {
                        stmt.setNull(7, Types.INTEGER);
                    }

                    stmt.executeUpdate();
                }

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    showAlert(Alert.AlertType.INFORMATION,
                            "Registration successful as " + userRole +
                                    (house != null ? " of house: " + house.getHouseName() : ""));
                    clearFormFields();
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean usernameExists(Connection conn, String username) throws SQLException {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean isFirstUser(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }

    private void clearFormFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        emailField.clear();
        phoneField.clear();
        dobPicker.setValue(null);
        genderGroup.selectToggle(null);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
