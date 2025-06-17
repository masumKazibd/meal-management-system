package bd.edu.seu.mealmanagementsystem.Model;

public class Mess {
    private int messId;
    private String messName;
    private Integer adminId;

    public Mess() {}

    public int getMessId() { return messId; }
    public void setMessId(int messId) { this.messId = messId; }
    public String getMessName() { return messName; }
    public void setMessName(String messName) { this.messName = messName; }
    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }
}
