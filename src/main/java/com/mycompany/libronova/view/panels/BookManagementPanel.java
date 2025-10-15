/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.panels;

import com.mycompany.libronova.controller.BookController;
import com.mycompany.libronova.model.Book;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author Coder
 */
/**
 * A JPanel for managing the book catalog. It displays books in a table
 * and provides controls for filtering, adding, and editing books.
 */
public class BookManagementPanel extends JPanel {

    private final BookController bookController;

    // --- UI Components ---
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, toggleStatusButton;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    public BookManagementPanel(BookController bookController) {
        this.bookController = bookController;
        
        // Use BorderLayout for the main panel structure
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        
        // Initial data load
        loadBookData();
    }

    private void initComponents() {
        // --- Top Panel (Toolbar with filters and search) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        topPanel.add(new JLabel("Filter by:"));
        filterComboBox = new JComboBox<>(new String[]{"Author", "Category"});
        topPanel.add(filterComboBox);
        
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        
        JButton searchButton = new JButton("Search");
        topPanel.add(searchButton);
        
        JButton clearButton = new JButton("Clear");
        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel (Table with books) ---
        String[] columnNames = {"ISBN", "Title", "Author", "Category", "Available Copies", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one row to be selected
        bookTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Right Panel (Action Buttons) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        addButton = new JButton("Add New Book");
        editButton = new JButton("Edit Selected");
        toggleStatusButton = new JButton("Toggle Status");
        
        // Set a consistent size for buttons
        Dimension buttonSize = new Dimension(120, 30);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        toggleStatusButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        toggleStatusButton.setMaximumSize(buttonSize);
        
        rightPanel.add(addButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        rightPanel.add(editButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        rightPanel.add(toggleStatusButton);
        
        add(rightPanel, BorderLayout.EAST);
    }

    private void initListeners() {
        // Add button listener
        addButton.addActionListener(e -> {
            // Here you would open a JDialog form to get new book details
            // For example: new BookFormDialog(bookController).setVisible(true);
            JOptionPane.showMessageDialog(this, "Add New Book dialog would open here.", "Info", JOptionPane.INFORMATION_MESSAGE);
            // After the dialog closes, refresh the data
            loadBookData();
        });

        // Edit button listener
        editButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedIsbn = (String) tableModel.getValueAt(selectedRow, 0);
            JOptionPane.showMessageDialog(this, "Edit dialog for ISBN: " + selectedIsbn + " would open here.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadBookData();
        });

        // Toggle Status button listener
        toggleStatusButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to change its status.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedIsbn = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to toggle the status for book with ISBN: " + selectedIsbn + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (bookController.toggleBookStatus(selectedIsbn)) {
                    loadBookData(); // Refresh table on success
                }
            }
        });
    }

    /**
     * Fetches the book data from the controller and populates the JTable.
     */
    private void loadBookData() {
        // Clear existing data
        tableModel.setRowCount(0);

        List<Book> books = bookController.getAllBooks();
        for (Book book : books) {
            Object[] rowData = {
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableCopies(),
                book.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(rowData);
        }
    }
}
