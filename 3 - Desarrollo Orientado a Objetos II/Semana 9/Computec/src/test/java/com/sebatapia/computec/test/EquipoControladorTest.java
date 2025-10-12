package com.sebatapia.computec.test;

import com.sebatapia.computec.controladores.EquipoControlador;
import com.sebatapia.computec.modelos.Desktop;
import com.sebatapia.computec.modelos.Equipo;
import com.sebatapia.computec.modelos.Laptop;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para las operaciones CRUD del EquipoControlador.
 * IMPORTANTE: Requiere que la base de datos esté activa.
 */
public class EquipoControladorTest {

    private static EquipoControlador equipoControlador;
    private static Laptop laptopDePrueba;
    private static Desktop desktopDePrueba;

    @BeforeAll
    public static void setUp() {
        equipoControlador = new EquipoControlador();
        laptopDePrueba = new Laptop(
            "LaptopTest-001", "CPU Test", 512, 16, 500000,
            14.0, true, 3
        );
        desktopDePrueba = new Desktop(
            "DesktopTest-002", "CPU Test", 1024, 32, 800000,
            "750W", "ATX"
        );
    }

    @Test
    public void testRegistrarYBuscarEquipos() {
        // --- Prueba de Laptop ---
        boolean registroLaptopExitoso = equipoControlador.registrarEquipo(laptopDePrueba);
        assertTrue(registroLaptopExitoso, "El registro del laptop de prueba falló.");
        // El ID se autogenera, verificamos que sea mayor a 0
        assertTrue(laptopDePrueba.getIdEquipo() > 0, "El ID del laptop no fue asignado después del registro.");

        Equipo equipoEncontrado1 = equipoControlador.buscarEquipoPorId(laptopDePrueba.getIdEquipo());
        assertNotNull(equipoEncontrado1, "No se encontró el laptop recién registrado.");
        assertTrue(equipoEncontrado1 instanceof Laptop, "El equipo encontrado debería ser una instancia de Laptop.");
        assertEquals("LaptopTest-001", equipoEncontrado1.getModelo());

        // --- Prueba de Desktop ---
        boolean registroDesktopExitoso = equipoControlador.registrarEquipo(desktopDePrueba);
        assertTrue(registroDesktopExitoso, "El registro del desktop de prueba falló.");
        assertTrue(desktopDePrueba.getIdEquipo() > 0, "El ID del desktop no fue asignado después del registro.");

        Equipo equipoEncontrado2 = equipoControlador.buscarEquipoPorId(desktopDePrueba.getIdEquipo());
        assertNotNull(equipoEncontrado2, "No se encontró el desktop recién registrado.");
        assertTrue(equipoEncontrado2 instanceof Desktop, "El equipo encontrado debería ser una instancia de Desktop.");
        assertEquals("DesktopTest-002", equipoEncontrado2.getModelo());
    }

    @AfterAll
    public static void tearDown() {
        if (equipoControlador != null) {
            if (laptopDePrueba.getIdEquipo() > 0) {
                equipoControlador.eliminarEquipo(laptopDePrueba.getIdEquipo());
            }
            if (desktopDePrueba.getIdEquipo() > 0) {
                equipoControlador.eliminarEquipo(desktopDePrueba.getIdEquipo());
            }
            System.out.println("Equipos de prueba eliminados.");
        }
    }
}