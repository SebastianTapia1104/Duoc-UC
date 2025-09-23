package com.sebastian.biblioteca_duoc_uc.Excepciones;

public class EntradaInvalidaException extends Exception {

    public EntradaInvalidaException(String mensaje) {
        super(mensaje);
    }

    public EntradaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}