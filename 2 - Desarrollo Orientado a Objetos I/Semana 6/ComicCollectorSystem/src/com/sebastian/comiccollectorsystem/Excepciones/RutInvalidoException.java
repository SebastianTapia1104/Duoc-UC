package com.sebastian.comiccollectorsystem.Excepciones;

public class RutInvalidoException extends Exception {
    
    public RutInvalidoException(String mensaje) {
        super(mensaje);
    }
}