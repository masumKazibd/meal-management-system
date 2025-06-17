package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.MealAttendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MealAttendanceDao {

    public boolean markAttendance(MealAttendance attendance) {
        // This query either inserts a new attendance record or updates an existing one
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
}
