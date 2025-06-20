package bd.edu.seu.mealmanagementsystem.Model;

public class User {
    private int userId;
    private String username;
    private String password; // In a real app, this should be hashed
    private String fullName;
    private String email;
    private Role role;
    private Integer messId;

    public enum Role {
        ADMIN, MEMBER
    }


    public User() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Integer getMessId() { return messId; }
    public void setMessId(Integer messId) { this.messId = messId; }
}