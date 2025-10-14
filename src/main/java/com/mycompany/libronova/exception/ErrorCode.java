/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.exception;

/**
 *
 * @author Coder
 */
public enum ErrorCode {
    // Validation errors (4xx)
    ISBN_DUPLICATE(4001, "El ISBN ya existe.", "WARN"),
    STOCK_INSUFFICIENT(4002, "No hay ejemplares disponibles.", "WARN"),
    PARTNER_INACTIVE(4003, "El socio no se encuentra activo.", "WARN"),
    INVALID_DATA(4004, "Los datos proporcionados son inválidos.", "WARN"),
    INVALID_CREDENTIALS(4011, "Usuario o contraseña incorrectos.", "INFO"),
    MEMBER_DUPLICATE(4005, "El miebro ya existe", "WARM"),

    // Data access errors (5xx)
    DB_CONNECTION(5001, "No se pudo conectar a la base de datos.", "ERROR"),
    QUERY_ERROR(5002, "Error al ejecutar la consulta SQL.", "ERROR"),
    TRANSACTION_ERROR(5003, "Error durante la transacción.", "ERROR"),

    // File errors (6xx)
    READ_CONFIG_ERROR(6001, "No se pudo leer el archivo de configuración.", "FATAL"),
    CSV_EXPORT_ERROR(6002, "Ocurrió un error al exportar a CSV.", "ERROR");

    private final int code;
    private final String message;
    private final String severity; // INFO, WARN, ERROR, FATAL

    ErrorCode(int code, String message, String severity) {
        this.code = code;
        this.message = message;
        this.severity = severity;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }
}
