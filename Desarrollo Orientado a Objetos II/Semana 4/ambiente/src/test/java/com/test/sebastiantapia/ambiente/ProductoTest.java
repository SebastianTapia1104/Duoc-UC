package com.test.sebastiantapia.ambiente;

import com.sebastiantapia.ambiente.modelos.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    void testCreacionProducto() {
        // Prueba de creación de un producto con todos los atributos
        Producto producto = new Producto("P001", "Laptop", 1200.50, "Portátil de 15 pulgadas", 10);
        
        // Verificaciones para asegurar que el constructor funciona correctamente
        assertEquals("P001", producto.getCodigo());
        assertEquals("Laptop", producto.getNombre());
        assertEquals(1200.50, producto.getPrecio(), 0.01);
        assertEquals("Portátil de 15 pulgadas", producto.getDescripcion());
        assertEquals(10, producto.getCantidadEnStock());
    }

    @Test
    void testActualizarPrecio() {
        // Prueba para verificar que el método actualizarPrecio funciona
        Producto producto = new Producto("P002", "Mouse", 25.0, "Mouse inalámbrico", 50);
        producto.actualizarPrecio(22.75);

        assertEquals(22.75, producto.getPrecio(), 0.01);
    }

    @Test
    void testSetters() {
        // Prueba para verificar que los métodos setter funcionan correctamente
        Producto producto = new Producto("P003", "Teclado", 75.0, "Teclado mecánico", 30);
        
        producto.setNombre("Teclado Gaming");
        producto.setPrecio(85.0);
        producto.setCantidadEnStock(25);

        assertEquals("Teclado Gaming", producto.getNombre());
        assertEquals(85.0, producto.getPrecio(), 0.01);
        assertEquals(25, producto.getCantidadEnStock());
    }

    @Test
    void testToString() {
        // Prueba para verificar el formato del método toString
        Producto producto = new Producto("P004", "Monitor", 300.0, "Monitor de 27 pulgadas", 5);
        String expectedString = "Código: P004, Nombre: Monitor, Descripción: Monitor de 27 pulgadas, Precio: $300.0, Cantidad en stock: 5";
        
        assertEquals(expectedString, producto.toString());
    }
}