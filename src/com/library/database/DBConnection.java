package com.library.database;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // Update your password SQL root if any

    public static Connection getConnection() throws SQLException {
        Driver manualDriver = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Cố gắng tải driver thủ công từ file .jar trong dự án nếu classpath chưa được
            // cấu hình
            try {
                File jarFile = new File("lib/mysql-connector-j-8.3.0.jar");
                if (!jarFile.exists()) {
                    jarFile = new File("Book_store/lib/mysql-connector-j-8.3.0.jar");
                }
                if (jarFile.exists()) {
                    URL jarUrl = jarFile.toURI().toURL();
                    URLClassLoader ucl = new URLClassLoader(new URL[] { jarUrl }, DBConnection.class.getClassLoader());
                    manualDriver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver", true, ucl)
                            .getDeclaredConstructor().newInstance();
                } else {
                    System.err.println(
                            "MySQL Driver không tìm thấy trong thư viện IDE và cả đường dẫn cục bộ! Hãy kiểm tra lại thư mục lib.");
                    throw new SQLException("JDBC Driver not found", e);
                }
            } catch (Exception ex) {
                System.err.println("Lỗi khi tải MySQL Driver thủ công: " + ex.getMessage());
                throw new SQLException("JDBC Driver not found", ex);
            }
        }

        try {
            if (manualDriver != null) {
                Properties props = new Properties();
                props.put("user", USER);
                props.put("password", PASSWORD);
                Connection conn = manualDriver.connect(URL, props);
                if (conn != null)
                    return conn;
                throw new SQLException("Kết nối thất bại: Driver trả về kết nối rỗng");
            }
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
