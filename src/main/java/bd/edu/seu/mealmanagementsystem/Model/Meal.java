package bd.edu.seu.mealmanagementsystem.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public
class Meal {
    private int mealId;
    private int messId;
    private LocalDate mealDate;
    private MealType mealType;
    private BigDecimal costPerMeal;

    public enum MealType {
        BREAKFAST, LUNCH, DINNER
    }

    public Meal() {}

    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }
    public int getMessId() { return messId; }
    public void setMessId(int messId) { this.messId = messId; }
    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
    public BigDecimal getCostPerMeal() { return costPerMeal; }
    public void setCostPerMeal(BigDecimal costPerMeal) { this.costPerMeal = costPerMeal; }
}
