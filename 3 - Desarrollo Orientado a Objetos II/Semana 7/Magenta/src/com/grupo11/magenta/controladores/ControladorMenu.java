package com.grupo11.magenta.controladores;

import com.grupo11.magenta.modelos.Cartelera;
import com.grupo11.magenta.modelos.Pelicula;
import com.grupo11.magenta.vistas.VistaMenu;
import java.util.List;

public class ControladorMenu {
    private final Cartelera cartelera;
    private final VistaMenu vista;

    public ControladorMenu() {
        this.cartelera = new Cartelera();
        this.vista = new VistaMenu();
    }

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarOpciones();
            opcion = vista.leerOpcion();
            manejarOpcion(opcion);
        } while (opcion != 0);
    }

    private void manejarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                verCartelera();
                break;
            case 2:
                agregarPelicula();
                break;
            case 3:
                actualizarPelicula();
                break;
            case 4:
                eliminarPelicula();
                break;
            case 0:
                vista.mostrarMensaje("Saliendo del programa...");
                vista.cerrarScanner();
                break;
            default:
                vista.mostrarMensaje("Opción no válida. Intente de nuevo.");
                break;
        }
    }

    private void verCartelera() {
        List<Pelicula> peliculas = cartelera.obtenerPeliculas();
        vista.mostrarCartelera(peliculas);
    }

    private void agregarPelicula() {
        vista.mostrarMensaje("\n--- Agregar Nueva Película ---");
        Pelicula nuevaPelicula = vista.leerDatosPelicula();
        cartelera.crearPelicula(nuevaPelicula);
    }

    private void actualizarPelicula() {
        vista.mostrarMensaje("\n--- Actualizar Película ---");
        int id = vista.leerIdPelicula();
        Pelicula peliculaActualizada = vista.leerDatosPelicula();
        peliculaActualizada.setId(id);
        cartelera.actualizarPelicula(peliculaActualizada);
    }

    private void eliminarPelicula() {
        vista.mostrarMensaje("\n--- Eliminar Película ---");
        int id = vista.leerIdPelicula();
        cartelera.eliminarPelicula(id);
    }
}