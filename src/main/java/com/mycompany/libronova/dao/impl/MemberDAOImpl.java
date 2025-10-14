/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao.impl;

import com.mycompany.libronova.dao.MemberDAO;
import com.mycompany.libronova.exception.DataAccessException;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.enums.MemberStatus;
import com.mycompany.libronova.util.DatabaseConnector;
import com.mycompany.libronova.util.LoggerManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 *
 * @author Coder
 */
public class MemberDAOImpl implements MemberDAO{
    private static final String INSERT_MEMBER_SQL = "INSERT INTO members (first_name, last_name, email, status, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_MEMBER_BY_ID_SQL = "SELECT * FROM members WHERE id = ?";
    private static final String SELECT_ALL_MEMBERS_SQL = "SELECT * FROM members";
    private static final String UPDATE_MEMBER_SQL = "UPDATE members SET first_name = ?, last_name = ?, email = ?, status = ? WHERE id = ?";
    private static final String DELETE_MEMBER_SQL = "DELETE FROM members WHERE id = ?";

    @Override
    public Member create(Member member) {
        LoggerManager.logHttpRequest("POST", "/members", "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_MEMBER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, member.getFirstName());
            pstmt.setString(2, member.getLastName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getStatus().name());
            pstmt.setTimestamp(5, Timestamp.valueOf(member.getCreatedAt()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating member failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating member failed, no ID obtained.");
                }
            }
            LoggerManager.logHttpRequest("POST", "/members", "201 CREATED");
            return member;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error creating member", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    @Override
    public Optional<Member> findById(int id) {
        LoggerManager.logHttpRequest("GET", "/members/" + id, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_MEMBER_BY_ID_SQL)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding member by ID: " + id, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        LoggerManager.logHttpRequest("GET", "/members", "Attempting");
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_MEMBERS_SQL)) {

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding all members", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return members;
    }

    @Override
    public Member update(Member member) {
        LoggerManager.logHttpRequest("PATCH", "/members/" + member.getId(), "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_MEMBER_SQL)) {
            
            pstmt.setString(1, member.getFirstName());
            pstmt.setString(2, member.getLastName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getStatus().name());
            pstmt.setInt(5, member.getId());

            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error updating member with ID: " + member.getId(), e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    @Override
    public void delete(int id) {
        LoggerManager.logHttpRequest("DELETE", "/members/" + id, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_MEMBER_SQL)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error deleting member with ID: " + id, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                MemberStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
