// package com.sebatapia.computec.decorator;
package com.sebatapia.computec.decorator;

import com.sebatapia.computec.interfaces.Descuento;

/**
 * Componente Concreto: Es el objeto base al que se le añadirán decoradores.
 * No aplica ningún descuento, solo devuelve el precio original.
 */
public class PrecioBase implements Descuento {

    @Override
    public double calcularPrecioFinal(double precioBase) {
        return precioBase;
    }
}