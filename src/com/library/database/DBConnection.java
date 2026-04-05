package com.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; //

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
            throw new SQLException("JDBC Driver not found", e);
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
            System.err.println("URL: " + URL);
            System.err.println("Error: " + e.getMessage());
            if (e.getMessage().contains("Access denied")) {
                System.err.println("TIP: Check if your MySQL password is correct (current: " + PASSWORD + ")");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("TIP: Run setup.sql to create the 'library_db' database.");
            }
            throw e;
        }
    }
}
