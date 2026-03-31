package com.library.dao;

import com.library.model.Member;
import com.library.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (username, password, full_name, birth_date, gender, address, phone, email, card_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getUsername());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getFullName());
            pstmt.setString(4, member.getBirthDate());
            pstmt.setString(5, member.getGender());
            pstmt.setString(6, member.getAddress());
            pstmt.setString(7, member.getPhone());
            pstmt.setString(8, member.getEmail());
            pstmt.setString(9, member.getLibraryCardId());
            pstmt.executeUpdate();
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("birth_date"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("card_id")
                ));
            }
        }
        return members;
    }

    public Member login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM members WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("birth_date"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("card_id")
                    );
                }
            }
        }
        return null;
    }

    public void updateMember(Member member) throws SQLException {
        String sql = "UPDATE members SET full_name=?, birth_date=?, gender=?, address=?, phone=?, email=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getFullName());
            pstmt.setString(2, member.getBirthDate());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getAddress());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getEmail());
            pstmt.setInt(7, member.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteMember(String username) throws SQLException {
        String sql = "DELETE FROM members WHERE username = ? AND username != 'admin'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    public void deleteAllMembers() throws SQLException {
        String sql = "DELETE FROM members WHERE username != 'admin'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
