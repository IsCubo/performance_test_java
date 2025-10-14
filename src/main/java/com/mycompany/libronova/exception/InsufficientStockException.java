/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.exception;

/**
 *
 * @author Coder
 */
public class InsufficientStockException extends LibroNovaException {
    public InsufficientStockException() {
        super(ErrorCode.STOCK_INSUFFICIENT);
    }
}
