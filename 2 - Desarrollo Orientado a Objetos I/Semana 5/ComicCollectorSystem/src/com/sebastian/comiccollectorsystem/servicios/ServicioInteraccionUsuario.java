package com.sebastian.comiccollectorsystem.servicios;

import com.sebastian.comiccollectorsystem.Excepciones.ComicNoDisponibleException;
import com.sebastian.comiccollectorsystem.Excepciones.ComicNoEncontradoException;
import com.sebastian.comiccollectorsystem.Excepciones.ComicYaDisponibleException;
import com.sebastian.comiccollectorsystem.Excepciones.EntradaInvalidaException;
import com.sebastian.comiccollectorsystem.Excepciones.RutInvalidoException;
import com.sebastian.comiccollectorsystem.Excepciones.UsuarioNoEncontradoException;
import com.sebastian.comiccollectorsystem.Excepciones.UsuarioYaRegistradoException;
import com.sebastian.comiccollectorsystem.modelos.usuarios.Usuario;
import java.util.Scanner;

public class ServicioInteraccionUsuario {

    private final ServicioComicCollectorSystem comicSystem;
    private final Scanner sc;
    private Usuario usuarioActual; // Usuario logeado actualmente

    public ServicioInteraccionUsuario(ServicioComicCollectorSystem comicSystem, Scanner sc) {
        this.comicSystem = comicSystem;
        this.sc = sc;
        this.usuarioActual = null; // Nadie logeado al inicio
    }

    public void ejecutarSistema() {
        int opcion;
        do {
            if (usuarioActual == null) {
                // Menú para usuarios no logeados
                System.out.println("\n--- Bienvenido al ComicCollectorSystem DUOC UC ---");
                System.out.println("1. Registrar nuevo usuario");
                System.out.println("2. Identificarse");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                try {
                    opcion = ServicioValidadores.leerEntero(sc);
                    switch (opcion) {
                        case 1 -> registrarUsuario();
                        case 2 -> identificarUsuario();
                        case 3 -> System.out.println("👋 Cerrando sistema...");
                        default -> System.out.println("Opción no válida.");
                    }
                } catch (EntradaInvalidaException e) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número: " + e.getMessage());
                    opcion = 0; // Para mantener el bucle
                }
            } else {
                // Menú para usuarios logeados
                System.out.println("\n--- Menú de Usuario (" + usuarioActual.getNombre() + ") ---");
                System.out.println("1. Consultar disponibilidad de cómic");
                System.out.println("2. Prestar cómic");
                System.out.println("3. Devolver cómic");
                System.out.println("4. Listar cómics");
                System.out.println("5. Listar usuarios");
                System.out.println("6. Listar cómics ordenados alfabéticamente");
                System.out.println("7. Listar usuarios ordenados alfabéticamente");
                System.out.println("8. Mostrar cómics únicos, sin duplicado.");
                System.out.println("9. Cerrar sesión");
                System.out.print("Seleccione una opción: ");
                try {
                    opcion = ServicioValidadores.leerEntero(sc);
                    switch (opcion) {
                        case 1 -> consultarDisponibilidadComic();
                        case 2 -> prestarComic();
                        case 3 -> devolverComic();
                        case 4 -> comicSystem.listarComics();
                        case 5 -> comicSystem.listarUsuarios();
                        case 6 -> comicSystem.listarCatalogoOrdenado();
                        case 7 -> comicSystem.listarUsuariosOrdenados();
                        case 8 -> comicSystem.mostrarComicsUnicos();
                        case 9 -> {
                            usuarioActual = null; // Cerrar sesión
                            System.out.println("Sesión cerrada.");
                        }
                        default -> System.out.println("Opción no válida.");
                    }
                } catch (EntradaInvalidaException e) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número: " + e.getMessage());
                    opcion = 0; // Para mantener el bucle
                }
            }
        } while (opcion != 3 || usuarioActual != null); // Salir solo si se selecciona 3 y no hay sesión activa
    }

    private void registrarUsuario() {
        System.out.print("Ingrese RUT (sin dígito verificador, ej: 12345678): ");
        String rut = sc.nextLine();
        try {
            ServicioValidadores.validarRut(rut);
        } catch (RutInvalidoException e) {
            System.out.println("❌ " + e.getMessage());
            return;
        }
        System.out.print("Ingrese nombre del usuario: ");
        String nombre = sc.nextLine();
        if (!ServicioValidadores.esNombreValido(nombre)) {
            System.out.println("Nombre inválido, debe tener mínimo 3 letras y solo caracteres alfabéticos.");
            return;
        }
        Usuario nuevoUsuario = new Usuario(nombre, rut);
        try {
            comicSystem.registrarUsuario(nuevoUsuario);
            System.out.println("Usuario registrado exitosamente.");
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void identificarUsuario() {
        System.out.print("Ingrese su RUT para identificarse: ");
        String rut = sc.nextLine();
        try {
            usuarioActual = comicSystem.identificarUsuarioExistente(rut);
            System.out.println("¡Bienvenido, " + usuarioActual.getNombre() + "!");
        } catch (UsuarioNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void consultarDisponibilidadComic() {
        System.out.print("Ingrese ID del cómic: ");
        String idComic = sc.nextLine();
        try {
            String info = comicSystem.consultarDisponibilidadComic(idComic);
            System.out.println(info);
        } catch (ComicNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void prestarComic() {
        System.out.print("Ingrese ID del cómic a prestar: ");
        comicSystem.listarComics();
        String idComic = sc.nextLine();
        try {
            comicSystem.prestarComic(usuarioActual.getRut(), idComic);
            System.out.println("✅ Cómic prestado exitosamente.");
        } catch (ComicNoEncontradoException | ComicNoDisponibleException | UsuarioNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void devolverComic() {
        System.out.print("Ingrese ID del cómic a devolver: ");
        comicSystem.listarComics();
        String idComic = sc.nextLine();
        try {
            comicSystem.devolverComic(usuarioActual.getRut(), idComic);
            System.out.println("✅ Cómic devuelto exitosamente.");
        } catch (ComicNoEncontradoException | ComicYaDisponibleException | UsuarioNoEncontradoException | ComicNoDisponibleException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}