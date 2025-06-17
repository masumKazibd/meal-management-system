package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.Meal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MealDao {

    public boolean addMeal(Meal meal) {
        String sql = "INSERT INTO meals(mess_id, meal_date, meal_type, cost_per_meal) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, meal.getMessId());
            pstmt.setDate(2, Date.valueOf(meal.getMealDate()));
            pstmt.setString(3, meal.getMealType().name().toLowerCase());
            pstmt.setBigDecimal(4, meal.getCostPerMeal());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add other methods like getMealsForWeek, updateMealCost etc.
}

