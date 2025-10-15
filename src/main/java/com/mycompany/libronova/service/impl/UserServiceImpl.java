/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.UserDAO;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.UserService;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
/**
 * Base implementation for UserService. It only contains the core logic.
 */
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User createUser(User user) {
        // Core logic: just call the DAO.
        // The decorator will add default properties before this is called.
        return userDAO.create(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userDAO.update(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        // La lógica de negocio aquí es simplemente delegar la llamada al DAO.
        return userDAO.findById(id);
    }
}
