package com.sebastian.comiccollectorsystem.Excepciones;

public class UsuarioYaRegistradoException extends Exception {
    public UsuarioYaRegistradoException(String message) {
        super(message);
    }
    public UsuarioYaRegistradoException(String message, Throwable cause) {
        super(message, cause);
    }
}