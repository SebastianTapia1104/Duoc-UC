package com.grupo11.magenta.vistas;

import com.grupo11.magenta.modelos.Pelicula;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaMenu {
    private final Scanner scanner;

    public VistaMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarOpciones() {
        System.out.println("\n--- Menú de Gestión de Cartelera ---");
        System.out.println("1. Ver cartelera");
        System.out.println("2. Agregar película");
        System.out.println("3. Actualizar película");
        System.out.println("4. Eliminar película");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            return opcion;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar el buffer
            return -1; // Valor para indicar un error
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarCartelera(List<Pelicula> peliculas) {
        if (peliculas.isEmpty()) {
            System.out.println("La cartelera está vacía.");
        } else {
            System.out.println("\n--- Cartelera Actual ---");
            for (Pelicula p : peliculas) {
                System.out.println(p);
            }
        }
    }

    public Pelicula leerDatosPelicula() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Director: ");
        String director = scanner.nextLine();
        System.out.print("Año: ");
        int anio = scanner.nextInt();
        System.out.print("Duración (en minutos): ");
        int duracion = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Género: ");
        String genero = scanner.nextLine();
        
        return new Pelicula(0, titulo, director, anio, duracion, genero);
    }

    public int leerIdPelicula() {
        System.out.print("ID de la película: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public void cerrarScanner() {
        scanner.close();
    }
}