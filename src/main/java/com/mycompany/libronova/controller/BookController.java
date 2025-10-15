/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.controller;

import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.service.BookService;
import com.mycompany.libronova.util.LoggerManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
/**
 * Controller to manage interactions between the book views and the book service.
 */
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Fetches all books and returns them for display in a view.
     * @return A list of all books, or an empty list if an error occurs.
     */
    public List<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (LibroNovaException ex) {
            handleError(ex, "Failed to load books.");
            return Collections.emptyList();
        }
    }

    /**
     * Handles the logic for creating a new book.
     * @param newBook The book object populated with data from a form.
     * @return true if the creation was successful, false otherwise.
     */
    public boolean createBook(Book newBook) {
        try {
            bookService.registerBook(newBook);
            showSuccessMessage("Book '" + newBook.getTitle() + "' was successfully registered.");
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Could not register the book.");
            return false;
        }
    }

    /**
     * Handles the logic for updating an existing book.
     * @param bookToUpdate The book object with updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateBook(Book bookToUpdate) {
        try {
            bookService.updateBook(bookToUpdate);
            showSuccessMessage("Book '" + bookToUpdate.getTitle() + "' was successfully updated.");
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Could not update the book.");
            return false;
        }
    }

    /**
     * Toggles the active status of a book.
     * @param isbn The ISBN of the book to toggle.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean toggleBookStatus(String isbn) {
        try {
            bookService.toggleBookStatus(isbn);
            showSuccessMessage("The status of the book with ISBN " + isbn + " has been updated.");
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Could not update the book's status.");
            return false;
        }
    }

    private void handleError(LibroNovaException ex, String logMessage) {
        LoggerManager.log(Level.SEVERE, logMessage + " Error: " + ex.getMessage(), ex);
        JOptionPane.showMessageDialog(
            null,
            "Operation failed: " + ex.getErrorCode().getMessage(),
            "Error - Code: " + ex.getErrorCode().getCode(),
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Optional<Book> getBookByIsbn(String isbn) {
        try {
            // Delega la llamada directamente al servicio.
            return bookService.getBookByIsbn(isbn);
        } catch (LibroNovaException ex) {
            // Si ocurre un error en una capa inferior (ej. error de BD), lo maneja.
            LoggerManager.log(Level.SEVERE, "Failed to retrieve book with ISBN: " + isbn, ex);
            JOptionPane.showMessageDialog(
                null,
                "Failed to retrieve book: " + ex.getErrorCode().getMessage(),
                "Error - Code: " + ex.getErrorCode().getCode(),
                JOptionPane.ERROR_MESSAGE
            );
            return Optional.empty(); // Devuelve un Optional vacío en caso de error.
        }
    }
}
