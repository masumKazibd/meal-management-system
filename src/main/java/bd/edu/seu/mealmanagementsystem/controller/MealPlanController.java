package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.MealDao;
import bd.edu.seu.mealmanagementsystem.Model.Meal;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

public class MealPlanController {

    @FXML private Label weekDateRangeLabel;
    @FXML private GridPane mealGridPane;
    @FXML private Button addMealButton;

    private MealDao mealDao;
    private User currentUser;
    private LocalDate currentWeekStartDate;

    public void initialize() {
        this.currentWeekStartDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
        this.mealDao = new MealDao();

    }

    public void initData(User user) {
        this.currentUser = user;
        if (user.getRole() == User.Role.ADMIN) {
            addMealButton.setVisible(true);
        }
        updateMealPlanView();
    }

    @FXML
    void handlePreviousWeekButton(ActionEvent event) {
        currentWeekStartDate = currentWeekStartDate.minusWeeks(1);
        updateMealPlanView();
    }

    @FXML
    void handleNextWeekButton(ActionEvent event) {
        currentWeekStartDate = currentWeekStartDate.plusWeeks(1);
        updateMealPlanView();
    }

    private void updateMealPlanView() {
        LocalDate weekEndDate = currentWeekStartDate.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        weekDateRangeLabel.setText(currentWeekStartDate.format(formatter) + " - " + weekEndDate.format(formatter));
        mealGridPane.getChildren().clear();

        // Fetch all meals for the current week
        List<Meal> weeklyMeals = mealDao.getMealsBetweenDates(currentUser.getMessId(), currentWeekStartDate, weekEndDate);

        // --- DEBUG LINE 1 ---
        System.out.println("--- Found " + weeklyMeals.size() + " total meals for the week. ---");

        // Create a card for each day
        for (int i = 0; i < 7; i++) {
            try {
                final LocalDate dateForCard = currentWeekStartDate.plusDays(i);

                // Filter the list for this specific day
                List<Meal> mealsForThisDay = weeklyMeals.stream()
                        .filter(meal -> meal.getMealDate().isEqual(dateForCard))
                        .collect(Collectors.toList());

                // --- DEBUG LINE 2 ---
                System.out.println("For " + dateForCard + ", found " + mealsForThisDay.size() + " meals.");

                URL resourceUrl = getClass().getResource("/bd/edu/seu/mealmanagementsystem/DayCard.fxml");
                FXMLLoader loader = new FXMLLoader(resourceUrl);
                VBox dayCard = loader.load();

                DayCardController controller = loader.getController();

                // --- DEBUG LINE 3 ---
                if (controller == null) {
                    System.out.println("FATAL ERROR: DayCardController is NULL for " + dateForCard + ". Check fx:controller in DayCard.fxml.");
                } else {
                    controller.setData(dateForCard, mealsForThisDay);
                }

                mealGridPane.add(dayCard, i, 0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddMealButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bd/edu/seu/mealmanagementsystem/AddEditMealView.fxml"));
            Parent root = loader.load();

            AddEditMealController controller = loader.getController();
            controller.initData(currentUser.getMessId(), this::updateMealPlanView);

            Stage stage = new Stage();
            stage.setTitle("Add/Edit Meal");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
