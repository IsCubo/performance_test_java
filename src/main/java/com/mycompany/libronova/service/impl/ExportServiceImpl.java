/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.dao.LoanDAO;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.service.ExportService;
import com.mycompany.libronova.util.LoggerManager;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Coder
 */
public class ExportServiceImpl implements ExportService {

    private final BookDAO bookDAO;
    private final LoanDAO loanDAO;

    public ExportServiceImpl(BookDAO bookDAO, LoanDAO loanDAO) {
        this.bookDAO = bookDAO;
        this.loanDAO = loanDAO;
    }

    @Override
    public void exportBooksToCsv(Path path) {
        List<Book> books = bookDAO.findAll();
        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile()))) {
            // CSV Header
            writer.println("isbn,title,author,category,total_copies,available_copies,reference_price,is_active");

            // CSV Data
            for (Book book : books) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%.2f,%b%n",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.getTotalCopies(),
                        book.getAvailableCopies(),
                        book.getReferencePrice(),
                        book.isActive());
            }
            LoggerManager.log(Level.INFO, "Book catalog successfully exported to " + path);
        } catch (IOException e) {
            LoggerManager.log(Level.SEVERE, "Failed to export books to CSV", e);
            throw new LibroNovaException(ErrorCode.CSV_EXPORT_ERROR, e);
        }
    }

    @Override
    public void exportOverdueLoansToCsv(Path path) {
        List<Loan> loans = loanDAO.findOverdueLoans();
        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile()))) {
            // CSV Header
            writer.println("loan_id,book_isbn,member_id,loan_date,due_date");

            // CSV Data
            for (Loan loan : loans) {
                writer.printf("%d,\"%s\",%d,%s,%s%n",
                        loan.getId(),
                        loan.getBookIsbn(),
                        loan.getMemberId(),
                        loan.getLoanDate().toString(),
                        loan.getDueDate().toString());
            }
            LoggerManager.log(Level.INFO, "Overdue loans successfully exported to " + path);
        } catch (IOException e) {
            LoggerManager.log(Level.SEVERE, "Failed to export overdue loans to CSV", e);
            throw new LibroNovaException(ErrorCode.CSV_EXPORT_ERROR, e);
        }
    }
}
