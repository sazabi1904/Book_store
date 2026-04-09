package com.library.dao;

import com.library.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViPhamDAO {

    public static class ViPhamInfo {
        private int id;
        private int phieuMuonId;
        private int chiTietId;
        private String loaiViPham;
        private double tienPhat;
        private String ghiChu;
        private String createdAt;

        public ViPhamInfo(int id, int phieuMuonId, int chiTietId, String loaiViPham,
                          double tienPhat, String ghiChu, String createdAt) {
            this.id = id;
            this.phieuMuonId = phieuMuonId;
            this.chiTietId = chiTietId;
            this.loaiViPham = loaiViPham;
            this.tienPhat = tienPhat;
            this.ghiChu = ghiChu;
            this.createdAt = createdAt;
        }

        public int getId() {
            return id;
        }

        public int getPhieuMuonId() {
            return phieuMuonId;
        }

        public int getChiTietId() {
            return chiTietId;
        }

        public String getLoaiViPham() {
            return loaiViPham;
        }

        public double getTienPhat() {
            return tienPhat;
        }

        public String getGhiChu() {
            return ghiChu;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    public void insertViolation(int chiTietId, String loaiViPham, double tienPhat, String ghiChu) throws SQLException {
        String sql = "INSERT INTO vi_pham (chi_tiet_phieu_muon_id, loai_vi_pham, tien_phat, ghi_chu) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTietId);
            pstmt.setString(2, loaiViPham);
            pstmt.setDouble(3, tienPhat);
            pstmt.setString(4, ghiChu);
            pstmt.executeUpdate();
        }
    }

    public List<ViPhamInfo> getAllViolations() throws SQLException {
        List<ViPhamInfo> result = new ArrayList<>();
        String sql = "SELECT v.id, p.id AS phieu_muon_id, c.id AS chi_tiet_id, v.loai_vi_pham, v.tien_phat, v.ghi_chu, v.created_at " +
                     "FROM vi_pham v " +
                     "JOIN chi_tiet_phieu_muon c ON c.id = v.chi_tiet_phieu_muon_id " +
                     "JOIN phieu_muon p ON p.id = c.phieu_muon_id " +
                     "ORDER BY v.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new ViPhamInfo(
                        rs.getInt("id"),
                        rs.getInt("phieu_muon_id"),
                        rs.getInt("chi_tiet_id"),
                        rs.getString("loai_vi_pham"),
                        rs.getDouble("tien_phat"),
                        rs.getString("ghi_chu"),
                        rs.getString("created_at")
                ));
            }
        }
        return result;
    }

    public boolean deleteViolation(int violationId) throws SQLException {
        String sql = "DELETE FROM vi_pham WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, violationId);
            return pstmt.executeUpdate() > 0;
        }
    }
}
