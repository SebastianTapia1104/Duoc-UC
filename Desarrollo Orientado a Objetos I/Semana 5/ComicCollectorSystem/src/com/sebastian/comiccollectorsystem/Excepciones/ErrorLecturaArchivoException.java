package com.sebastian.comiccollectorsystem.Excepciones;

public class ErrorLecturaArchivoException extends Exception {
    
    public ErrorLecturaArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa); // Para dar la causa
    }

    public ErrorLecturaArchivoException(String mensaje) {
        super(mensaje);
    }
}