/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.dialogs;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.controller.BookController;
import com.mycompany.libronova.model.Book;
import javax.swing.*;
import java.awt.*;

/**
 * A modal JDialog for adding a new book or editing an existing one.
 */
public class BookFormDialog extends JDialog {

    private final BookController bookController;
    private final boolean isEditMode;
    private Book bookToEdit; // Holds the book instance when in edit mode

    // --- UI Components ---
    private JTextField isbnField, titleField, authorField, categoryField;
    private JSpinner totalCopiesSpinner, availableCopiesSpinner, priceSpinner;
    private JCheckBox activeCheckBox;
    private JButton saveButton, cancelButton;

    /**
     * Constructor for ADD mode.
     */
    public BookFormDialog(Frame parent, BookController bookController) {
        super(parent, "Add New Book", true); // true for modal
        this.bookController = bookController;
        this.isEditMode = false;
        initComponents();
    }

    /**
     * Constructor for EDIT mode.
     */
    public BookFormDialog(Frame parent, BookController bookController, Book bookToEdit) {
        super(parent, "Edit Book", true); // true for modal
        this.bookController = bookController;
        this.isEditMode = true;
        this.bookToEdit = bookToEdit;
        initComponents();
        populateForm();
    }

    private void initComponents() {
        // --- Form Panel using GridBagLayout for alignment ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: ISBN
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        isbnField = new JTextField(20);
        formPanel.add(isbnField, gbc);

        // Row 1: Title
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField();
        formPanel.add(titleField, gbc);
        
        // Row 2: Author
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        authorField = new JTextField();
        formPanel.add(authorField, gbc);

        // Row 3: Category
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        categoryField = new JTextField();
        formPanel.add(categoryField, gbc);

        // Row 4: Copies
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        totalCopiesSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
        formPanel.add(totalCopiesSpinner, gbc);

        // Row 5: Price
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        priceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.5));
        formPanel.add(priceSpinner, gbc);

        // Row 6: Status
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        activeCheckBox = new JCheckBox("Active", true);
        formPanel.add(activeCheckBox, gbc);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // --- Add panels to dialog ---
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        initListeners();
        pack(); // Adjust window size to fit components
        setLocationRelativeTo(getParent());
    }

    private void populateForm() {
        if (bookToEdit != null) {
            isbnField.setText(bookToEdit.getIsbn());
            isbnField.setEditable(false); // ISBN is a primary key, should not be changed
            titleField.setText(bookToEdit.getTitle());
            authorField.setText(bookToEdit.getAuthor());
            categoryField.setText(bookToEdit.getCategory());
            totalCopiesSpinner.setValue(bookToEdit.getTotalCopies());
            priceSpinner.setValue(bookToEdit.getReferencePrice());
            activeCheckBox.setSelected(bookToEdit.isActive());
        }
    }

    private void initListeners() {
        saveButton.addActionListener(e -> saveBook());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveBook() {
        // --- Basic Validation ---
        if (isbnField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ISBN, Title, and Author fields cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Collect data from form ---
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        int totalCopies = (int) totalCopiesSpinner.getValue();
        double price = (double) priceSpinner.getValue();
        boolean isActive = activeCheckBox.isSelected();

        // --- Call Controller ---
        if (isEditMode) {
            // Update existing book object
            bookToEdit.setTitle(title);
            bookToEdit.setAuthor(author);
            bookToEdit.setCategory(category);
            bookToEdit.setTotalCopies(totalCopies);
            bookToEdit.setReferencePrice(price);
            bookToEdit.setActive(isActive);
            // Available copies might have changed due to loans, so we don't reset it here
            // unless a specific business rule requires it.

            if (bookController.updateBook(bookToEdit)) {
                dispose(); // Close dialog on success
            }
        } else {
            // Create a new book object
            Book newBook = new Book();
            newBook.setIsbn(isbn);
            newBook.setTitle(title);
            newBook.setAuthor(author);
            newBook.setCategory(category);
            newBook.setTotalCopies(totalCopies);
            newBook.setReferencePrice(price);
            // The service will set createdAt, isActive, and availableCopies on creation
            
            if (bookController.createBook(newBook)) {
                dispose(); // Close dialog on success
            }
        }
    }
}
