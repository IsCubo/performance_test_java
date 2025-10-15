/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.controller;

import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.AuthService;
import com.mycompany.libronova.util.LoggerManager;
import java.util.Optional;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
/**
 * Controller responsible for handling the user login process.
 */
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    

    /**
     * Attempts to log in the user and handles the view's response.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return An Optional containing the authenticated User on success, otherwise empty.
     */
    public Optional<User> attemptLogin(String username, char[] password) {
        try {
            String passwordStr = new String(password);
            Optional<User> userOptional = authService.login(username, passwordStr);

            if (userOptional.isPresent()) {
                LoggerManager.log(Level.INFO, "User '" + username + "' logged in successfully.");
                return userOptional;
            }
        } catch (LibroNovaException ex) {
            LoggerManager.log(Level.WARNING, "Login failed for user '" + username + "': " + ex.getMessage());
            showErrorMessage(
                "Login Failed: " + ex.getErrorCode().getMessage(),
                "Authentication Error - Code: " + ex.getErrorCode().getCode()
            );
        } catch (Exception ex) {
            LoggerManager.log(Level.SEVERE, "An unexpected error occurred during login.", ex);
            showErrorMessage(
                "An unexpected error occurred. Please check the logs.",
                "System Error"
            );
        }
        return Optional.empty();
    }
    
    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
