/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.panels;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.controller.UserController;
import com.mycompany.libronova.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A JPanel for managing system users. It's accessible only to ADMIN users.
 */
public class UserManagementPanel extends JPanel {

    private final UserController userController;

    // --- UI Components ---
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton;

    public UserManagementPanel(UserController userController) {
        this.userController = userController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        
        // Initial data load
        loadUserData();
    }

    private void initComponents() {
        // --- Center Panel (Table) ---
        String[] columnNames = {"ID", "Username", "Role", "Status", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Right Panel (Action Buttons) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        addButton = new JButton("Add New User");
        editButton = new JButton("Edit Selected");
        
        Dimension buttonSize = new Dimension(130, 30);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        editButton.setMaximumSize(buttonSize);

        rightPanel.add(addButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(editButton);
        
        add(rightPanel, BorderLayout.EAST);
    }

    private void initListeners() {
        addButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Add New User dialog would open here.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
        });

        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            JOptionPane.showMessageDialog(this, "Edit dialog for User ID: " + userId + " would open here.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
        });
    }

    private void loadUserData() {
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            Object[] rowData = {
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.isActive() ? "Active" : "Inactive",
                user.getCreatedAt().toLocalDate().toString()
            };
            tableModel.addRow(rowData);
        }
    }
}
