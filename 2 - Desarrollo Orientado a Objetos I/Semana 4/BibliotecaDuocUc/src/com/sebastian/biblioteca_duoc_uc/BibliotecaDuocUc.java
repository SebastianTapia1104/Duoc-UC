package com.sebastian.biblioteca_duoc_uc;

import com.sebastian.biblioteca_duoc_uc.Excepciones.ArchivoNoEncontradoException;
import com.sebastian.biblioteca_duoc_uc.Excepciones.EntradaInvalidaException;
import com.sebastian.biblioteca_duoc_uc.Excepciones.ErrorLecturaArchivoException;
import com.sebastian.biblioteca_duoc_uc.Excepciones.LibroNoEncontradoException;
import com.sebastian.biblioteca_duoc_uc.Excepciones.LibroPrestadoException;
import com.sebastian.biblioteca_duoc_uc.Excepciones.RutInvalidoException;
import com.sebastian.biblioteca_duoc_uc.modelos.clientes.Usuario;
import com.sebastian.biblioteca_duoc_uc.modelos.libros.Libro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BibliotecaDuocUc {

    private static ArrayList<Libro> listaLibros = new ArrayList<>();
    private static HashMap<String, Usuario> mapaUsuarios = new HashMap<>();
    private static final String RUTA_PRESTAMOS = "src/com/sebastian/biblioteca_duoc_uc/resources/prestamos.txt"; // Ruta del archivo de registro de préstamos
    private static final String RUTA_LIBROS_CSV = "src/com/sebastian/biblioteca_duoc_uc/resources/libros.csv"; // Ruta del archivo CSV de libros
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario usuarioActual = null; // Para mantener el usuario logueado

    public static void main(String[] args) {

        try { // Cargar libros
            cargarLibrosDesdeCSV(RUTA_LIBROS_CSV);
        } catch (ArchivoNoEncontradoException | ErrorLecturaArchivoException e) { // No encuentra libros
            System.err.println("Error crítico al cargar libros: " + e.getMessage());
            System.err.println("El programa se cerrará.");
            return; // Salir del programa si no se pueden cargar los libros
        }
        int opcionPrincipal;
        do {
            System.out.println("\n--- Bienvenido a la Biblioteca DUOC UC ---");
            System.out.println("1. Registrar nuevo usuario");
            System.out.println("2. Identificarse como usuario existente");
            System.out.println("3. Salir");
            System.out.print("Elija una opción: ");
            try {
                opcionPrincipal = leerEntero();
                switch (opcionPrincipal) {
                    case 1:
                        registrarNuevoUsuario();
                        break;
                    case 2:
                        identificarUsuarioExistente();
                        break;
                    case 3:
                        System.out.println("¡Gracias por usar la Biblioteca DUOC UC! Hasta pronto.");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (EntradaInvalidaException  e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número: " + e.getMessage());
                opcionPrincipal = 0; // Bucle
            }
        } while (opcionPrincipal != 3);
        scanner.close();
    }
    
    private static int leerEntero() throws EntradaInvalidaException {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new EntradaInvalidaException("Se esperaba un número entero.", e); 
        }
    }
    
    private static String leerDigitoVerificador() throws EntradaInvalidaException {
        System.out.print("Ingrese Dígito Verificador (número o 'K', ej. 9 o K): ");
        String dv = scanner.nextLine().trim().toUpperCase(); 
        if (dv.isEmpty() || dv.length() > 1 || (!dv.matches("[0-9]") && !dv.equals("K"))) {
            throw new EntradaInvalidaException("El Dígito Verificador debe ser un solo carácter numérico o 'K'.");
        }
        return dv;
    }

    private static void cargarLibrosDesdeCSV(String rutaArchivo) throws ArchivoNoEncontradoException, ErrorLecturaArchivoException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(","); // Divide la línea por comas
                if (datos.length == 4) { // ID, Titulo, Autor, Año
                    try {
                        String id = datos[0].trim();
                        String titulo = datos[1].trim();
                        String autor = datos[2].trim();
                        int ano = Integer.parseInt(datos[3].trim()); // Año a entero
                        listaLibros.add(new Libro(id, titulo, autor, ano));
                    } catch (NumberFormatException e) {
                        System.err.println("Advertencia: Línea con formato de año incorrecto saltada: " + linea + " | Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Advertencia: Línea con formato incorrecto saltada: " + linea);
                }
            }
            System.out.println("Libros cargados exitosamente desde " + rutaArchivo);
        } catch (java.io.FileNotFoundException e) {
            throw new ArchivoNoEncontradoException("El archivo de libros CSV no se encontró en la ruta: " + rutaArchivo);
        } catch (IOException e) {
            throw new ErrorLecturaArchivoException("Error al leer el archivo CSV de libros: " + e.getMessage(), e);
        }
    }

    private static void registrarEventoPrestamo(String mensaje) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_PRESTAMOS, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println(timestamp + " - " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al registrar evento de préstamo: " + e.getMessage());
        }
    }
    
    private static Libro buscarLibroPorId(String idLibro) throws LibroNoEncontradoException {
        for (Libro libro : listaLibros) {
            if (libro.getIdLibro().equalsIgnoreCase(idLibro)) {
                return libro;
            }
        }
        throw new LibroNoEncontradoException("El libro con ID '" + idLibro + "' no se encuentra en la biblioteca.");
    }

    private static void agregarUsuario(Usuario usuario) {
        mapaUsuarios.put(String.valueOf(usuario.getIdUsuario()), usuario);
        System.out.println("Usuario agregado: " + usuario.getNombre());
    }

    private static Usuario buscarUsuarioPorRutDv(int rut, String dv) {
        for (Usuario usuario : mapaUsuarios.values()) {
            if (usuario.verificarRutCompleto(rut, dv)) {
                return usuario;
            }
        }
        return null; // Sin coincidencia
    }

    private static void prestarLibro(String idLibro, String idUsuario) throws LibroNoEncontradoException, LibroPrestadoException {
        Libro libro = buscarLibroPorId(idLibro);
        if (libro.isPrestado()) {
            throw new LibroPrestadoException("El libro '" + libro.getTitulo() + "' (ID: " + idLibro + ") ya está prestado.");
        }
        Usuario usuario = mapaUsuarios.get(idUsuario); // Obtiene ID de usuario
        if (usuario == null) {
            System.out.println("Error interno: El usuario con ID '" + idUsuario + "' no está registrado. Por favor, reinicie la sesión.");
            return;
        }
        libro.setPrestado(true);
        libro.setIdUsuarioPrestado(idUsuario);
        String mensajeRegistro = "PRESTAMO: Libro '" + libro.getTitulo() + "' (ID: " + idLibro + ") prestado a Usuario '" + usuario.getNombre() + "' (RUT: " + usuario.getRut() + "-" + usuario.getDv() + ").";
        System.out.println(mensajeRegistro);
        registrarEventoPrestamo(mensajeRegistro); // Registra el evento en el archivo
    }

    private static void devolverLibro(String idLibro) throws LibroNoEncontradoException {
        Libro libro = buscarLibroPorId(idLibro); // Busca el libro
        if (!libro.isPrestado()) {
            System.out.println("El libro '" + libro.getTitulo() + "' (ID: " + idLibro + ") no estaba prestado.");
            return;
        }
        if (usuarioActual == null) {
            System.out.println("Error: No ha iniciado sesión. Debe iniciar sesión para devolver un libro.");
            return;
        }
        if (!String.valueOf(usuarioActual.getIdUsuario()).equals(libro.getIdUsuarioPrestado())) {
            System.out.println("Error: No puedes devolver el libro '" + libro.getTitulo() + "' porque no lo tienes prestado.");
            System.out.println("Este libro está prestado al usuario con ID: " + libro.getIdUsuarioPrestado());
            return;
        }
        libro.setPrestado(false);
        String mensajeRegistro = "DEVOLUCION: Libro '" + libro.getTitulo() + "' (ID: " + idLibro + ") devuelto por Usuario '" + usuarioActual.getNombre() + "' (RUT: " + usuarioActual.getRut() + "-" + usuarioActual.getDv() + ").";
        System.out.println(mensajeRegistro);
        registrarEventoPrestamo(mensajeRegistro); // Registra el evento en el archivo
    }

    private static void listarLibros() {
        if (listaLibros.isEmpty()) {
            System.out.println("No hay libros en la biblioteca.");
            return;
        }
        System.out.println("\n--- Lista de Libros ---");
        System.out.printf("%-10s %-40s %-25s %-5s %-15s\n", "ID", "Título", "Autor", "Año", "Estado");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Libro libro : listaLibros) {
            System.out.printf("%-10s %-40s %-25s %-5d %-15s\n",
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getAnoPublicacion(),
                    (libro.isPrestado() ? "Prestado" : "Disponible"));
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private static void listarUsuarios() {
        if (mapaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("\n--- Lista de Usuarios ---");
        System.out.printf("%-15s %-25s %-15s %-5s\n", "ID Usuario", "Nombre", "RUT", "DV");
        System.out.println("---------------------------------------------------------------");
        for (Usuario usuario : mapaUsuarios.values()) {
            System.out.printf("%-15d %-25s %-15d %-5s\n", usuario.getIdUsuario(), usuario.getNombre(), usuario.getRut(), usuario.getDv());
        }
        System.out.println("---------------------------------------------------------------");
    }

    private static void registrarNuevoUsuario() {
        System.out.println("\n--- Registro de Nuevo Usuario ---");
        String nombre = "";
        int rut = 0;
        String dv = "";
        boolean datosValidos = false;
        while (!datosValidos) {
            try {
                System.out.print("Ingrese Nombre completo: ");
                nombre = scanner.nextLine();
                System.out.print("Ingrese RUT (solo números, sin puntos ni dígito verificador, ej. 12345678): ");
                rut = leerEntero();
                dv = leerDigitoVerificador();
                if (buscarUsuarioPorRutDv(rut, dv) != null) {
                    System.out.println("Error: Ya existe un usuario registrado con ese RUT y Dígito Verificador.");
                } else {
                    datosValidos = true; // Sale del bucle si no existe
                }
            } catch (EntradaInvalidaException e) { 
                System.out.println("Error de entrada: " + e.getMessage());
            } catch (Exception e) { 
                System.out.println("Ocurrió un error inesperado al leer los datos: " + e.getMessage());
                scanner.nextLine(); 
            }
        }
        Usuario nuevoUsuario = new Usuario(nombre, rut, dv); // ID generado en clase Usuario
        agregarUsuario(nuevoUsuario);
        System.out.println("Usuario '" + nombre + "' registrado exitosamente con ID: " + nuevoUsuario.getIdUsuario());
    }


    private static void identificarUsuarioExistente() {
        System.out.println("\n--- Identificación de Usuario ---");
        int rutIngresado = 0;
        String dvIngresado = "";
        boolean datosValidos = false;
        while (!datosValidos) {
            try {
                System.out.print("Ingrese su RUT (solo números, sin puntos ni dígito verificador): ");
                rutIngresado = leerEntero(); // RUT
                dvIngresado = leerDigitoVerificador(); // DV
                datosValidos = true;
            } catch (EntradaInvalidaException e) { 
                System.out.println("Error de entrada: " + e.getMessage());
            }
        }
        try {
            Usuario usuarioEncontrado = buscarUsuarioPorRutDv(rutIngresado, dvIngresado);
            if (usuarioEncontrado != null) {
                usuarioActual = usuarioEncontrado; // Establece el usuario como logueado
                System.out.println("Bienvenido, " + usuarioActual.getNombre() + "!");
                menuUsuario(); // Abre el menú de usuario
            } else {
                throw new RutInvalidoException("RUT o Dígito Verificador incorrecto. Usuario no encontrado.");
            }
        } catch (RutInvalidoException e) {
            System.out.println("Error de identificación: " + e.getMessage());
        }
    }

    private static void menuUsuario() {
        int opcionUsuario;
        do {
            System.out.println("\n--- Menú de Usuario (" + usuarioActual.getNombre() + ") ---");
            System.out.println("1. Consultar disponibilidad de libro");
            System.out.println("2. Pedir prestado un libro");
            System.out.println("3. Devolver un libro");
            System.out.println("4. Listar todos los libros");
            System.out.println("5. Listar todos los usuarios");
            System.out.println("6. Cerrar sesión");
            System.out.print("Elija una opción: ");
            try {
                opcionUsuario = leerEntero();
                switch (opcionUsuario) {
                    case 1:
                        listarLibros();
                        consultarDisponibilidadLibro();
                        break;
                    case 2:
                        listarLibros();
                        pedirPrestamoLibro();
                        break;
                    case 3:
                        listarLibros();
                        devolverLibro();
                        break;
                    case 4:
                        listarLibros();
                        break;
                    case 5:
                        listarUsuarios();
                        break;
                    case 6: // Cierra sesión y vuelve al menú anterior
                        System.out.println("Cerrando sesión de " + usuarioActual.getNombre() + ".");
                        usuarioActual = null;
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (EntradaInvalidaException e) {
                System.out.println("Error de entrada: " + e.getMessage());
                opcionUsuario = 0;
            }
        } while (opcionUsuario != 6);
    }

    private static void consultarDisponibilidadLibro() {
        System.out.print("Ingrese el ID del libro a consultar: ");
        String idLibro = scanner.nextLine();
        try {
            Libro libro = buscarLibroPorId(idLibro);
            System.out.println("Información del libro: " + libro);
            if (libro.isPrestado()) {
                String prestadoPorNombre = "desconocido";
                if (libro.getIdUsuarioPrestado() != null) {
                    Usuario u = mapaUsuarios.get(libro.getIdUsuarioPrestado());
                    if (u != null) {
                        prestadoPorNombre = u.getNombre();
                    } else {
                        prestadoPorNombre = "ID " + libro.getIdUsuarioPrestado() + " (usuario no encontrado)";
                    }
                }
                System.out.println("Estado: ¡El libro se encuentra PRESTADO por " + prestadoPorNombre + "!");
            } else {
                System.out.println("Estado: ¡El libro está DISPONIBLE!");
            }
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void pedirPrestamoLibro() {
        System.out.print("Ingrese el ID del libro que desea pedir prestado: ");
        String idLibro = scanner.nextLine();
        try {
            if (usuarioActual == null) {
                System.out.println("Error: Debe identificarse como usuario para realizar un préstamo.");
                return;
            }
            prestarLibro(idLibro, String.valueOf(usuarioActual.getIdUsuario()));
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (LibroPrestadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void devolverLibro() {
        System.out.print("Ingrese el ID del libro que desea devolver: ");
        String idLibro = scanner.nextLine();
        try {
            devolverLibro(idLibro);
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
}