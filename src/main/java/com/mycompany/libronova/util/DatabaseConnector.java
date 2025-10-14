/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.util;

import com.mycompany.libronova.exception.DataAccessException;
import com.mycompany.libronova.exception.ErrorCode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Coder
 */
public class DatabaseConnector {
    private static final String URL = PropertiesLoader.getProperty("db.url");
    private static final String USER = PropertiesLoader.getProperty("db.user");
    private static final String PASSWORD = PropertiesLoader.getProperty("db.password");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error de conexión a la BD: " + e.getMessage());
            throw new DataAccessException(ErrorCode.DB_CONNECTION, e);
        }
    }
}
