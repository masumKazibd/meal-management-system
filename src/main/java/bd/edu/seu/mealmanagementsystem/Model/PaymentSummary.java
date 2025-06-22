package bd.edu.seu.mealmanagementsystem.Model;

import java.math.BigDecimal;

/**
 * A "wrapper" class to hold the calculated payment summary data for display in the TableView.
 * This class does NOT represent a database table. Its purpose is to structure the
 * final calculated data for the UI.
 */
public class PaymentSummary {
    private String memberName;
    private BigDecimal totalDue;
    private BigDecimal amountPaid;
    private BigDecimal balance;

    public PaymentSummary(String memberName, BigDecimal totalDue, BigDecimal amountPaid, BigDecimal balance) {
        this.memberName = memberName;
        this.totalDue = totalDue;
        this.amountPaid = amountPaid;
        this.balance = balance;
    }

    // Getters are required for the PropertyValueFactory in the controller to work.
    public String getMemberName() {
        return memberName;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
