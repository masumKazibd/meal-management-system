package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.DAO.DatabaseConnection;
import bd.edu.seu.mealmanagementsystem.Model.Payment;

import java.math.BigDecimal;
import java.sql.*;

public class PaymentDao {


    public BigDecimal getTotalPaidByUser(int userId, int year, int month) {
        // Format the month string to match the 'YYYY-MM' format in the database
        String monthStr = String.format("%d-%02d", year, month);
        String sql = "SELECT SUM(amount) AS total_paid FROM payments WHERE user_id = ? AND payment_for_month = ?";

        try (Connection conn = bd.edu.seu.mealmanagementsystem.DAO.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, monthStr);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                BigDecimal totalPaid = rs.getBigDecimal("total_paid");
                // If the SUM is NULL (no payments found), return ZERO.
                return totalPaid == null ? BigDecimal.ZERO : totalPaid;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments(user_id, mess_id, amount, payment_date, payment_for_month, is_paid) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, payment.getUserId());
            pstmt.setInt(2, payment.getMessId());
            pstmt.setBigDecimal(3, payment.getAmount());
            pstmt.setDate(4, Date.valueOf(payment.getPaymentDate()));
            pstmt.setString(5, payment.getPaymentForMonth());
            pstmt.setBoolean(6, payment.isPaid());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
