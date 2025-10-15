/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.dialogs;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.controller.UserController;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.model.enums.UserRole;
import javax.swing.*;
import java.awt.*;

/**
 * A modal JDialog for adding or editing a system user.
 */
public class UserFormDialog extends JDialog {
    
    private final UserController userController;
    private final boolean isEditMode;
    private User userToEdit;

    // --- UI Components ---
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<UserRole> roleComboBox;
    private JCheckBox activeCheckBox;
    private JButton saveButton, cancelButton;

    public UserFormDialog(Frame parent, UserController userController) {
        super(parent, "Add New User", true);
        this.userController = userController;
        this.isEditMode = false;
        initComponents();
    }
    
    public UserFormDialog(Frame parent, UserController userController, User userToEdit) {
        super(parent, "Edit User", true);
        this.userController = userController;
        this.isEditMode = true;
        this.userToEdit = userToEdit;
        initComponents();
        populateForm();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Username, Password, Role, Active Status
        gbc.gridy = 0; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 1; usernameField = new JTextField(20); formPanel.add(usernameField, gbc);
        gbc.gridy = 2; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 3; passwordField = new JPasswordField(); formPanel.add(passwordField, gbc);
        gbc.gridy = 4; formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridy = 5; roleComboBox = new JComboBox<>(UserRole.values()); formPanel.add(roleComboBox, gbc);
        gbc.gridy = 6; activeCheckBox = new JCheckBox("Is Active", true); formPanel.add(activeCheckBox, gbc);

        if (isEditMode) {
            // Add a hint for the password field in edit mode
            JLabel passwordHint = new JLabel(" (Leave blank to keep current password)");
            passwordHint.setFont(new Font("Serif", Font.ITALIC, 10));
            gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(passwordHint, gbc);
            gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        initListeners();
        pack();
        setLocationRelativeTo(getParent());
    }

    private void populateForm() {
        if (userToEdit != null) {
            usernameField.setText(userToEdit.getUsername());
            roleComboBox.setSelectedItem(userToEdit.getRole());
            activeCheckBox.setSelected(userToEdit.isActive());
        }
    }
    
    private void initListeners() {
        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveUser() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || (!isEditMode && password.length == 0)) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty for new users.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserRole role = (UserRole) roleComboBox.getSelectedItem();
        boolean isActive = activeCheckBox.isSelected();

        if (isEditMode) {
            userToEdit.setUsername(username);
            // Only update password if a new one is provided
            if (password.length > 0) {
                userToEdit.setPassword(new String(password));
            }
            userToEdit.setRole(role);
            userToEdit.setActive(isActive);
            if (userController.updateUser(userToEdit)) {
                dispose();
            }
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(new String(password));
            // The decorator in the service will set the default role, status, and createdAt
            if (userController.createUser(newUser)) {
                dispose();
            }
        }
    }
}
