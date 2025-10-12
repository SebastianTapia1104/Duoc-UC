package com.sebatapia.computec.decorator;

import com.sebatapia.computec.interfaces.Descuento;

public abstract class DescuentoDecorator implements Descuento {
    
    // Referencia al componente "envuelto"
    protected Descuento descuentoEnvuelto;

    public DescuentoDecorator(Descuento descuentoEnvuelto) {
        this.descuentoEnvuelto = descuentoEnvuelto;
    }

    @Override
    public double calcularPrecioFinal(double precioBase) {
        return descuentoEnvuelto.calcularPrecioFinal(precioBase);
    }
}