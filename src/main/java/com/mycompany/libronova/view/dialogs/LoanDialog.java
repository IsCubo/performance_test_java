/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.dialogs;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.controller.LoanController;
import javax.swing.*;
import java.awt.*;

public class LoanDialog extends JDialog {

    private final LoanController loanController;

    // --- Componentes para Nuevo Préstamo ---
    private JTextField bookIsbnField;
    private JTextField memberIdField;
    private JButton processLoanButton;

    // --- Componentes para Registrar Devolución ---
    private JTextField loanIdField;
    private JButton processReturnButton;

    public LoanDialog(Frame parent, LoanController loanController) {
        super(parent, "Loan and Return Management", true); // Modal
        this.loanController = loanController;

        initComponents();
        initListeners();

        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Pestaña 1: Nuevo Préstamo ---
        JPanel newLoanPanel = new JPanel(new GridBagLayout());
        newLoanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcLoan = new GridBagConstraints();
        gbcLoan.insets = new Insets(5, 5, 5, 5);
        gbcLoan.fill = GridBagConstraints.HORIZONTAL;

        gbcLoan.gridx = 0; gbcLoan.gridy = 0;
        newLoanPanel.add(new JLabel("Book ISBN:"), gbcLoan);
        gbcLoan.gridx = 1; gbcLoan.gridy = 0; gbcLoan.weightx = 1.0;
        bookIsbnField = new JTextField(15);
        newLoanPanel.add(bookIsbnField, gbcLoan);

        gbcLoan.gridx = 0; gbcLoan.gridy = 1; gbcLoan.weightx = 0;
        newLoanPanel.add(new JLabel("Member ID:"), gbcLoan);
        gbcLoan.gridx = 1; gbcLoan.gridy = 1;
        memberIdField = new JTextField(15);
        newLoanPanel.add(memberIdField, gbcLoan);
        
        gbcLoan.gridy = 2; gbcLoan.gridx = 0; gbcLoan.gridwidth = 2;
        gbcLoan.fill = GridBagConstraints.NONE; gbcLoan.anchor = GridBagConstraints.CENTER;
        processLoanButton = new JButton("Process Loan");
        newLoanPanel.add(processLoanButton, gbcLoan);

        // --- Pestaña 2: Registrar Devolución ---
        JPanel returnPanel = new JPanel(new GridBagLayout());
        returnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcReturn = new GridBagConstraints();
        gbcReturn.insets = new Insets(5, 5, 5, 5);
        gbcReturn.fill = GridBagConstraints.HORIZONTAL;

        gbcReturn.gridx = 0; gbcReturn.gridy = 0;
        returnPanel.add(new JLabel("Loan ID:"), gbcReturn);
        gbcReturn.gridx = 1; gbcReturn.gridy = 0; gbcReturn.weightx = 1.0;
        loanIdField = new JTextField(15);
        returnPanel.add(loanIdField, gbcReturn);
        
        gbcReturn.gridy = 1; gbcReturn.gridx = 0; gbcReturn.gridwidth = 2;
        gbcReturn.fill = GridBagConstraints.NONE; gbcReturn.anchor = GridBagConstraints.CENTER;
        processReturnButton = new JButton("Register Return");
        returnPanel.add(processReturnButton, gbcReturn);

        // Añadir pestañas al panel
        tabbedPane.addTab("New Loan", newLoanPanel);
        tabbedPane.addTab("Register Return", returnPanel);

        // Añadir el JTabbedPane al diálogo
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initListeners() {
        processLoanButton.addActionListener(e -> {
            String isbn = bookIsbnField.getText().trim();
            String memberIdStr = memberIdField.getText().trim();

            if (isbn.isEmpty() || memberIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN and Member ID cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int memberId = Integer.parseInt(memberIdStr);
                if (loanController.performLoan(isbn, memberId)) {
                    dispose(); // Cierra el diálogo si la operación fue exitosa
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Member ID must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        processReturnButton.addActionListener(e -> {
            String loanIdStr = loanIdField.getText().trim();
            if (loanIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Loan ID cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int loanId = Integer.parseInt(loanIdStr);
                if (loanController.registerReturn(loanId)) {
                    dispose(); // Cierra el diálogo si la operación fue exitosa
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Loan ID must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
