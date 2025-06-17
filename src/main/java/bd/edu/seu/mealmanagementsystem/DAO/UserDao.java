package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return User object if found, otherwise null.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password")); // Password should be handled securely
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(User.Role.valueOf(rs.getString("role").toUpperCase()));
                user.setMessId(rs.getObject("mess_id", Integer.class));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a new user to the database.
     * @param user The user object to add.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users(username, password, full_name, email, role, mess_id) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Store hashed password in a real app
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole().name().toLowerCase());
            pstmt.setObject(6, user.getMessId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add other methods like updateUser, deleteUser, getUserById etc. as needed
}

