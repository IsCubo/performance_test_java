/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.dao.LoanDAO;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.service.ExportService;
import com.mycompany.libronova.util.CsvExporter;
import com.mycompany.libronova.util.LoggerManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Coder
 */
/**
 * CORRECTED implementation of the ExportService.
 * This class orchestrates the export process by fetching data and delegating
 * the file-writing task to the CsvExporter utility.
 */
public class ExportServiceImpl implements ExportService {

    private final BookDAO bookDAO;
    private final LoanDAO loanDAO;

    public ExportServiceImpl(BookDAO bookDAO, LoanDAO loanDAO) {
        this.bookDAO = bookDAO;
        this.loanDAO = loanDAO;
    }

    /**
     * Exports the complete book catalog to a CSV file.
     * It fetches the data and then calls the CsvExporter utility to do the writing.
     * @param path The file path where the CSV will be saved.
     */
    @Override
    public void exportBooksToCsv(Path path) {
        try {
            List<Book> books = bookDAO.findAll();

            CsvExporter.exportBooks(path, books); 

            LoggerManager.log(Level.INFO, "Book catalog successfully exported to " + path);

        } catch (IOException e) {
            LoggerManager.log(Level.SEVERE, "Failed to export books to CSV", e);
            throw new LibroNovaException(ErrorCode.CSV_EXPORT_ERROR, e);
        }
    }

    /**
     * Exports all overdue loans to a CSV file.
     * It fetches the data and then calls the CsvExporter utility to do the writing.
     * @param path The file path where the CSV will be saved.
     */
    @Override
    public void exportOverdueLoansToCsv(Path path) {
        try {
            // Responsabilidad del Servicio: Obtener los datos de negocio.
            List<Loan> loans = loanDAO.findOverdueLoans();

            // Responsabilidad del Servicio: Delegar la tarea técnica a la herramienta.
            CsvExporter.exportOverdueLoans(path, loans); // <-- PASO 2: Usar la herramienta

            LoggerManager.log(Level.INFO, "Overdue loans successfully exported to " + path);
            
        } catch (IOException e) {
            // Si la herramienta (CsvExporter) falla, el servicio lo captura y lo maneja.
            LoggerManager.log(Level.SEVERE, "Failed to export overdue loans to CSV", e);
            throw new LibroNovaException(ErrorCode.CSV_EXPORT_ERROR, e);
        }
    }
}
