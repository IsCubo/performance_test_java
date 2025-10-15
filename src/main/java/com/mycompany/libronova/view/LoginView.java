/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view;

import com.mycompany.libronova.controller.AuthController;
import com.mycompany.libronova.model.User;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.*;

/**
 *
 * @author Coder
 */
/**
 * The login window for the application.
 * It collects user credentials and uses the AuthController to authenticate.
 */
public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel; 

    private final AuthController authController;

    public LoginView(AuthController authController) {
        this.authController = authController;

        setTitle("LibroNova - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        setResizable(false);

        initComponents();
        initListeners();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel, gbc);

        add(panel);
    }

    private Consumer<Optional<User>> loginSuccessListener;

    public LoginView(JTextField usernameField, JPasswordField passwordField, JButton loginButton, JLabel statusLabel, AuthController authController, Consumer<Optional<User>> loginSuccessListener) throws HeadlessException {
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.loginButton = loginButton;
        this.statusLabel = statusLabel;
        this.authController = authController;
        this.loginSuccessListener = loginSuccessListener;
    }

    public void setLoginSuccessListener(Consumer<Optional<User>> listener) {
        this.loginSuccessListener = listener;
    }

    private void initListeners() {
        ActionListener loginAction = e -> {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();
            Optional<User> userOptional = authController.attemptLogin(username, password);
            
            if (loginSuccessListener != null) {
                loginSuccessListener.accept(userOptional);
            }
        };
        loginButton.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
    }
}

