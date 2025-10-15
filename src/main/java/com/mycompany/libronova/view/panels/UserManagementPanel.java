package com.mycompany.libronova.view.panels;

import com.mycompany.libronova.controller.UserController;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.view.dialogs.UserFormDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {

    private final UserController userController;

    // --- Componentes Visuales ---
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton;

    public UserManagementPanel(UserController userController) {
        this.userController = userController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents(); // Crear los componentes
        initListeners();  // Asignar acciones a los botones
        loadUserData();   // Cargar los datos iniciales
    }

    // <<< CORRECCIÓN: CÓDIGO COMPLETO PARA CREAR LOS COMPONENTES VISUALES
    private void initComponents() {
        // --- Panel Central (Tabla) ---
        String[] columnNames = {"ID", "Username", "Role", "Status", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Derecho (Botones de Acción) ---
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
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciador
        rightPanel.add(editButton);
        
        add(rightPanel, BorderLayout.EAST);
    }

    private void initListeners() {
        // La lógica para añadir un nuevo usuario está bien.
        addButton.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            UserFormDialog dialog = new UserFormDialog(parentFrame, userController);
            dialog.setVisible(true);
            loadUserData(); // Refresca la tabla después de cerrar el diálogo
        });

        // <<< CORRECCIÓN: LÓGICA COMPLETA PARA EDITAR UN USUARIO
        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // La forma correcta de manejar un Optional: usar .ifPresent()
            // Este código solo se ejecuta SI el Optional contiene un User.
            userController.getUserById(userId).ifPresent(userToEdit -> {
                 Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
                 UserFormDialog dialog = new UserFormDialog(parentFrame, userController, userToEdit);
                 dialog.setVisible(true);
                 loadUserData(); // Refresca la tabla
            });
        });
    }

    private void loadUserData() {
        // Este método está bien, pero depende de que `tableModel` no sea null.
        // Por eso es crucial que initComponents() se ejecute primero.
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