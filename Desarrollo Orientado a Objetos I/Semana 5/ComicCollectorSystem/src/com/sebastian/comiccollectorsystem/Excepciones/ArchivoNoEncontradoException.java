package com.sebastian.comiccollectorsystem.Excepciones;

public class ArchivoNoEncontradoException extends Exception {
    
    public ArchivoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}