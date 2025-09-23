package com.test.sebastiantapia.ambiente;

import com.sebastiantapia.ambiente.modelos.Inventario;
import com.sebastiantapia.ambiente.modelos.Producto;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InventarioTest {

    private Inventario inventario;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        // Configura un nuevo inventario y productos antes de cada prueba
        inventario = new Inventario();
        producto1 = new Producto("P001", "Laptop", 1200.50, "Portátil de 15 pulgadas", 10);
        producto2 = new Producto("P002", "Mouse", 25.0, "Mouse inalámbrico", 50);
        inventario.agregarProducto(producto1);
        inventario.agregarProducto(producto2);
    }

    @Test
    void testAgregarProducto() {
        // Prueba para verificar que se agrega un producto al inventario
        Producto producto3 = new Producto("P003", "Teclado", 75.0, "Teclado mecánico", 30);
        inventario.agregarProducto(producto3);
        
        assertNotNull(inventario.buscarPorCodigo("P003"));
        assertEquals(3, inventario.listarTodosLosProductos().size());
    }

    @Test
    void testAgregarProductoNulo() {
        // Prueba de integración: verificar el manejo de productos nulos
        inventario.agregarProducto(null);
        // El tamaño del inventario no debería cambiar
        assertEquals(2, inventario.listarTodosLosProductos().size());
    }

    @Test
    void testEliminarProducto() {
        // Prueba para verificar la eliminación de un producto existente
        inventario.eliminarProducto("P001");
        
        assertNull(inventario.buscarPorCodigo("P001"));
        assertEquals(1, inventario.listarTodosLosProductos().size());
    }
    
    @Test
    void testEliminarProductoNoExistente() {
        // Prueba para verificar el caso de que se intente eliminar un producto no existente
        inventario.eliminarProducto("P999");
        
        // El tamaño del inventario no debería cambiar
        assertEquals(2, inventario.listarTodosLosProductos().size());
    }

    @Test
    void testBuscarPorNombre() {
        // Prueba para buscar productos por nombre (debería encontrar uno)
        List<Producto> resultados = inventario.buscarPorNombre("Laptop");
        
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals("P001", resultados.get(0).getCodigo());
    }

    @Test
    void testBuscarPorNombreSinResultados() {
        // Prueba para buscar por un nombre que no existe
        List<Producto> resultados = inventario.buscarPorNombre("Monitor");
        
        assertTrue(resultados.isEmpty());
    }

    @Test
    void testBuscarPorCodigo() {
        // Prueba para buscar un producto por su código
        Producto encontrado = inventario.buscarPorCodigo("P002");
        
        assertNotNull(encontrado);
        assertEquals("Mouse", encontrado.getNombre());
    }

    @Test
    void testBuscarPorCodigoNoExistente() {
        // Prueba para buscar un código que no existe
        Producto encontrado = inventario.buscarPorCodigo("P999");
        
        assertNull(encontrado);
    }
    
    @Test
    void testListarTodosLosProductos() {
        // Prueba para listar todos los productos en el inventario
        List<Producto> todos = inventario.listarTodosLosProductos();
        
        assertEquals(2, todos.size());
        assertTrue(todos.contains(producto1));
        assertTrue(todos.contains(producto2));
    }
}