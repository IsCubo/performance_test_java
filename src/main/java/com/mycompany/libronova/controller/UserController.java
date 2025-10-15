/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.controller;

import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.UserService;
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
 * Controller to manage interactions between the user views and the user service.
 */
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetches all system users to be displayed in a view.
     * @return A list of all users, or an empty list if an error occurs.
     */
    public List<User> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (LibroNovaException ex) {
            handleError(ex, "Failed to load system users.");
            return Collections.emptyList();
        }
    }

    /**
     * Handles the logic for creating a new user.
     * This method relies on the decorator to set default values.
     * @param user The user object with basic info (username, password) from the form.
     * @return true if the creation was successful, false otherwise.
     */
    public boolean createUser(User user) {
        try {
            // The injected service should be the decorator, so it will handle defaults
            userService.createUser(user);
            showSuccessMessage("User '" + user.getUsername() + "' was successfully created with default role and status.");
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Could not create the user.");
            return false;
        }
    }

    /**
     * Handles the logic for updating an existing user.
     * @param userToUpdate The user object with updated data from a form.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUser(User userToUpdate) {
        try {
            userService.updateUser(userToUpdate);
            showSuccessMessage("User '" + userToUpdate.getUsername() + "' was successfully updated.");
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Could not update the user.");
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
    
    public Optional<User> getUserById(int id) {
    try {
        return userService.getUserById(id);
    } catch (LibroNovaException ex) {
        handleError(ex, "Failed to retrieve user with id: " + id);
        return Optional.empty();
    }
}
}
