package com.sebatapia.computec.test;

import com.sebatapia.computec.decorator.DescuentoTerceraEdad;
import com.sebatapia.computec.decorator.PrecioBase;
import com.sebatapia.computec.interfaces.Descuento;
import com.sebatapia.computec.modelos.Cliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class DescuentoDecoratorTest {

    @Test
    public void testDescuentoTerceraEdadSeAplicaCorrectamente() {
        // --- 1. Preparación (Arrange) ---
        // Cliente que SÍ es de la tercera edad (más de 65 años)
        Cliente clienteMayor = new Cliente();
        clienteMayor.setFechaNacimiento(LocalDate.now().minusYears(70));

        double precioOriginal = 100000.0;
        // Se espera un 15% de descuento: 100000 - 15000 = 85000
        double precioEsperado = 85000.0;

        // --- 2. Actuación (Act) ---
        // Se "decora" el precio base con el descuento
        Descuento calculo = new PrecioBase();
        calculo = new DescuentoTerceraEdad(calculo, clienteMayor);
        double precioFinal = calculo.calcularPrecioFinal(precioOriginal);

        // --- 3. Afirmación (Assert) ---
        assertEquals(precioEsperado, precioFinal, "El descuento del 15% para tercera edad no se aplicó correctamente.");
    }

    @Test
    public void testDescuentoTerceraEdadNoSeAplicaSiNoCorresponde() {
        // --- 1. Preparación (Arrange) ---
        // Cliente que NO es de la tercera edad
        Cliente clienteJoven = new Cliente();
        clienteJoven.setFechaNacimiento(LocalDate.now().minusYears(30));

        double precioOriginal = 100000.0;
        // Se espera que el precio no cambie
        double precioEsperado = 100000.0;

        // --- 2. Actuación (Act) ---
        Descuento calculo = new PrecioBase();
        calculo = new DescuentoTerceraEdad(calculo, clienteJoven);
        double precioFinal = calculo.calcularPrecioFinal(precioOriginal);

        // --- 3. Afirmación (Assert) ---
        assertEquals(precioEsperado, precioFinal, "El descuento se aplicó incorrectamente a un cliente que no es de tercera edad.");
    }
}