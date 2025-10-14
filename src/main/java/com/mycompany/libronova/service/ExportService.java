/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service;

import java.nio.file.Path;

/**
 *
 * @author Coder
 */
/**
 * Service for exporting application data to files.
 */
public interface ExportService {

    /**
     * Exports the complete book catalog to a CSV file.
     * @param path The file path where the CSV will be saved.
     */
    void exportBooksToCsv(Path path);

    /**
     * Exports all overdue loans to a CSV file.
     * @param path The file path where the CSV will be saved.
     */
    void exportOverdueLoansToCsv(Path path);
}
