/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.model.User;
import java.util.List;

/**
 *
 * @author Coder
 */
/**
 * Service layer for system user management.
 * This is the base interface for the Decorator pattern.
 */
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link User} entities.
 * <p>
 * Provides business operations for user management within the LibroNova system,
 * including user creation, retrieval, and updates.
 */
public interface UserService {

    /**
     * Creates a new user in the system.
     * <p>
     * This method includes business validations such as setting default roles and statuses.
     *
     * @param user the {@link User} to create
     * @return the created {@link User} with any generated or default fields set
     */
    User createUser(User user);

    /**
     * Retrieves a list of all users.
     *
     * @return a {@link List} of all {@link User} entities
     */
    List<User> getAllUsers();

    /**
     * Updates the information of an existing user.
     *
     * @param user the {@link User} containing updated data
     * @return the updated {@link User}
     */
    User updateUser(User user);
    Optional<User> getUserById(int id);
}

