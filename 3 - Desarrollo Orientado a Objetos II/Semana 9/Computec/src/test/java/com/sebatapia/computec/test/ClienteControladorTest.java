package com.sebatapia.computec.test;

import com.sebatapia.computec.controladores.ClienteControlador;
import com.sebatapia.computec.modelos.Cliente;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * Pruebas unitarias para las operaciones CRUD del ClienteControlador.
 * IMPORTANTE: Requiere que la base de datos esté activa.
 */
public class ClienteControladorTest {

    private static ClienteControlador clienteControlador;
    private static Cliente clienteDePrueba;

    @BeforeAll
    public static void setUp() {
        // Se ejecuta una vez antes de todas las pruebas
        clienteControlador = new ClienteControlador();
        clienteDePrueba = new Cliente(
            "99.999.999-9", "Usuario De Prueba", "Calle Test 123",
            "Test-Comuna", "test@test.com", "999999999", LocalDate.of(2000, 1, 1)
        );
    }

    @Test
    public void testRegistrarYBuscarCliente() {
        // 1. Registrar
        boolean registroExitoso = clienteControlador.registrarCliente(clienteDePrueba);
        assertTrue(registroExitoso, "El registro del cliente de prueba falló.");

        // 2. Buscar
        Cliente clienteEncontrado = clienteControlador.buscarClientePorRut(clienteDePrueba.getRut());
        assertNotNull(clienteEncontrado, "No se encontró el cliente recién registrado.");
        assertEquals("Usuario De Prueba", clienteEncontrado.getNombreCompleto(), "El nombre del cliente encontrado no coincide.");
    }
    
    @AfterAll
    public static void tearDown() {
        // Se ejecuta una vez después de todas las pruebas para limpiar
        if (clienteControlador != null) {
            clienteControlador.eliminarCliente("99.999.999-9");
            System.out.println("Cliente de prueba eliminado.");
        }
    }
}