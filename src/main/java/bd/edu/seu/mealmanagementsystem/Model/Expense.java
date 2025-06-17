package bd.edu.seu.mealmanagementsystem.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int expenseId;
    private int messId;
    private String expenseType;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String description;
    private int addedBy;

    // Constructors, Getters, and Setters
    public Expense() {}

    public int getExpenseId() { return expenseId; }
    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }
    public int getMessId() { return messId; }
    public void setMessId(int messId) { this.messId = messId; }
    public String getExpenseType() { return expenseType; }
    public void setExpenseType(String expenseType) { this.expenseType = expenseType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getAddedBy() { return addedBy; }
    public void setAddedBy(int addedBy) { this.addedBy = addedBy; }
}