/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao.impl;

import com.mycompany.libronova.dao.UserDAO;
import com.mycompany.libronova.exception.DataAccessException;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.model.enums.UserRole;
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
public class UserDAOImpl implements UserDAO {

    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password, role, is_active, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_USERNAME_SQL = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String UPDATE_USER_SQL = "UPDATE users SET username = ?, password = ?, role = ?, is_active = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";

    @Override
    public User create(User user) {
        LoggerManager.logHttpRequest("POST", "/users", "Attempting");
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().name());
            pstmt.setBoolean(4, user.isActive());
            pstmt.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            LoggerManager.logHttpRequest("POST", "/users", "201 CREATED");
            return user;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error creating user", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        LoggerManager.logHttpRequest("GET", "/users/" + id, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_ID_SQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding user by ID: " + id, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        LoggerManager.logHttpRequest("GET", "/users?username=" + username, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_SQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding user by username: " + username, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        LoggerManager.logHttpRequest("GET", "/users", "Attempting");
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS_SQL)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding all users", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return users;
    }

    @Override
    public User update(User user) {
        LoggerManager.logHttpRequest("PATCH", "/users/" + user.getId(), "Attempting");
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE_USER_SQL)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().name());
            pstmt.setBoolean(4, user.isActive());
            pstmt.setInt(5, user.getId());

            pstmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error updating user with ID: " + user.getId(), e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    @Override
    public void delete(int id) {
        LoggerManager.logHttpRequest("DELETE", "/users/" + id, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_USER_SQL)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error deleting user with ID: " + id, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                UserRole.valueOf(rs.getString("role")),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
