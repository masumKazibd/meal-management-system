package bd.edu.seu.mealmanagementsystem.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int userId;
    private int messId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentForMonth;
    private boolean isPaid;

    public Payment() {}

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMessId() { return messId; }
    public void setMessId(int messId) { this.messId = messId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentForMonth() { return paymentForMonth; }
    public void setPaymentForMonth(String paymentForMonth) { this.paymentForMonth = paymentForMonth; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}