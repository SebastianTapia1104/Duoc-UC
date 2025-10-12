// package com.sebatapia.computec.decorator;
package com.sebatapia.computec.decorator;

import com.sebatapia.computec.interfaces.Descuento;

/**
 * Decorador Abstracto: Mantiene una referencia a un objeto Componente (Descuento)
 * y define una interfaz que se ajusta a la del Componente.
 */
public abstract class DescuentoDecorator implements Descuento {
    
    // Referencia al componente "envuelto"
    protected Descuento descuentoEnvuelto;

    public DescuentoDecorator(Descuento descuentoEnvuelto) {
        this.descuentoEnvuelto = descuentoEnvuelto;
    }

    /**
     * El trabajo del decorador es delegar la llamada al componente envuelto.
     * Las clases hijas (decoradores concretos) añadirán su propia lógica aquí.
     */
    @Override
    public double calcularPrecioFinal(double precioBase) {
        return descuentoEnvuelto.calcularPrecioFinal(precioBase);
    }
}