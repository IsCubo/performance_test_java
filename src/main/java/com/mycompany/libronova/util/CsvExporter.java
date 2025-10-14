/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.util;

import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Coder
 */
/**
 * Utility class to handle the logic of writing data to a CSV file.
 */
public class CsvExporter {

    /**
     * A generic method to export a list of items to a CSV file.
     * @param <T> The type of the items in the list.
     * @param path The file path to save the CSV to.
     * @param header The CSV header row as a string.
     * @param items The list of items to export.
     * @param rowMapper A function that converts an item of type T into a CSV formatted string.
     * @throws IOException if an I/O error occurs.
     */
    private static <T> void export(Path path, String header, List<T> items, Function<T, String> rowMapper) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile()))) {
            writer.println(header);
            for (T item : items) {
                writer.println(rowMapper.apply(item));
            }
        }
    }

    /**
     * Exports a list of books to the specified path.
     * @param path The destination file path.
     * @param books The list of books to export.
     * @throws IOException if an I/O error occurs.
     */
    public static void exportBooks(Path path, List<Book> books) throws IOException {
        String header = "isbn,title,author,category,total_copies,available_copies,reference_price,is_active";
        
        Function<Book, String> rowMapper = book -> String.format(
            "\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%.2f,%b",
            book.getIsbn(),
            book.getTitle().replace("\"", "\"\""), // Escape quotes
            book.getAuthor().replace("\"", "\"\""),
            book.getCategory(),
            book.getTotalCopies(),
            book.getAvailableCopies(),
            book.getReferencePrice(),
            book.isActive()
        );

        export(path, header, books, rowMapper);
    }

    /**
     * Exports a list of overdue loans to the specified path.
     * @param path The destination file path.
     * @param loans The list of loans to export.
     * @throws IOException if an I/O error occurs.
     */
    public static void exportOverdueLoans(Path path, List<Loan> loans) throws IOException {
        String header = "loan_id,book_isbn,member_id,loan_date,due_date";
        
        Function<Loan, String> rowMapper = loan -> String.format(
            "%d,\"%s\",%d,%s,%s",
            loan.getId(),
            loan.getBookIsbn(),
            loan.getMemberId(),
            loan.getLoanDate().toString(),
            loan.getDueDate().toString()
        );
        
        export(path, header, loans, rowMapper);
    }
}