/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.IsbnDuplicateException;
import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.service.BookService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



/**
 *
 * @author Coder
 */
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;

    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public Book registerBook(Book book) {
        // Business Rule: Validate that ISBN is unique
        bookDAO.findByIsbn(book.getIsbn()).ifPresent(b -> {
            throw new IsbnDuplicateException();
        });

        // Set server-side properties
        book.setActive(true);
        book.setCreatedAt(LocalDateTime.now());
        book.setAvailableCopies(book.getTotalCopies()); // Initially, all copies are available

        return bookDAO.create(book);
    }

    @Override
    public Book updateBook(Book book) {
        // Ensure the book exists before updating
        bookDAO.findByIsbn(book.getIsbn())
                .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));
        
        return bookDAO.update(book);
    }

    @Override
    public void toggleBookStatus(String isbn) {
        Book book = bookDAO.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));
        
        book.setActive(!book.isActive());
        bookDAO.update(book);
    }
    
    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookDAO.findByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    @Override
    public List<Book> filterBooksByAuthor(String author) {
        return bookDAO.findAll().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> filterBooksByCategory(String category) {
        return bookDAO.findAll().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
