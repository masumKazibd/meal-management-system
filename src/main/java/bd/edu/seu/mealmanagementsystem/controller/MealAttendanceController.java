package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.MealAttendanceDao;
import bd.edu.seu.mealmanagementsystem.DAO.MealDao;
import bd.edu.seu.mealmanagementsystem.Model.Meal;
import bd.edu.seu.mealmanagementsystem.Model.MealAttendance;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MealAttendanceController {

    @FXML private DatePicker attendanceDatePicker;
    @FXML private CheckBox breakfastCheckbox;
    @FXML private CheckBox lunchCheckbox;
    @FXML private CheckBox dinnerCheckbox;
    @FXML private Label messageLabel;

    private User currentUser;
    private MealDao mealDao;
    private MealAttendanceDao mealAttendanceDao;

    private Map<Meal.MealType, Meal> availableMealsForDay;

    public void initialize() {
        this.mealDao = new MealDao();
        this.mealAttendanceDao = new MealAttendanceDao();

        // Set the initial date and add a listener to react to date changes
        attendanceDatePicker.setValue(LocalDate.now());
        attendanceDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadAttendanceForDate(newDate);
            }
        });
    }

    public void initData(User user) {
        this.currentUser = user;
        // Initial load for today's date
        loadAttendanceForDate(LocalDate.now());
    }

    private void loadAttendanceForDate(LocalDate date) {
        if (currentUser == null) return;

        messageLabel.setText("");
        resetCheckboxes();

        // 1. Get all available meals for the selected date
        List<Meal> mealsOnThisDate = mealDao.getMealsBetweenDates(currentUser.getMessId(), date, date);
        availableMealsForDay = mealsOnThisDate.stream()
                .collect(Collectors.toMap(Meal::getMealType, Function.identity()));

        // 2. Enable checkboxes only for available meals
        breakfastCheckbox.setDisable(!availableMealsForDay.containsKey(Meal.MealType.BREAKFAST));
        lunchCheckbox.setDisable(!availableMealsForDay.containsKey(Meal.MealType.LUNCH));
        dinnerCheckbox.setDisable(!availableMealsForDay.containsKey(Meal.MealType.DINNER));

        // 3. Get the user's current attendance status for these meals
        if (!mealsOnThisDate.isEmpty()) {
            List<Integer> mealIds = mealsOnThisDate.stream().map(Meal::getMealId).collect(Collectors.toList());
            List<MealAttendance> userAttendances = mealAttendanceDao.getAttendanceForUserAndMeals(currentUser.getUserId(), mealIds);

            // 4. Check the boxes based on the user's attendance
            for (MealAttendance attendance : userAttendances) {
                if (attendance.isPresent()) {
                    Meal meal = mealsOnThisDate.stream().filter(m -> m.getMealId() == attendance.getMealId()).findFirst().orElse(null);
                    if (meal != null) {
                        switch (meal.getMealType()) {
                            case BREAKFAST: breakfastCheckbox.setSelected(true); break;
                            case LUNCH: lunchCheckbox.setSelected(true); break;
                            case DINNER: dinnerCheckbox.setSelected(true); break;
                        }
                    }
                }
            }
        }
    }

    @FXML
    void handleSaveAttendanceButton(ActionEvent event) {
        List<MealAttendance> attendancesToSave = new ArrayList<>();

        // Create a MealAttendance object for each available meal
        if(availableMealsForDay.containsKey(Meal.MealType.BREAKFAST)){
            attendancesToSave.add(new MealAttendance(
                    availableMealsForDay.get(Meal.MealType.BREAKFAST).getMealId(),
                    currentUser.getUserId(),
                    breakfastCheckbox.isSelected()
            ));
        }
        if(availableMealsForDay.containsKey(Meal.MealType.LUNCH)){
            attendancesToSave.add(new MealAttendance(
                    availableMealsForDay.get(Meal.MealType.LUNCH).getMealId(),
                    currentUser.getUserId(),
                    lunchCheckbox.isSelected()
            ));
        }
        if(availableMealsForDay.containsKey(Meal.MealType.DINNER)){
            attendancesToSave.add(new MealAttendance(
                    availableMealsForDay.get(Meal.MealType.DINNER).getMealId(),
                    currentUser.getUserId(),
                    dinnerCheckbox.isSelected()
            ));
        }

        boolean allSuccess = true;
        for (MealAttendance attendance : attendancesToSave) {
            if (!mealAttendanceDao.markAttendance(attendance)) {
                allSuccess = false;
            }
        }

        if (allSuccess) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Attendance saved successfully!");
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to save one or more choices.");
        }
    }

    private void resetCheckboxes() {
        breakfastCheckbox.setSelected(false);
        lunchCheckbox.setSelected(false);
        dinnerCheckbox.setSelected(false);
        breakfastCheckbox.setDisable(true);
        lunchCheckbox.setDisable(true);
        dinnerCheckbox.setDisable(true);
    }
}