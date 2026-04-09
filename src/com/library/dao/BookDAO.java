package com.library.dao;

import com.library.model.Book;
import com.library.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (id, title, author, category, year, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getBookId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getCategory());
            pstmt.setInt(5, book.getYear());
            pstmt.setInt(6, book.getQuantity());
            pstmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("year"),
                        rs.getInt("quantity")));
            }
        }
        return books;
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, category=?, year=?, quantity=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setInt(4, book.getYear());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setString(6, book.getBookId());
            pstmt.executeUpdate();
        }
    }

    public void deleteBook(String bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            pstmt.executeUpdate();
        }
    }

    public void deleteAllBooks() throws SQLException {
        String sql = "DELETE FROM books";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE id LIKE ? OR title LIKE ? OR author LIKE ? OR category LIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getInt("year"),
                            rs.getInt("quantity")));
                }
            }
        }
        return books;
    }

    // Phương thức này để lấy Book theo bookId
    public Book getByBookId(String bookId) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ? OR bookId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            pstmt.setString(2, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getInt("year"),
                            rs.getInt("quantity"));
                }
            }
        }
        return null;
    }

    // Phương thức cập nhật trạng thái sách (available / borrowed)
    public void updateTrangThai(String bookId, String trangThai) throws SQLException {
        String sql = "UPDATE books SET trangThai = ? WHERE id = ? OR bookId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trangThai);
            pstmt.setString(2, bookId);
            pstmt.setString(3, bookId);
            pstmt.executeUpdate();
        }
    }

    // PHẦN THÊM VÀO: Lấy danh sách model phân loại sách
    public List<com.library.model.BookCategoryModel> getBooksCategorized() throws SQLException {
        List<com.library.model.BookCategoryModel> categoryModels = new ArrayList<>();
        String sql = "SELECT category, title FROM books ORDER BY category, title";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            String currentCategory = null;
            com.library.model.BookCategoryModel currentModel = null;
            while (rs.next()) {
                String cat = rs.getString("category");
                String title = rs.getString("title");

                if (currentCategory == null || !currentCategory.equals(cat)) {
                    currentCategory = cat;
                    currentModel = new com.library.model.BookCategoryModel(cat);
                    categoryModels.add(currentModel);
                }
                if (currentModel != null) {
                    currentModel.addBookTitle(title);
                }
            }
        }
        return categoryModels;
    }
    // ========================================================
}
