/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.exception;

/**
 *
 * @author Coder
 */
public class DataAccessException extends LibroNovaException {
    public DataAccessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
