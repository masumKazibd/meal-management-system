package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.MealAttendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealAttendanceDao {

    public boolean markAttendance(MealAttendance attendance) {
        String sql = "INSERT INTO meal_attendance(meal_id, user_id, is_present) VALUES(?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE is_present = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, attendance.getMealId());
            pstmt.setInt(2, attendance.getUserId());
            pstmt.setBoolean(3, attendance.isPresent());
            pstmt.setBoolean(4, attendance.isPresent()); // For the ON DUPLICATE KEY UPDATE part

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<MealAttendance> getAttendanceForUserAndMeals(int userId, List<Integer> mealIds) {
        List<MealAttendance> attendances = new ArrayList<>();
        if (mealIds == null || mealIds.isEmpty()) {
            return attendances;
        }

        // Creates a string of placeholders like "?, ?, ?"
        String placeholders = String.join(",", java.util.Collections.nCopies(mealIds.size(), "?"));
        String sql = String.format("SELECT * FROM meal_attendance WHERE user_id = ? AND meal_id IN (%s)", placeholders);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int index = 2;
            for (Integer mealId : mealIds) {
                pstmt.setInt(index++, mealId);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MealAttendance attendance = new MealAttendance();
                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setMealId(rs.getInt("meal_id"));
                attendance.setUserId(rs.getInt("user_id"));
                attendance.setPresent(rs.getBoolean("is_present"));
                attendances.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }
}
