/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.util;

import java.io.IOException;
import java.util.logging.*;

/**
 *
 * @author Coder
 */
public class LoggerManager {
    private static final Logger logger = Logger.getLogger("LibroNovaLogger");

    static {
        try {
            // Prevent duplicate logs in the console
            logger.setUseParentHandlers(false);
            
            // Handler for the app.log filelog
            FileHandler fileHandler = new FileHandler("app.log", true); // true para append
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Handler for the console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            logger.setLevel(Level.INFO);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "No se pudo inicializar el Logger.", e);
        }
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
    
    public static void log(Level level, String message, Throwable throwable) {
        logger.log(level, message, throwable);
    }
    
    // HTTP trace simulation
    public static void logHttpRequest(String method, String path, String status) {
        String logMessage = String.format("HTTP TRACE :: Method: %s, Path: %s, Status: %s", method, path, status);
        logger.log(Level.INFO, logMessage);
    }
}
