package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.ExpenseDao;
import bd.edu.seu.mealmanagementsystem.DAO.MessDao;
import bd.edu.seu.mealmanagementsystem.DAO.UserDao;
import bd.edu.seu.mealmanagementsystem.Model.Expense;
import bd.edu.seu.mealmanagementsystem.Model.Mess;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DashboardHomeController {

    @FXML private Label totalMembersLabel;
    @FXML private Label totalExpensesLabel;
    @FXML private Label userMealsLabel;
    @FXML private Label messNameLabel;

    private UserDao userDao;
    private ExpenseDao expenseDao;
    private MessDao messDao;
    private User currentUser;

    public void initialize() {
        this.userDao = new UserDao();
        this.expenseDao = new ExpenseDao();
        this.messDao = new MessDao();
    }

    public void initData(User user) {
        System.out.println("<<<<< DashboardHomeController's initData was called! >>>>>");
        this.currentUser = user;
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        System.out.println("--- Loading Dashboard Stats ---");

        if (currentUser == null) {
            System.out.println("ERROR: Current user is NULL!");
            return;
        }
        if (currentUser.getMessId() == null) {
            System.out.println("ERROR: Current user's Mess ID is NULL!");
            return;
        }

        int messId = currentUser.getMessId();
        System.out.println("Fetching data for Mess ID: " + messId);
        // --- End of Debugging ---


        // Load Mess Name
        Mess currentMess = messDao.getMessById(messId);
        if (currentMess != null) {
            System.out.println("Found Mess Name: " + currentMess.getMessName()); // <-- Debug
            messNameLabel.setText("Mess: " + currentMess.getMessName());
        } else {
            System.out.println("WARNING: Mess not found for ID: " + messId); // <-- Debug
        }

        // 1. Get Total Members
        List<User> members = userDao.getUsersByMessId(messId);
        System.out.println("Found " + members.size() + " members in the mess."); // <-- Debug
        totalMembersLabel.setText(String.valueOf(members.size()));

        // 2. Get Total Expenses for the Current Month
        LocalDate today = LocalDate.now();
        List<Expense> monthlyExpenses = expenseDao.getExpensesForMonth(messId, today.getYear(), today.getMonthValue());
        System.out.println("Found " + monthlyExpenses.size() + " expenses this month."); // <-- Debug

        BigDecimal totalExpenseAmount = BigDecimal.ZERO;
        for (Expense expense : monthlyExpenses) {
            totalExpenseAmount = totalExpenseAmount.add(expense.getAmount());
        }
        System.out.println("Total expense amount: " + totalExpenseAmount); // <-- Debug
        totalExpensesLabel.setText("৳" + totalExpenseAmount.toPlainString());

        userMealsLabel.setText("TBD");
    }
}
