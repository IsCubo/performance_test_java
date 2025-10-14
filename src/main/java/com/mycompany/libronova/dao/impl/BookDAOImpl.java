/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao.impl;
import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.exception.DataAccessException;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.util.DatabaseConnector;
import com.mycompany.libronova.util.LoggerManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * JDBC implementation of the BookDAO interface.
 */
public class BookDAOImpl implements BookDAO {

    private static final String INSERT_BOOK_SQL = "INSERT INTO books (isbn, title, author, category, total_copies, available_copies, reference_price, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BOOK_BY_ISBN_SQL = "SELECT * FROM books WHERE isbn = ?";
    private static final String SELECT_ALL_BOOKS_SQL = "SELECT * FROM books";
    private static final String UPDATE_BOOK_SQL = "UPDATE books SET title = ?, author = ?, category = ?, total_copies = ?, available_copies = ?, reference_price = ?, is_active = ? WHERE isbn = ?";
    private static final String DELETE_BOOK_SQL = "DELETE FROM books WHERE isbn = ?";

    @Override
    public Book create(Book book) {
        LoggerManager.logHttpRequest("POST", "/books", "Attempting");
        try (Connection conn = DatabaseConnector.getConnection()) {
            return create(book, conn);
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Failed to get connection for creating book", e);
            throw new DataAccessException(ErrorCode.DB_CONNECTION, e);
        }
    }

    @Override
    public Book create(Book book, Connection connection) {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_BOOK_SQL)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getCategory());
            pstmt.setInt(5, book.getTotalCopies());
            pstmt.setInt(6, book.getAvailableCopies());
            pstmt.setDouble(7, book.getReferencePrice());
            pstmt.setBoolean(8, book.isActive());
            pstmt.setTimestamp(9, Timestamp.valueOf(book.getCreatedAt()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException(ErrorCode.QUERY_ERROR, new SQLException("Creating book failed, no rows affected."));
            }
            LoggerManager.logHttpRequest("POST", "/books", "201 CREATED");
            return book;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error creating book with ISBN: " + book.getIsbn(), e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        LoggerManager.logHttpRequest("GET", "/books/" + isbn, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOK_BY_ISBN_SQL)) {
            
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                LoggerManager.logHttpRequest("GET", "/books/" + isbn, "200 OK");
                return Optional.of(mapResultSetToBook(rs));
            }
            LoggerManager.logHttpRequest("GET", "/books/" + isbn, "404 NOT FOUND");
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error finding book with ISBN: " + isbn, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        LoggerManager.logHttpRequest("GET", "/books", "Attempting");
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_BOOKS_SQL)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            LoggerManager.logHttpRequest("GET", "/books", "200 OK");
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error retrieving all books", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
        return books;
    }

    @Override
    public Book update(Book book) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            return update(book, conn);
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Failed to get connection for updating book", e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }
    
    @Override
    public Book update(Book book, Connection connection) {
        LoggerManager.logHttpRequest("PATCH", "/books/" + book.getIsbn(), "Attempting");
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_BOOK_SQL)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setInt(4, book.getTotalCopies());
            pstmt.setInt(5, book.getAvailableCopies());
            pstmt.setDouble(6, book.getReferencePrice());
            pstmt.setBoolean(7, book.isActive());
            pstmt.setString(8, book.getIsbn());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException(ErrorCode.QUERY_ERROR, new SQLException("Updating book failed, no rows affected."));
            }
            LoggerManager.logHttpRequest("PATCH", "/books/" + book.getIsbn(), "200 OK");
            return book;
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error updating book with ISBN: " + book.getIsbn(), e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }


    @Override
    public void delete(String isbn) {
        LoggerManager.logHttpRequest("DELETE", "/books/" + isbn, "Attempting");
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOK_SQL)) {
            
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            LoggerManager.logHttpRequest("DELETE", "/books/" + isbn, "204 NO CONTENT");
        } catch (SQLException e) {
            LoggerManager.log(Level.SEVERE, "Error deleting book with ISBN: " + isbn, e);
            throw new DataAccessException(ErrorCode.QUERY_ERROR, e);
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies"),
                rs.getDouble("reference_price"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
