/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.controller;

import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.service.LoanService;
import com.mycompany.libronova.util.LoggerManager;
import com.mycompany.libronova.view.dialogs.LoanDialog;
import java.awt.Frame;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    public boolean performLoan(String bookIsbn, int memberId) {
        try {
            loanService.performLoan(bookIsbn, memberId);
            showSuccessMessage("Loan successfully processed for book ISBN: " + bookIsbn);
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Loan process failed for book " + bookIsbn);
            return false;
        }
    }

    public boolean registerReturn(int loanId) {
        try {
            loanService.registerReturn(loanId);
            showSuccessMessage("Return successfully processed for loan ID: " + loanId);
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Return process failed for loan ID " + loanId);
            return false;
        }
    }

    private void handleError(LibroNovaException ex, String logMessage) {
        LoggerManager.log(Level.WARNING, logMessage + " Reason: " + ex.getMessage(), ex);
        JOptionPane.showMessageDialog(null,
                "Operation failed: " + ex.getErrorCode().getMessage(),
                "Validation Error - Code: " + ex.getErrorCode().getCode(),
                JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Crea y muestra el diálogo modal para gestionar préstamos y devoluciones.
     *
     * @param parent El Frame que será el padre del diálogo (para que sea modal
     * sobre él).
     */
    public void showLoanDialog(Frame parent) {
        LoanDialog dialog = new LoanDialog(parent, this);
        dialog.setVisible(true);
    }

    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    public List<Loan> getLoansByMemberId(int memberId) {
        return loanService.getLoansByMemberId(memberId);
    }

}
