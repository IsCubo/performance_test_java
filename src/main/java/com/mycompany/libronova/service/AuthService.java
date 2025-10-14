/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.model.User;
import java.util.Optional;

/**
 * Service for handling user authentication.
 */
public interface AuthService {

    /**
     * Attempts to log in a user with the given credentials.
     * @param username The user's username.
     * @param password The user's password.
     * @return An Optional containing the authenticated User if credentials are valid and user is active, otherwise empty.
     */
    Optional<User> login(String username, String password);
}