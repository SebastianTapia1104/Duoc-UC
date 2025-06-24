package com.sebastian.biblioteca_duoc_uc.Excepciones;

public class ErrorLecturaArchivoException extends Exception {
    public ErrorLecturaArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa); // Para dar la causa
    }

    public ErrorLecturaArchivoException(String mensaje) {
        super(mensaje);
    }
}