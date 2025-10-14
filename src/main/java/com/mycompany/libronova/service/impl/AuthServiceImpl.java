/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.UserDAO;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.AuthService;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new LibroNovaException(ErrorCode.INVALID_DATA);
        }

        Optional<User> userOpt = userDAO.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // if (BCrypt.checkpw(password, user.getPassword())) {
            if (user.getPassword().equals(password) && user.isActive()) {
                return Optional.of(user);
            }
        }
        // Throw an exception for invalid credentials to be caught by the controller
        throw new LibroNovaException(ErrorCode.INVALID_CREDENTIALS);
    }
}
