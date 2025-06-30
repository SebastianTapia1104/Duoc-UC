package com.sebastian.comiccollectorsystem.Excepciones;

public class ComicNoDisponibleException extends Exception {
    public ComicNoDisponibleException(String message) {
        super(message);
    }
    public ComicNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}