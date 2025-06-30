package com.sebastian.comiccollectorsystem;

import com.sebastian.comiccollectorsystem.Excepciones.ArchivoNoEncontradoException;
import com.sebastian.comiccollectorsystem.Excepciones.ErrorLecturaArchivoException;
import com.sebastian.comiccollectorsystem.servicios.ServicioComicCollectorSystem;
import com.sebastian.comiccollectorsystem.servicios.ServicioInteraccionUsuario;
import java.util.Scanner;

public class main {

    private static final String RUTA_PRESTAMOS = "src/com/sebastian/comiccollectorsystem/resources/prestamos.txt";
    private static final String RUTA_USUARIOS = "src/com/sebastian/comiccollectorsystem/resources/usuarios.txt";
    private static final String RUTA_COMICS_CSV = "src/com/sebastian/comiccollectorsystem/resources/comics.csv";

    public static void main(String[] args) {
        // 1. Inicializar el servicio principal del sistema
        ServicioComicCollectorSystem comicSystem = new ServicioComicCollectorSystem(RUTA_PRESTAMOS, RUTA_USUARIOS);
        // 2. Cargar los cómics desde el archivo CSV al inicio
        try {
            comicSystem.cargarComicsDesdeCSV(RUTA_COMICS_CSV);
        } catch (ArchivoNoEncontradoException | ErrorLecturaArchivoException e) {
            System.err.println("Error crítico al cargar cómics: " + e.getMessage());
            System.err.println("El programa se cerrará.");
            return; // Terminar la ejecución si no se pueden cargar los cómics
        }
        // 3. Inicializar el Scanner para la entrada del usuario
        Scanner sc = new Scanner(System.in);
        // 4. Inicializar el nuevo servicio de interacción con el usuario y ejecutarlo
        ServicioInteraccionUsuario servicioUI = new ServicioInteraccionUsuario(comicSystem, sc);
        servicioUI.ejecutarSistema(); // Inicia el bucle principal del menú
        // 5. Cerrar el Scanner al finalizar el programa
        sc.close();
    }
}