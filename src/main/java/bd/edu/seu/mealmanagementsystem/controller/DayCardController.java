package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.Model.Meal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DayCardController {

    @FXML private Label dayNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label breakfastLabel;
    @FXML private Label lunchLabel;
    @FXML private Label dinnerLabel;


    public void setData(LocalDate date, List<Meal> mealsForDay) {
        String dayName = date.format(DateTimeFormatter.ofPattern("EEEE"));
        String dateStr = date.format(DateTimeFormatter.ofPattern("dd MMM"));

        // --- DEBUG LINE 4 ---
        System.out.println("DayCardController: setData called for " + date);

        if (dayNameLabel == null || dateLabel == null) {
            System.out.println("FATAL ERROR: Labels in DayCardController are NULL. Check fx:id in DayCard.fxml.");
            return;
        }

        dayNameLabel.setText(dayName);
        dateLabel.setText(dateStr);

        resetLabelsToDefault();

        if (mealsForDay != null && !mealsForDay.isEmpty()) {
            System.out.println("Populating " + mealsForDay.size() + " meals for " + date); // DEBUG
            for (Meal meal : mealsForDay) {
                String mealInfo = "৳" + meal.getCostPerMeal().toPlainString();

                switch (meal.getMealType()) {
                    case BREAKFAST:
                        setLabelWithMealData(breakfastLabel, mealInfo);
                        break;
                    case LUNCH:
                        setLabelWithMealData(lunchLabel, mealInfo);
                        break;
                    case DINNER:
                        setLabelWithMealData(dinnerLabel, mealInfo);
                        break;
                }
            }
        }
    }

//     * A helper method to style a label once it has meal data.
    private void setLabelWithMealData(Label label, String text) {
        label.setText(text);
        label.setStyle("-fx-font-style: normal; -fx-font-weight: bold; -fx-text-fill: #007BFF;");
    }

//     * A helper method to reset all meal labels to their default "-- Not Set --" state.
    private void resetLabelsToDefault() {
        String defaultStyle = "-fx-font-style: italic; -fx-text-fill: #555555;";

        breakfastLabel.setText("-- Not Set --");
        breakfastLabel.setStyle(defaultStyle);

        lunchLabel.setText("-- Not Set --");
        lunchLabel.setStyle(defaultStyle);

        dinnerLabel.setText("-- Not Set --");
        dinnerLabel.setStyle(defaultStyle);
    }
}