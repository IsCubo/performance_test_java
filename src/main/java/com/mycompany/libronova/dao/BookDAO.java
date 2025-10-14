/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao;

import com.mycompany.libronova.model.Book;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Book entity. Defines standard operations to
 * be performed on Book model objects.
 */
public interface BookDAO {

    /**
     * Creates a new book record in the database.
     *
     * @param book The book object to create.
     * @return The created book object.
     */
    Book create(Book book);

    /**
     * Creates a new book record in the database using an existing transaction.
     *
     * @param book The book object to create.
     * @param connection The existing database connection.
     * @return The created book object.
     */
    Book create(Book book, Connection connection);

    /**
     * Finds a book by its unique ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return An Optional containing the found book, or empty if not found.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Retrieves all books from the database.
     *
     * @return A list of all books.
     */
    List<Book> findAll();

    /**
     * Updates an existing book's information in the database.
     *
     * @param book The book object with updated information.
     * @return The updated book object.
     */
    Book update(Book book);

    /**
     * Updates an existing book's information using an existing transaction.
     *
     * @param book The book object with updated information.
     * @param connection The existing database connection.
     * @return The updated book object.
     */
    Book update(Book book, Connection connection);

    /**
     * Deletes a book from the database by its ISBN.
     *
     * @param isbn The ISBN of the book to delete.
     */
    void delete(String isbn);
}

