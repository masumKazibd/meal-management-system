package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.Meal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


    public List<Meal> getMealsBetweenDates(int messId, LocalDate startDate, LocalDate endDate) {
        List<Meal> meals = new ArrayList<>();

        // THE FIX IS HERE: The column name must be 'mess_id' to match the database table.
        String sql = "SELECT * FROM meals WHERE mess_id = ? AND meal_date BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Meal meal = new Meal();
                meal.setMealId(rs.getInt("meal_id"));
                meal.setMessId(rs.getInt("mess_id"));
                meal.setMealDate(rs.getDate("meal_date").toLocalDate());
                meal.setMealType(Meal.MealType.valueOf(rs.getString("meal_type").toUpperCase()));
                meal.setCostPerMeal(rs.getBigDecimal("cost_per_meal"));
                meals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }


    public boolean addOrUpdateMeal(Meal meal) {
        String sql = "INSERT INTO meals (mess_id, meal_date, meal_type, cost_per_meal) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cost_per_meal = VALUES(cost_per_meal)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, meal.getMessId());
            pstmt.setDate(2, java.sql.Date.valueOf(meal.getMealDate()));
            pstmt.setString(3, meal.getMealType().name());
            pstmt.setBigDecimal(4, meal.getCostPerMeal());

            return pstmt.executeUpdate() >= 1; // Returns 1 for insert, 2 for update
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

