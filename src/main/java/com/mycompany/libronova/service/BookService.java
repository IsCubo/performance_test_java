/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service;

/**
 *
 * @author Coder
 */

import com.mycompany.libronova.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for book management.
 */
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link Book} entities.
 * <p>
 * Defines business operations related to book management in the LibroNova system,
 * including registration, updates, status toggling, retrieval, and filtering.
 */
public interface BookService {

    /**
     * Registers a new book in the system.
     * <p>
     * Validates that the ISBN is unique before registering.
     *
     * @param book the {@link Book} to register
     * @return the registered {@link Book} with any generated or default fields set
     */
    Book registerBook(Book book);

    /**
     * Updates the information of an existing book.
     *
     * @param book the {@link Book} with updated data
     * @return the updated {@link Book}
     */
    Book updateBook(Book book);

    /**
     * Toggles the active status of a book based on its ISBN.
     * <p>
     * Activates the book if inactive, or deactivates if active.
     *
     * @param isbn the ISBN of the book to toggle status
     */
    void toggleBookStatus(String isbn);

    /**
     * Retrieves a book by its unique ISBN.
     *
     * @param isbn the ISBN of the book to find
     * @return an {@link Optional} containing the found {@link Book}, or empty if not found
     */
    Optional<Book> getBookByIsbn(String isbn);

    /**
     * Retrieves all books in the system.
     *
     * @return a {@link List} of all {@link Book} entities
     */
    List<Book> getAllBooks();

    /**
     * Filters books by the given author name.
     *
     * @param author the author name to filter books by
     * @return a {@link List} of {@link Book} entities matching the author filter
     */
    List<Book> filterBooksByAuthor(String author);

    /**
     * Filters books by the given category.
     *
     * @param category the category to filter books by
     * @return a {@link List} of {@link Book} entities matching the category filter
     */
    List<Book> filterBooksByCategory(String category);

}

