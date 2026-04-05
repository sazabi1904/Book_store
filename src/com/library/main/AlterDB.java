package com.library.main;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import com.library.database.DBConnection;

public class AlterDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            try {
                stmt.execute("ALTER TABLE members ADD COLUMN login_count INT DEFAULT 0");
                System.out.println("Column added.");
            } catch (SQLException e) {
                System.out.println("Column likely already exists: " + e.getMessage());
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
