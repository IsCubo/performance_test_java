/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao;

import com.mycompany.libronova.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing {@link User} entities.
 * <p>
 * Defines the contract for user-related CRUD operations, including search by username
 * for authentication purposes in the LibroNova system.
 */
public interface UserDAO {

    /**
     * Persists a new user in the database.
     *
     * @param user the {@link User} to be created
     * @return the created {@link User}, including any generated fields (e.g., ID)
     */
    User create(User user);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return an {@link Optional} containing the found {@link User}, or empty if not found
     */
    Optional<User> findById(int id);

    /**
     * Retrieves a user by their unique username.
     * <p>
     * Typically used during login for authentication and role validation.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the found {@link User}, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves all users from the database.
     *
     * @return a {@link List} of all {@link User} entities
     */
    List<User> findAll();

    /**
     * Updates an existing user in the database.
     *
     * @param user the {@link User} with updated information
     * @return the updated {@link User}
     */
    User update(User user);

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to delete
     */
    void delete(int id);
}

