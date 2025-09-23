package com.sebastian.comiccollectorsystem.Excepciones;

public class ComicYaDisponibleException extends Exception {
    public ComicYaDisponibleException(String message) {
        super(message);
    }
    public ComicYaDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}