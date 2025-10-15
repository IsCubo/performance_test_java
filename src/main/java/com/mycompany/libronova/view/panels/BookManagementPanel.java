package com.mycompany.libronova.view.panels;

import com.mycompany.libronova.controller.BookController;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.view.dialogs.BookFormDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A JPanel for managing the book catalog. It displays books in a table
 * and provides controls for filtering, adding, and editing books.
 */
public class BookManagementPanel extends JPanel {

    private final BookController bookController;
    private List<Book> allBooks; // Cache for local search functionality

    // --- UI Components ---
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, toggleStatusButton, searchButton, clearButton;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    public BookManagementPanel(BookController bookController) {
        this.bookController = bookController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        initListeners();
        loadBookData();
    }

    private void initComponents() {
        // --- Top Panel (Toolbar with filters and search) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        topPanel.add(new JLabel("Filter by:"));
        filterComboBox = new JComboBox<>(new String[]{"Title", "Author", "Category", "ISBN"});
        topPanel.add(filterComboBox);
        
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        
        searchButton = new JButton("Search");
        topPanel.add(searchButton);
        
        clearButton = new JButton("Clear");
        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel (Table with books) ---
        String[] columnNames = {"ISBN", "Title", "Author", "Category", "Available Copies", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Right Panel (Action Buttons) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        addButton = new JButton("Add New Book");
        editButton = new JButton("Edit Selected");
        toggleStatusButton = new JButton("Toggle Status");
        
        Dimension buttonSize = new Dimension(120, 30);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        toggleStatusButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        toggleStatusButton.setMaximumSize(buttonSize);
        
        rightPanel.add(addButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(editButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(toggleStatusButton);
        
        add(rightPanel, BorderLayout.EAST);
    }

    private void initListeners() {
        // Add button listener
        addButton.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            BookFormDialog dialog = new BookFormDialog(parentFrame, bookController);
            dialog.setVisible(true);
            loadBookData(); // Reload table after the dialog is closed
        });

        // Edit button listener
        editButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String isbn = (String) tableModel.getValueAt(selectedRow, 0);

            // <<< CORRECCIÓN: El nombre del método es 'getBookByIsbn', no 'get'.
            bookController.getBookByIsbn(isbn).ifPresent(bookToEdit -> {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
                BookFormDialog dialog = new BookFormDialog(parentFrame, bookController, bookToEdit);
                dialog.setVisible(true);
                loadBookData(); // Reload table
            });
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

            if (confirm == JOptionPane.YES_OPTION && bookController.toggleBookStatus(selectedIsbn)) {
                loadBookData();
            }
        });
        
        // Search and Clear listeners
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            populateTable(this.allBooks); // Restore the full table
        });
    }

    private void loadBookData() {
        this.allBooks = bookController.getAllBooks(); // Load and cache all books
        populateTable(this.allBooks); // Display all books in the table
    }

    private void populateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            Object[] rowData = {
                book.getIsbn(), book.getTitle(), book.getAuthor(),
                book.getCategory(), book.getAvailableCopies(),
                book.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(rowData);
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        String filterType = (String) filterComboBox.getSelectedItem();

        if (searchTerm.isEmpty()) {
            populateTable(this.allBooks);
            return;
        }

        List<Book> filteredBooks = this.allBooks.stream()
            .filter(book -> {
                // Defensive null check for category, in case a book has no category
                String category = book.getCategory() != null ? book.getCategory().toLowerCase() : "";
                
                switch (filterType) {
                    case "Title":    return book.getTitle().toLowerCase().contains(searchTerm);
                    case "Author":   return book.getAuthor().toLowerCase().contains(searchTerm);
                    case "Category": return category.contains(searchTerm);
                    case "ISBN":     return book.getIsbn().contains(searchTerm);
                    default:         return false;
                }
            })
            .collect(Collectors.toList());
        
        populateTable(filteredBooks);
    }
}