package com.library.dao;

import com.library.model.BookStatistic;
import com.library.model.BorrowedBook;
import com.library.database.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {

    public List<BookStatistic> getTopBorrowedBooks() {
        List<BookStatistic> result = new ArrayList<>();
        String sql = "SELECT b.title, COUNT(c.book_id) AS borrowCount " +
                     "FROM chi_tiet_phieu_muon c " +
                     "JOIN books b ON c.book_id = b.id " +
                     "GROUP BY b.title " +
                     "ORDER BY borrowCount DESC " +
                     "LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new BookStatistic(
                    rs.getString("title"),
                    rs.getInt("borrowCount")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi thống kê sách mượn nhiều nhất: " + e.getMessage());
        }
        return result;
    }

    public List<String> getInventoryStatus() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM books) AS totalBooks, " +
                     "(SELECT SUM(quantity) FROM books) AS totalQuantity, " +
                     "(SELECT COUNT(*) FROM chi_tiet_phieu_muon WHERE trang_thai = 'dang_muon') AS totalBorrowed";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                list.add("- Tổng số đầu sách (loại sách) trong thư viện: " + rs.getInt("totalBooks"));
                int qty = rs.getInt("totalQuantity");
                list.add("- Tổng số lượng sách (bản in): " + (rs.wasNull() ? 0 : qty));
                list.add("- Số lượng sách đang được độc giả mượn: " + rs.getInt("totalBorrowed"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi thống kê kho sách: " + e.getMessage());
        }
        return list;
    }

    public List<BorrowedBook> getOverdueBooks() {
        List<BorrowedBook> list = new ArrayList<>();
        String sql = "SELECT m.full_name AS readerName, b.title AS bookTitle, " +
                     "p.ngay_hen_tra AS returnDate, " +
                     "CASE " +
                     "  WHEN p.ngay_hen_tra < CURRENT_DATE THEN 'Quá hạn' " +
                     "  ELSE 'Đang mượn' " +
                     "END AS status " +
                     "FROM chi_tiet_phieu_muon c " +
                     "JOIN phieu_muon p ON c.phieu_muon_id = p.id " +
                     "JOIN members m ON p.member_id = m.id " +
                     "JOIN books b ON c.book_id = b.id " +
                     "WHERE c.trang_thai = 'dang_muon' " +
                     "ORDER BY p.ngay_hen_tra ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new BorrowedBook(
                    rs.getString("readerName"),
                    rs.getString("bookTitle"),
                    rs.getString("returnDate"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi theo dõi mượn trả: " + e.getMessage());
        }
        return list;
    }
}
