package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.UserDao;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

public class ProfileController {

    @FXML private Label initialsLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private User currentUser;
    private UserDao userDao;

    public void initialize() {
        this.userDao = new UserDao();
    }

    /**
     * Receives the logged-in user's data and populates the profile fields.
     */
    public void initData(User user) {
        this.currentUser = user;

        // Populate the user information labels
        fullNameLabel.setText(user.getFullName());
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

        // Create and set the initials for the avatar
        setAvatarInitials(user.getFullName());
    }

    /**
     * Handles the action for the "Update Password" button.
     */
    @FXML
    private void handleUpdatePasswordButton(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // --- Validation ---
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Password fields cannot be empty.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Passwords do not match.");
            return;
        }

        // --- Update Password in Database ---
        // Note: In a real application, you MUST hash the password before saving.
        boolean success = userDao.updateUserPassword(currentUser.getUserId(), newPassword);

        if (success) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Password updated successfully!");
            // Clear the password fields after a successful update
            newPasswordField.clear();
            confirmPasswordField.clear();
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to update password.");
        }
    }

    /**
     * A helper method to generate and display initials from a full name.
     * @param fullName The user's full name.
     */
    private void setAvatarInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            initialsLabel.setText("");
            return;
        }

        String[] nameParts = fullName.trim().split("\\s+");
        String initials = "";

        if (nameParts.length > 0) {
            initials += nameParts[0].charAt(0);
        }
        if (nameParts.length > 1) {
            initials += nameParts[nameParts.length - 1].charAt(0);
        }

        initialsLabel.setText(initials.toUpperCase());
    }
}