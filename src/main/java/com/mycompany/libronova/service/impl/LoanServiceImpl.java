/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.dao.LoanDAO;
import com.mycompany.libronova.dao.MemberDAO;
import com.mycompany.libronova.exception.DataAccessException;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.InactiveMemberException;
import com.mycompany.libronova.exception.InsufficientStockException;
import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.enums.MemberStatus;
import com.mycompany.libronova.service.LoanService;
import com.mycompany.libronova.util.DatabaseConnector;
import com.mycompany.libronova.util.LoggerManager;
import com.mycompany.libronova.util.PropertiesLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Coder
 */
public class LoanServiceImpl implements LoanService {

    private final LoanDAO loanDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    public LoanServiceImpl(LoanDAO loanDAO, BookDAO bookDAO, MemberDAO memberDAO) {
        this.loanDAO = loanDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
    }

    @Override
    public Loan performLoan(String bookIsbn, int memberId) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // --- START TRANSACTION ---

            // 1. Get and validate the book
            Book book = bookDAO.findByIsbn(bookIsbn)
                    .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));

            // 2. Business Rule: Check for available copies
            if (book.getAvailableCopies() <= 0) {
                throw new InsufficientStockException();
            }

            // 3. Get and validate the member
            Member member = memberDAO.findById(memberId)
                    .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));

            // 4. Business Rule: Check if member is active
            if (member.getStatus() != MemberStatus.ACTIVE) {
                throw new InactiveMemberException();
            }

            // 5. Update book stock
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.update(book, conn); // Use the same connection for the transaction

            // 6. Create the loan record
            int loanDays = Integer.parseInt(PropertiesLoader.getProperty("diasPrestamo"));
            Loan newLoan = new Loan();
            newLoan.setBookIsbn(bookIsbn);
            newLoan.setMemberId(memberId);
            newLoan.setLoanDate(LocalDate.now());
            newLoan.setDueDate(LocalDate.now().plusDays(loanDays));
            newLoan.setReturned(false);
            newLoan.setFine(0.0);
            
            Loan createdLoan = loanDAO.create(newLoan, conn); // Use the same connection

            conn.commit(); // --- COMMIT TRANSACTION ---
            LoggerManager.log(Level.INFO, "Loan successful for book ISBN " + bookIsbn + " to member ID " + memberId);
            return createdLoan;

        } catch (SQLException | LibroNovaException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // --- ROLLBACK TRANSACTION ---
                    LoggerManager.log(Level.WARNING, "Transaction rolled back for performLoan: " + e.getMessage());
                }
            } catch (SQLException se) {
                LoggerManager.log(Level.SEVERE, "CRITICAL: Failed to rollback transaction.", se);
            }
            if (e instanceof LibroNovaException) throw (LibroNovaException) e;
            throw new DataAccessException(ErrorCode.TRANSACTION_ERROR, e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public Loan registerReturn(int loanId) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // --- START TRANSACTION ---

            // 1. Get the loan to be returned
            Loan loan = loanDAO.findById(loanId)
                    .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));

            // 2. Get the associated book
            Book book = bookDAO.findByIsbn(loan.getBookIsbn())
                    .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));

            // 3. Update book stock
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookDAO.update(book, conn);

            // 4. Calculate fine
            double fine = calculateFine(loan.getDueDate());
            
            // 5. Update loan record
            loan.setReturned(true);
            loan.setReturnDate(LocalDate.now());
            loan.setFine(fine);
            loanDAO.update(loan, conn);

            conn.commit(); // --- COMMIT TRANSACTION ---
            LoggerManager.log(Level.INFO, "Return successful for loan ID " + loanId);
            return loan;

        } catch (SQLException | LibroNovaException e) {
            try {
                if (conn != null) conn.rollback(); // --- ROLLBACK TRANSACTION ---
            } catch (SQLException se) {
                LoggerManager.log(Level.SEVERE, "CRITICAL: Failed to rollback transaction.", se);
            }
            if (e instanceof LibroNovaException) throw (LibroNovaException) e;
            throw new DataAccessException(ErrorCode.TRANSACTION_ERROR, e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public List<Loan> getOverdueLoans() {
        return loanDAO.findOverdueLoans();
    }

    private double calculateFine(LocalDate dueDate) {
        long overdueDays = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        if (overdueDays > 0) {
            double finePerDay = Double.parseDouble(PropertiesLoader.getProperty("multaPorDia"));
            return overdueDays * finePerDay;
        }
        return 0.0;
    }
    
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // Restore default behavior
                conn.close();
            } catch (SQLException e) {
                LoggerManager.log(Level.SEVERE, "Failed to close connection.", e);
            }
        }
    }
}
