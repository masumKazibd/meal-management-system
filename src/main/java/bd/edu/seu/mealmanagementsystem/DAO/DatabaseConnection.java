package bd.edu.seu.mealmanagementsystem.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

        private static final String URL = "jdbc:mysql://localhost:3306/Meal_Management_System";
        private static final String USER = "root";
        private static final String PASSWORD = "admin";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }