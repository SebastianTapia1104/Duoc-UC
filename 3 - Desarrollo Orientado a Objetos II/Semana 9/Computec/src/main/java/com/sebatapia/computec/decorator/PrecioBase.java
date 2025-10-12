package com.sebatapia.computec.decorator;

import com.sebatapia.computec.interfaces.Descuento;

public class PrecioBase implements Descuento {

    @Override
    public double calcularPrecioFinal(double precioBase) {
        return precioBase;
    }
}