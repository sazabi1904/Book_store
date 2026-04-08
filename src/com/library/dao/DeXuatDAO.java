package com.library.dao;

import com.library.model.DeXuat;
import com.library.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeXuatDAO {
    
    public void addDeXuat(int memberId, String tenSach, String tacGia) throws SQLException {
        String sql = "INSERT INTO de_xuat (member_id, ten_sach, tac_gia) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setString(2, tenSach);
            pstmt.setString(3, tacGia);
            pstmt.executeUpdate();
        }
    }

    public List<DeXuat> getAllDeXuat() throws SQLException {
        List<DeXuat> list = new ArrayList<>();
        String sql = "SELECT d.*, m.username, m.full_name FROM de_xuat d JOIN members m ON d.member_id = m.id ORDER BY d.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DeXuat(
                    rs.getInt("id"),
                    rs.getInt("member_id"),
                    rs.getString("ten_sach"),
                    rs.getString("tac_gia"),
                    rs.getString("trang_thai"),
                    rs.getString("username")
                ));
            }
        }
        return list;
    }

    public void clearAllDeXuat() throws SQLException {
        String sql = "DELETE FROM de_xuat";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
