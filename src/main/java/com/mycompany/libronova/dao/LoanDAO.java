/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.model.Loan;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing {@link Loan} entities.
 * <p>
 * Defines the contract for handling loan-related operations in the LibroNova system,
 * including support for transaction management via external database connections.
 */
public interface LoanDAO {

    /**
     * Persists a new loan into the database using a standalone connection.
     *
     * @param loan the {@link Loan} to be created
     * @return the created {@link Loan} with any generated fields (e.g., ID)
     */
    Loan create(Loan loan);

    /**
     * Persists a new loan using an existing database connection.
     * <p>
     * This method is used within transactional operations such as loan creation
     * alongside stock updates.
     *
     * @param loan       the {@link Loan} to be created
     * @param connection the active {@link Connection} to be used
     * @return the created {@link Loan}
     */
    Loan create(Loan loan, Connection connection);

    /**
     * Retrieves a loan by its unique ID.
     *
     * @param id the ID of the loan to retrieve
     * @return an {@link Optional} containing the found {@link Loan}, or empty if not found
     */
    Optional<Loan> findById(int id);

    /**
     * Retrieves all loans from the database.
     *
     * @return a {@link List} of all {@link Loan} entities
     */
    List<Loan> findAll();

    /**
     * Retrieves all loans that are currently overdue.
     * <p>
     * Typically used for reporting or applying fines.
     *
     * @return a {@link List} of {@link Loan} entities that are overdue
     */
    List<Loan> findOverdueLoans();

    /**
     * Updates an existing loan in the database using a standalone connection.
     *
     * @param loan the {@link Loan} with updated information
     * @return the updated {@link Loan}
     */
    Loan update(Loan loan);

    /**
     * Updates an existing loan using an existing database connection.
     * <p>
     * Used in transactional operations like returns or fine calculations.
     *
     * @param loan       the {@link Loan} to update
     * @param connection the active {@link Connection} to be used
     * @return the updated {@link Loan}
     */
    Loan update(Loan loan, Connection connection);
}

