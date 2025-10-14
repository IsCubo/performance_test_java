/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao.impl;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.dao.LoanDAO;
import com.mycompany.libronova.exception.DataAccessException;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.util.DatabaseConnector;
import com.mycompany.libronova.util.LoggerManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class LoanDAOImpl implements LoanDAO {

    private static final String INSERT_LOAN_SQL = "INSERT INTO loans (book_isbn, member_id, loan_date, due_date, return_date, fine, is_returned) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_LOAN_BY_ID_SQL = "SELECT * FROM loans WHERE id = ?";
    private static final String SELECT_ALL_LOANS_SQL = "SELECT * FROM loans";
    private static final String SELECT_OVERDUE_LOANS_SQL = "SELECT * FROM loans WHERE due_date < ? AND is_returned = false";
    private static final String UPDATE_LOAN_SQL = "UPDATE loans SET return_date = ?, fine = ?, is_returned = ? WHERE id = ?";

    @Override
    public Loan create(Loan loan) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            return create(loan, conn);
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Failed to get connection for creating loan", e);
            throw new DataAccessException(ErrorCode.DB_CONNECTION, e);
        }
    }

    @Override
    public Loan create(Loan loan, Connection connection) {
        LoggerManager.logHttpRequest("POST", "/loans", "Attempting");
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_LOAN_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, loan.getBookIsbn());
            pstmt.setInt(2, loan.getMemberId());
            pstmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(4, Date.valueOf(loan.getDueDate()));
            pstmt.setObject(5, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setDouble(6, loan.getFine());
            pstmt.setBoolean(7, loan.isReturned());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating loan failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating loan failed, no ID obtained.");
                }
            }
            LoggerManager.logHttpRequest("POST", "/loans", "201 CREATED");
            return loan;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error creating loan", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    @Override
    public Optional<Loan> findById(int id) {
        LoggerManager.logHttpRequest("GET", "/loans/" + id, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_LOAN_BY_ID_SQL)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding loan by ID: " + id, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Loan> findAll() {
        LoggerManager.logHttpRequest("GET", "/loans", "Attempting");
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_LOANS_SQL)) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding all loans", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return loans;
    }

    @Override
    public List<Loan> findOverdueLoans() {
        LoggerManager.logHttpRequest("GET", "/loans/overdue", "Attempting");
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_OVERDUE_LOANS_SQL)) {

            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding overdue loans", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return loans;
    }

    @Override
    public Loan update(Loan loan) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            return update(loan, conn);
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Failed to get connection for updating loan", e);
            throw new DataAccessException(ErrorCode.DB_CONNECTION, e);
        }
    }

    @Override
    public Loan update(Loan loan, Connection connection) {
        LoggerManager.logHttpRequest("PATCH", "/loans/" + loan.getId(), "Attempting");
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_LOAN_SQL)) {
            
            pstmt.setObject(1, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setDouble(2, loan.getFine());
            pstmt.setBoolean(3, loan.isReturned());
            pstmt.setInt(4, loan.getId());

            pstmt.executeUpdate();
            return loan;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error updating loan with ID: " + loan.getId(), e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Date returnDateSql = rs.getDate("return_date");
        LocalDate returnDate = (returnDateSql != null) ? returnDateSql.toLocalDate() : null;

        return new Loan(
                rs.getInt("id"),
                rs.getString("book_isbn"),
                rs.getInt("member_id"),
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate,
                rs.getDouble("fine"),
                rs.getBoolean("is_returned")
        );
    }
}
