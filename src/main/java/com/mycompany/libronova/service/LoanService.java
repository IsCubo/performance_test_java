/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.model.Loan;
import java.util.List;

/**
 *
 * @author Coder
 */
/**
 * Service layer for handling loan and return transactions.
 */
public interface LoanService {

    /**
     * Executes a book loan transaction. This is an atomic operation.
     * It will decrease the book's stock and create a new loan record.
     * @param bookIsbn The ISBN of the book to be loaned.
     * @param memberId The ID of the member borrowing the book.
     * @return The created Loan object.
     */
    Loan performLoan(String bookIsbn, int memberId);

    /**
     * Executes a book return transaction. This is an atomic operation.
     * It will update the loan record, calculate fines, and increase the book's stock.
     * @param loanId The ID of the loan to be returned.
     * @return The updated Loan object with return date and fine.
     */
    Loan registerReturn(int loanId);

    /**
     * Retrieves all loans that are past their due date and not yet returned.
     * @return A list of overdue loans.
     */
    List<Loan> getOverdueLoans();
    
    List<Loan> getAllLoans();
    
    List<Loan> getLoansByMemberId(int memberId);
}
