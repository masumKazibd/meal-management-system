package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.MealDao;
import bd.edu.seu.mealmanagementsystem.Model.Meal;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AddEditMealController {

    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<Meal.MealType> mealTypeChoiceBox;
    @FXML private TextField costField;
    @FXML private Label messageLabel;

    private MealDao mealDao;
    private int messId;
    private Runnable onSaveCallback; // A callback to refresh the meal plan view

    public void initialize() {
        mealDao = new MealDao();
        mealTypeChoiceBox.setItems(FXCollections.observableArrayList(Meal.MealType.values()));
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Initializes the dialog with necessary data.
     * @param messId The ID of the current mess.
     * @param callback A function to run after saving, which will refresh the parent view.
     */
    public void initData(int messId, Runnable callback) {
        this.messId = messId;
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        // --- Validation ---
        if (datePicker.getValue() == null || mealTypeChoiceBox.getValue() == null || costField.getText().isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("All fields are required.");
            return;
        }

        BigDecimal cost;
        try {
            cost = new BigDecimal(costField.getText());
            if (cost.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Please enter a valid positive cost.");
            return;
        }

        // --- Create and Save Meal ---
        Meal meal = new Meal();
        meal.setMessId(messId);
        meal.setMealDate(datePicker.getValue());
        meal.setMealType(mealTypeChoiceBox.getValue());
        meal.setCostPerMeal(cost);

        // MealDao needs an 'addOrUpdateMeal' method
        boolean success = mealDao.addOrUpdateMeal(meal);

        if (success) {
            // Run the callback to refresh the meal plan
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            // Close the pop-up window
            ((Stage) messageLabel.getScene().getWindow()).close();
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to save meal. It may already exist.");
        }
    }
}
