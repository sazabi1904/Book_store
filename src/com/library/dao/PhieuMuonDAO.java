package com.library.dao;

import com.library.database.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhieuMuonDAO {

    public static class LoanInfo {
        private int id;
        private String memberName;
        private Date ngayHenTra;
        private String status;
        private int bookCount;
        private String bookTitles;

        public LoanInfo(int id, String memberName, Date ngayHenTra, String status, int bookCount, String bookTitles) {
            this.id = id;
            this.memberName = memberName;
            this.ngayHenTra = ngayHenTra;
            this.status = status;
            this.bookCount = bookCount;
            this.bookTitles = bookTitles;
        }

        public int getId() {
            return id;
        }

        public String getMemberName() {
            return memberName;
        }

        public Date getNgayHenTra() {
            return ngayHenTra;
        }

        public String getStatus() {
            return status;
        }

        public int getBookCount() {
            return bookCount;
        }

        public String getBookTitles() {
            return bookTitles;
        }
    }

    public int createPhieuMuon(int memberId, Date ngayMuon, Date ngayHenTra, int soLuongSach) throws SQLException {
        String sql = "INSERT INTO phieu_muon (member_id, ngay_muon, ngay_hen_tra, so_luong_sach) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, memberId);
            pstmt.setDate(2, ngayMuon);
            pstmt.setDate(3, ngayHenTra);
            pstmt.setInt(4, soLuongSach);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void addChiTietPhieuMuon(int phieuMuonId, String bookId) throws SQLException {
        String sql = "INSERT INTO chi_tiet_phieu_muon (phieu_muon_id, book_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, phieuMuonId);
            pstmt.setString(2, bookId);
            pstmt.executeUpdate();
        }
    }

    public List<LoanInfo> getAllBorrowSlips() throws SQLException {
        List<LoanInfo> result = new ArrayList<>();
        String sql = "SELECT p.id, m.full_name, p.ngay_hen_tra, p.so_luong_sach AS book_count, " +
                     "GROUP_CONCAT(b.title ORDER BY b.title SEPARATOR ', ') AS book_titles, " +
                     "CASE WHEN SUM(c.trang_thai = 'dang_muon') > 0 THEN 'Đang mượn' ELSE 'Hoàn tất' END AS status " +
                     "FROM phieu_muon p " +
                     "JOIN members m ON p.member_id = m.id " +
                     "JOIN chi_tiet_phieu_muon c ON c.phieu_muon_id = p.id " +
                     "JOIN books b ON b.id = c.book_id " +
                     "GROUP BY p.id, m.full_name, p.ngay_hen_tra, p.so_luong_sach " +
                     "ORDER BY p.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new LoanInfo(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getDate("ngay_hen_tra"),
                        rs.getString("status"),
                        rs.getInt("book_count"),
                        rs.getString("book_titles")
                ));
            }
        }
        return result;
    }

    public Date getDueDate(int phieuMuonId) throws SQLException {
        String sql = "SELECT ngay_hen_tra FROM phieu_muon WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, phieuMuonId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("ngay_hen_tra");
                }
            }
        }
        return null;
    }

    public int getFirstChiTietIdForPhieu(int phieuMuonId) throws SQLException {
        String sql = "SELECT id FROM chi_tiet_phieu_muon WHERE phieu_muon_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, phieuMuonId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    public int returnBooks(int phieuMuonId, Date ngayTraThucTe) throws SQLException {
        String updateDetailSql = "UPDATE chi_tiet_phieu_muon SET trang_thai = 'da_tra', ngay_tra_thuc_te = ? " +
                                 "WHERE phieu_muon_id = ? AND trang_thai = 'dang_muon'";
        String updateBookSql = "UPDATE books b " +
                               "JOIN chi_tiet_phieu_muon c ON b.id = c.book_id " +
                               "SET b.trangThai = 'available' " +
                               "WHERE c.phieu_muon_id = ? AND c.trang_thai = 'da_tra'";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement detailStmt = conn.prepareStatement(updateDetailSql);
                 PreparedStatement bookStmt = conn.prepareStatement(updateBookSql)) {
                detailStmt.setDate(1, ngayTraThucTe);
                detailStmt.setInt(2, phieuMuonId);
                int updated = detailStmt.executeUpdate();
                if (updated > 0) {
                    bookStmt.setInt(1, phieuMuonId);
                    bookStmt.executeUpdate();
                }
                conn.commit();
                return updated;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
