package com.sebastian.comiccollectorsystem.Excepciones;

public class ComicNoEncontradoException extends Exception {

    public ComicNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}