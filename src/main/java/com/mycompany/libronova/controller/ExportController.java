/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.controller;

import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.service.ExportService;
import com.mycompany.libronova.util.LoggerManager;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Coder
 */
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    public void exportBooks() {
        JFileChooser fileChooser = createFileChooser("books_export.csv");
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path path = fileChooser.getSelectedFile().toPath();
                exportService.exportBooksToCsv(path);
                showSuccessMessage("Book catalog exported successfully to:\n" + path);
            } catch (LibroNovaException ex) {
                handleError(ex, "Failed to export books.");
            }
        }
    }

    public void exportOverdueLoans() {
        JFileChooser fileChooser = createFileChooser("prestamos_vencidos.csv");
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path path = fileChooser.getSelectedFile().toPath();
                exportService.exportOverdueLoansToCsv(path);
                showSuccessMessage("Overdue loans exported successfully to:\n" + path);
            } catch (LibroNovaException ex) {
                handleError(ex, "Failed to export overdue loans.");
            }
        }
    }

    private JFileChooser createFileChooser(String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));
        return fileChooser;
    }

    private void handleError(LibroNovaException ex, String logMessage) {
        LoggerManager.log(Level.SEVERE, logMessage + " Error: " + ex.getMessage(), ex);
        JOptionPane.showMessageDialog(null,
            "Export failed: " + ex.getErrorCode().getMessage(),
            "Export Error - Code: " + ex.getErrorCode().getCode(),
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Export Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}
