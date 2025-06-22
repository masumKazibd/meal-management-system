package bd.edu.seu.mealmanagementsystem.Model;

public class MealAttendance {
    private int attendanceId;
    private int mealId;
    private int userId;
    private boolean isPresent;

    public MealAttendance() {}
    public MealAttendance(int mealId, int userId, boolean isPresent) {
        this.mealId = mealId;
        this.userId = userId;
        this.isPresent = isPresent;
    }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}