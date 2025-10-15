/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.decorator;

import com.mycompany.libronova.model.User;
import com.mycompany.libronova.model.enums.UserRole;
import com.mycompany.libronova.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
/**
 * Decorator to add default properties to a new user before creation,
 * as required by the project statement.
 */
public class UserWithDefaultsDecorator implements UserService {

    private final UserService wrappedUserService;

    public UserWithDefaultsDecorator(UserService userService) {
        this.wrappedUserService = userService;
    }
    
    @Override
    public User createUser(User user) {
        // --- DECORATOR LOGIC ---
        // Add default properties before delegating
        user.setRole(UserRole.ASSISTANT);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        // --- END DECORATOR LOGIC ---
        
        // Delegate the call to the wrapped (original) service
        return this.wrappedUserService.createUser(user);
    }

    // Other methods just delegate the call directly
    @Override
    public List<User> getAllUsers() {
        return this.wrappedUserService.getAllUsers();
    }

    @Override
    public User updateUser(User user) {
        return this.wrappedUserService.updateUser(user);
    }
    @Override
    public Optional<User> getUserById(int id) {
    // El decorator no añade lógica aquí, solo pasa la llamada al servicio envuelto.
    return (Optional<User>) this.wrappedUserService.getUserById(id);
}
}
