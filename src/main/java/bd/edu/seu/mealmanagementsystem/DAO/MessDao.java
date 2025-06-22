package bd.edu.seu.mealmanagementsystem.DAO;

import bd.edu.seu.mealmanagementsystem.Model.Mess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessDao {
    public int createMess(Mess mess) {
        String sql = "INSERT INTO messes(mess_name, admin_id) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, mess.getMessName());
            pstmt.setInt(2, mess.getAdminId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets a list of all available messes.
     * @return A list of Mess objects.
     */
    public List<Mess> getAllMesses() {
        List<Mess> messes = new ArrayList<>();
        String sql = "SELECT * FROM messes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Mess mess = new Mess();
                mess.setMessId(rs.getInt("mess_id"));
                mess.setMessName(rs.getString("mess_name"));
                mess.setAdminId(rs.getInt("admin_id"));
                messes.add(mess);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messes;
    }

    public Mess getMessById(int messId) {
        String sql = "SELECT * FROM messes WHERE mess_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Mess mess = new Mess();
                mess.setMessId(rs.getInt("mess_id"));
                mess.setMessName(rs.getString("mess_name"));
                mess.setAdminId(rs.getInt("admin_id"));
                return mess;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add other methods like updateMess etc. as needed
}
