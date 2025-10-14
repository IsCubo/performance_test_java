/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.util;

import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.LibroNovaException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Coder
 */
public class PropertiesLoader {
    private static final Properties PROPERTIES = new Properties();
    
    static {
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
            } else {    
                throw new LibroNovaException(ErrorCode.READ_CONFIG_ERROR);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la configuración de la base de datos: " + e.getMessage());
            throw new LibroNovaException(ErrorCode.READ_CONFIG_ERROR); // Termina la aplicación si esto falla
        }
    }
    
    /**
     * Gets a new connection to the database.
     * @param key
     * @return An active connection to the database.
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
