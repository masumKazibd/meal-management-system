package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.ExpenseDao;
import bd.edu.seu.mealmanagementsystem.DAO.MealAttendanceDao;
import bd.edu.seu.mealmanagementsystem.DAO.PaymentDao;
import bd.edu.seu.mealmanagementsystem.DAO.UserDao;
import bd.edu.seu.mealmanagementsystem.Model.PaymentSummary;
import bd.edu.seu.mealmanagementsystem.Model.User;
import bd.edu.seu.mealmanagementsystem.Model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PaymentsController {

    @FXML private Label monthLabel;
    @FXML private TableView<PaymentSummary> paymentsTable;
    @FXML private TableColumn<PaymentSummary, String> memberNameColumn;
    @FXML private TableColumn<PaymentSummary, BigDecimal> totalDueColumn;
    @FXML private TableColumn<PaymentSummary, BigDecimal> amountPaidColumn;
    @FXML private TableColumn<PaymentSummary, BigDecimal> balanceColumn;
    @FXML private Label totalDueSummaryLabel;
    @FXML private Label totalPaidSummaryLabel;
    @FXML private Label totalBalanceSummaryLabel;

    private User currentUser;
    private UserDao userDao;
    private ExpenseDao expenseDao;
    private MealAttendanceDao mealAttendanceDao;
    private PaymentDao paymentDao;

    public void initialize() {
        // DAOs
        this.userDao = new UserDao();
        this.expenseDao = new ExpenseDao();
        this.mealAttendanceDao = new MealAttendanceDao();
        this.paymentDao = new PaymentDao();

        // Table Columns
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        totalDueColumn.setCellValueFactory(new PropertyValueFactory<>("totalDue"));
        amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    public void initData(User user) {
        this.currentUser = user;
        loadPaymentSummaries();
    }

    private void loadPaymentSummaries() {
        if (currentUser == null) return;

        LocalDate today = LocalDate.now();
        monthLabel.setText("For " + today.format(DateTimeFormatter.ofPattern("MMMM, yyyy")));

        List<User> members = userDao.getUsersByMessId(currentUser.getMessId());
        if (members.isEmpty()) {
            System.out.println("No members found in this mess."); // Debug
            return;
        }

        // --- Start of Main Debug Section ---
        System.out.println("\n--- Calculating Payment Summaries for " + members.size() + " Members ---");

        // Calculate total shared expenses for the month
        List<Expense> monthlyExpenses = expenseDao.getExpensesForMonth(currentUser.getMessId(), today.getYear(), today.getMonthValue());
        BigDecimal totalSharedExpenses = monthlyExpenses.stream()
                .map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total Shared Expenses for Month: " + totalSharedExpenses); // Debug

        // Calculate each member's share of the expenses
        BigDecimal perPersonExpenseShare = totalSharedExpenses.divide(new BigDecimal(members.size()), 2, RoundingMode.HALF_UP);
        System.out.println("Per Person Expense Share: " + perPersonExpenseShare); // Debug

        List<PaymentSummary> summaries = new ArrayList<>();
        for (User member : members) {
            System.out.println("\nProcessing member: " + member.getFullName()); // Debug

            // Calculate this member's total meal cost
            BigDecimal userMealCost = mealAttendanceDao.getTotalMealCostForUser(member.getUserId(), today.getYear(), today.getMonthValue());
            System.out.println("...Meal Cost: " + userMealCost); // <-- IMPORTANT DEBUG LINE

            // Calculate total due
            BigDecimal totalDue = userMealCost.add(perPersonExpenseShare);
            System.out.println("...Total Due: " + totalDue); // Debug

            // Get amount paid
            BigDecimal amountPaid = paymentDao.getTotalPaidByUser(member.getUserId(), today.getYear(), today.getMonthValue());
            System.out.println("...Amount Paid: " + amountPaid); // Debug

            // Calculate balance
            BigDecimal balance = totalDue.subtract(amountPaid);
            System.out.println("...Balance: " + balance); // Debug

            summaries.add(new PaymentSummary(member.getFullName(), totalDue, amountPaid, balance));
        }

        // --- UPDATE UI ---
        ObservableList<PaymentSummary> observableSummaries = FXCollections.observableArrayList(summaries);
        paymentsTable.setItems(observableSummaries);

        updateTotalSummary(summaries);
    }

    private void updateTotalSummary(List<PaymentSummary> summaries) {
        BigDecimal totalDue = summaries.stream().map(PaymentSummary::getTotalDue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = summaries.stream().map(PaymentSummary::getAmountPaid).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalBalance = summaries.stream().map(PaymentSummary::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

        totalDueSummaryLabel.setText("Total Due: ৳" + totalDue);
        totalPaidSummaryLabel.setText("Total Paid: ৳" + totalPaid);
        totalBalanceSummaryLabel.setText("Total Balance: ৳" + totalBalance);
    }
}