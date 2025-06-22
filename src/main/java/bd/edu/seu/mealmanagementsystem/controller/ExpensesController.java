package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.ExpenseDao;
import bd.edu.seu.mealmanagementsystem.DAO.UserDao;
import bd.edu.seu.mealmanagementsystem.Model.Expense;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpensesController {

    @FXML private Label monthLabel;
    @FXML private TableView<Expense> expensesTable;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, String> typeColumn;
    @FXML private TableColumn<Expense, BigDecimal> amountColumn;
    @FXML private TableColumn<Expense, String> descriptionColumn;
    @FXML private TableColumn<Expense, Integer> addedByColumn;
    @FXML private Label totalExpensesLabel;

    private User currentUser;
    private ExpenseDao expenseDao;
    private UserDao userDao;

    public void initialize() {
        this.expenseDao = new ExpenseDao();
        this.userDao = new UserDao();

        // Bind table columns to the Expense model's properties
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("expenseType"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        addedByColumn.setCellValueFactory(new PropertyValueFactory<>("addedBy"));
    }

    public void initData(User user) {
        this.currentUser = user;
        loadMonthlyExpenses();
    }

    private void loadMonthlyExpenses() {
        if (currentUser == null || currentUser.getMessId() == null) return;

        LocalDate today = LocalDate.now();
        // FIX: The date formatting pattern was invalid ("UCI"). It has been corrected to "yyyy".
        monthLabel.setText("For " + today.format(DateTimeFormatter.ofPattern("MMMM, yyyy")));

        List<Expense> expenses = expenseDao.getExpensesForMonth(currentUser.getMessId(), today.getYear(), today.getMonthValue());

        // --- DEBUG LINE ---
        System.out.println("Found " + expenses.size() + " expenses for the month.");

        // This part converts the user ID in "addedBy" to the user's actual name
        customizeAddedByColumn(expenses);

        ObservableList<Expense> observableExpenses = FXCollections.observableArrayList(expenses);
        expensesTable.setItems(observableExpenses);

        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalExpensesLabel.setText("Total Expenses: ৳" + total.toPlainString());
    }

    /**
     * Customizes the "Added By" column to show user names instead of user IDs.
     */
    private void customizeAddedByColumn(List<Expense> expenses) {
        // Get all unique user IDs from the expenses list
        List<Integer> userIds = expenses.stream()
                .map(Expense::getAddedBy)
                .distinct()
                .collect(Collectors.toList());

        // Fetch user details for these IDs in one go
        if (!userIds.isEmpty()) {
            Map<Integer, String> userIdToNameMap = userDao.getUserNamesByIds(userIds);

            addedByColumn.setCellFactory(column -> new TableCell<Expense, Integer>() {
                @Override
                protected void updateItem(Integer userId, boolean empty) {
                    super.updateItem(userId, empty);
                    if (empty || userId == null) {
                        setText(null);
                    } else {
                        // Use the map to find the name, with a fallback
                        setText(userIdToNameMap.getOrDefault(userId, "Unknown User"));
                    }
                }
            });
        }
    }
}