package com.sebastian.comiccollectorsystem.Excepciones;

public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
    public UsuarioNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}