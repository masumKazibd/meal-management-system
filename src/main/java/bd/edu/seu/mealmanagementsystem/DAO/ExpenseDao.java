package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDao {

    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expenses(mess_id, expense_type, amount, expense_date, description, added_by) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expense.getMessId());
            pstmt.setString(2, expense.getExpenseType());
            pstmt.setBigDecimal(3, expense.getAmount());
            pstmt.setDate(4, Date.valueOf(expense.getExpenseDate()));
            pstmt.setString(5, expense.getDescription());
            pstmt.setInt(6, expense.getAddedBy());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all expenses for a given mess in a specific month.
     * @param messId The ID of the mess.
     * @param year The year.
     * @param month The month (1-12).
     * @return A list of expenses.
     */
    public List<Expense> getExpensesForMonth(int messId, int year, int month) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE mess_id = ? AND YEAR(expense_date) = ? AND MONTH(expense_date) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Expense expense = new Expense();
                expense.setExpenseId(rs.getInt("expense_id"));
                expense.setMessId(rs.getInt("mess_id"));
                expense.setExpenseType(rs.getString("expense_type"));
                expense.setAmount(rs.getBigDecimal("amount"));
                expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
                expense.setDescription(rs.getString("description"));
                expense.setAddedBy(rs.getInt("added_by"));
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
}