package com.sebatapia.computec.decorator;

import com.sebatapia.computec.interfaces.Descuento;
import com.sebatapia.computec.modelos.Cliente;
import java.time.LocalDate;
import java.time.Period;

public class DescuentoTerceraEdad extends DescuentoDecorator {

    private final Cliente cliente;

    public DescuentoTerceraEdad(Descuento descuentoEnvuelto, Cliente cliente) {
        // Llama al constructor del padre para guardar la referencia al objeto envuelto
        super(descuentoEnvuelto); 
        this.cliente = cliente;
    }

    @Override
    public double calcularPrecioFinal(double precioBase) {
        // 1. Llama al método del padre, que a su vez llama al del objeto envuelto
        double precioCalculado = super.calcularPrecioFinal(precioBase);
        
        // 2. Aplica su propia lógica de descuento sobre ese resultado
        if (esTerceraEdad()) {
            double descuento = precioCalculado * 0.15; // 15% de descuento
            return precioCalculado - descuento;
        }
        return precioCalculado;
    }

    private boolean esTerceraEdad() {
        if (cliente == null || cliente.getFechaNacimiento() == null) {
            return false;
        }
        return Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears() >= 65;
    }
}