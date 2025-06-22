package bd.edu.seu.mealmanagementsystem.controller;

import bd.edu.seu.mealmanagementsystem.DAO.ExpenseDao;
import bd.edu.seu.mealmanagementsystem.Model.Expense;
import bd.edu.seu.mealmanagementsystem.Model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AddExpenseController {

    @FXML private ChoiceBox<String> expenseTypeChoiceBox;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private TextArea descriptionArea;
    @FXML private Label messageLabel;

    private User currentUser;
    private ExpenseDao expenseDao;

    public void initialize() {
        expenseDao = new ExpenseDao();
        // Set up the choices for the dropdown
        expenseTypeChoiceBox.setItems(FXCollections.observableArrayList(
                "Grocery", "Rent", "Electricity", "Water Bill", "Gas Bill", "Internet Bill", "Utilities", "Other"
        ));
        // Default the date to today
        datePicker.setValue(LocalDate.now());
    }

    public void initData(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleSaveExpenseButton(ActionEvent event) {
        if (currentUser == null || currentUser.getMessId() == null) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Error: User or Mess information is missing.");
            return;
        }

        String type = expenseTypeChoiceBox.getValue();
        String amountStr = amountField.getText();
        LocalDate date = datePicker.getValue();

        if (type == null || amountStr.isEmpty() || date == null) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Please fill in all required fields.");
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Please enter a valid positive number for the amount.");
            return;
        }

        Expense newExpense = new Expense();
        newExpense.setMessId(currentUser.getMessId());
        newExpense.setExpenseType(type);
        newExpense.setAmount(amount);
        newExpense.setExpenseDate(date);
        newExpense.setDescription(descriptionArea.getText());
        newExpense.setAddedBy(currentUser.getUserId());

        boolean success = expenseDao.addExpense(newExpense);

        if (success) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Expense added successfully!");
            clearForm();
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to save the expense to the database.");
        }
    }

    private void clearForm() {
        expenseTypeChoiceBox.setValue(null);
        amountField.clear();
        descriptionArea.clear();
        datePicker.setValue(LocalDate.now());
    }
}