package bd.edu.seu.mealmanagementsystem.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

        private static final String URL = "jdbc:mysql://localhost:3306/Meal_Management";
        private static final String USER = "root";
        private static final String PASSWORD = "admin";

        private static Connection connection;

        private DatabaseConnection() {}

        public static Connection getInstance() {
            if (connection == null) {
                try {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Database connected!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return connection;
        }
    }


