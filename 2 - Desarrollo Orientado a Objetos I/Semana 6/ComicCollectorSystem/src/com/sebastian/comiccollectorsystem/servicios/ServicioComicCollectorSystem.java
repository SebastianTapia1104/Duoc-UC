package com.sebastian.comiccollectorsystem.servicios;

import com.sebastian.comiccollectorsystem.Excepciones.ArchivoNoEncontradoException;
import com.sebastian.comiccollectorsystem.Excepciones.ComicNoDisponibleException;
import com.sebastian.comiccollectorsystem.Excepciones.ComicNoEncontradoException;
import com.sebastian.comiccollectorsystem.Excepciones.ComicYaDisponibleException;
import com.sebastian.comiccollectorsystem.Excepciones.ErrorLecturaArchivoException;
import com.sebastian.comiccollectorsystem.Excepciones.UsuarioNoEncontradoException;
import com.sebastian.comiccollectorsystem.Excepciones.UsuarioYaRegistradoException;
import com.sebastian.comiccollectorsystem.modelos.comics.Comic;
import com.sebastian.comiccollectorsystem.modelos.usuarios.Usuario;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ServicioComicCollectorSystem {
    
    // ArrayList - HashMap
    private final ArrayList<Comic> listaComics = new ArrayList<>();
    private final HashMap<String, Usuario> usuarios = new HashMap<>(); // 

    // TreeSet para ordenar alfabeticamente
    private final TreeSet<Comic> catalogoOrdenado = new TreeSet<>(Comparator.comparing(Comic::getTitulo).thenComparing(Comic::getIdComic));
    private final TreeSet<Usuario> usuariosOrdenados = new TreeSet<>(Comparator.comparing(Usuario::getNombre));

    // HashSet para colecciones únicas sin orden específico
    private final HashSet<Comic> comicsUnicos = new HashSet<>();

    private final String rutaPrestamos;
    private final String rutaUsuarios;

    public ServicioComicCollectorSystem(String rutaPrestamos, String rutaUsuarios) {
        this.rutaPrestamos = rutaPrestamos;
        this.rutaUsuarios = rutaUsuarios;
    }

    public boolean registrarUsuario(Usuario usuario) throws UsuarioYaRegistradoException {
        if (usuarios.containsKey(usuario.getRut())) {
            throw new UsuarioYaRegistradoException("El RUT " + usuario.getRut() + " ya se encuentra registrado.");
        }
        usuarios.put(usuario.getRut(), usuario);
        usuarios.put(usuario.getRut(), usuario);
        usuariosOrdenados.add(usuario);
        guardarUsuarios();;
        return true;
    }

    public Usuario identificarUsuarioExistente(String rut) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarios.get(rut);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario con RUT " + rut + " no encontrado.");
        }
        return usuario;
    }

    public Comic buscarComicPorId(String idComic) {
        return listaComics.stream()
                .filter(comic -> comic.getIdComic().equalsIgnoreCase(idComic))
                .findFirst()
                .orElse(null);
    }

    public Usuario buscarUsuario(String rut) {
        return usuarios.get(rut);
    }

    public void prestarComic(String rutUsuario, String idComic)
            throws ComicNoEncontradoException, ComicNoDisponibleException, UsuarioNoEncontradoException {
        Usuario usuario = buscarUsuario(rutUsuario);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario con RUT " + rutUsuario + " no encontrado.");
        }

        Comic comic = buscarComicPorId(idComic);
        if (comic == null) {
            throw new ComicNoEncontradoException("No se encontró el cómic con ID: " + idComic);
        }
        if (!comic.getDisponible()) {
            throw new ComicNoDisponibleException("El cómic '" + comic.getTitulo() + "' (ID: " + idComic + ") ya se encuentra prestado.");
        }

        usuario.prestarComic(comic); // Método en la clase Usuario
        comic.prestar(usuario.getRut()); // Actualiza el estado del cómic
        registrarEventoPrestamo(usuario.getRut(), comic.getIdComic(), comic.getTitulo());
    }

    public void devolverComic(String rutUsuario, String idComic)
            throws ComicNoEncontradoException, ComicYaDisponibleException, UsuarioNoEncontradoException, ComicNoDisponibleException {
        Usuario usuario = buscarUsuario(rutUsuario);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario con RUT " + rutUsuario + " no encontrado.");
        }

        Comic comic = buscarComicPorId(idComic);
        if (comic == null) {
            throw new ComicNoEncontradoException("No se encontró el cómic con ID: " + idComic);
        }
        if (comic.getDisponible()) {
            throw new ComicYaDisponibleException("El cómic '" + comic.getTitulo() + "' (ID: " + idComic + ") ya está disponible.");
        }
        if (!comic.getIdUsuarioPrestado().equalsIgnoreCase(rutUsuario)) {
            throw new ComicNoDisponibleException("El cómic '" + comic.getTitulo() + "' (ID: " + idComic + ") no fue prestado por este usuario.");
        }
        usuario.devolverComic(comic); // Método en la clase Usuario
        comic.devolver(); // Actualiza el estado del cómic
        registrarEventoDevolucion(usuario.getRut(), comic.getIdComic(), comic.getTitulo());
    }
    
    // ArrayList
    public void listarComics() {
        if (listaComics.isEmpty()) {
            System.out.println("No hay cómics en el catálogo.");
            return;
        }
        System.out.println("\n📚 Catálogo de Cómics (orden alfabético por título):");
        System.out.printf("%-10s %-40s %-25s %-15s %-15s\n", "ID", "Título", "Autor", "Año Publicación", "Estado"); // <-- Ajustado columna
        System.out.println("--------------------------------------------------------------------------------------------"); // <-- Ajustado
        listaComics.forEach(comic -> System.out.printf("%-10s %-40s %-25s %-15d %-15s\n", // <-- Ajustado columna
                comic.getIdComic(),
                comic.getTitulo(),
                comic.getAutor(),
                comic.getAnoPublicacion(),
                (comic.getDisponible() ? "Disponible" : "Prestado")));;
    }
    
    // HashMap
    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("\n👥 Usuarios Registrados (sin orden específico):");
        System.out.printf("%-20s %-15s %-15s\n", "Nombre", "RUT", "Cómics Prestados");
        System.out.println("----------------------------------------------------------------------------");
        usuarios.values().forEach(usuario -> {
            String comicsPrestados = usuario.getComicsPrestados().stream()
                    .map(Comic::getTitulo)
                    .collect(Collectors.joining(", "));
            if (comicsPrestados.isEmpty()) {
                comicsPrestados = "Ninguno";
            }
            System.out.printf("%-20s %-15s %-15s\n", usuario.getNombre(), usuario.getRut(), comicsPrestados);
        });
    }

    // TreeSet
    public void listarCatalogoOrdenado() {
        if (catalogoOrdenado.isEmpty()) {
            System.out.println("No hay cómics en el catálogo.");
            return;
        }
        System.out.println("\n📚 Catálogo de Cómics (orden alfabético por título):");
        System.out.printf("%-10s %-40s %-25s %-15s %-15s\n", "ID", "Título", "Autor", "Año Publicación", "Estado"); // <-- Ajustado columna
        System.out.println("--------------------------------------------------------------------------------------------"); // <-- Ajustado
        catalogoOrdenado.forEach(comic -> System.out.printf("%-10s %-40s %-25s %-15d %-15s\n", // <-- Ajustado columna
                comic.getIdComic(),
                comic.getTitulo(),
                comic.getAutor(),
                comic.getAnoPublicacion(),
                (comic.getDisponible() ? "Disponible" : "Prestado")));;
    }

    public void listarUsuariosOrdenados() {
        if (usuariosOrdenados.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("\n👥 Usuarios Registrados (orden alfabético por nombre):");
        System.out.printf("%-20s %-15s %-15s\n", "Nombre", "RUT", "Cómics Prestados");
        System.out.println("----------------------------------------------------------------------------");
        usuariosOrdenados.forEach(usuario -> {
            String comicsPrestados = usuario.getComicsPrestados().stream()
                    .map(Comic::getTitulo)
                    .collect(Collectors.joining(", "));
            if (comicsPrestados.isEmpty()) {
                comicsPrestados = "Ninguno";
            }
            System.out.printf("%-20s %-15s %-15s\n", usuario.getNombre(), usuario.getRut(), comicsPrestados);
        });
    }

    // HashSet
    public void mostrarComicsUnicos() {
        if (comicsUnicos.isEmpty()) {
            System.out.println("No hay cómics en el sistema.");
            return;
        }
        Map<String, List<Comic>> comicsByTitle = new HashMap<>();
        for (Comic comic : comicsUnicos) {
            comicsByTitle.computeIfAbsent(comic.getTitulo(), k -> new ArrayList<>()).add(comic);
        }
        System.out.println("\n📦 Cómics Únicos por Título (incluyendo todas las unidades):");
        System.out.printf("%-40s %s\n", "Título", "IDs (Estado)");
        System.out.println("----------------------------------------------------------------------------------------------------");

        for (Map.Entry<String, List<Comic>> entry : comicsByTitle.entrySet()) {
            String titulo = entry.getKey();
            List<Comic> comicsConMismoTitulo = entry.getValue();
            StringBuilder idsAndStatus = new StringBuilder();
            for (int i = 0; i < comicsConMismoTitulo.size(); i++) {
                Comic comic = comicsConMismoTitulo.get(i);
                idsAndStatus.append(comic.getIdComic());
                idsAndStatus.append(" (");
                idsAndStatus.append(comic.getDisponible() ? "Disponible" : "Prestado");
                idsAndStatus.append(")");
                if (i < comicsConMismoTitulo.size() - 1) {
                    idsAndStatus.append(", ");
                }
            }
            System.out.printf("%-40s %s\n", titulo, idsAndStatus.toString());
        }
    }

    public void cargarComicsDesdeCSV(String rutaArchivo) throws ArchivoNoEncontradoException, ErrorLecturaArchivoException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) { // ID, Titulo, Autor, AnoPublicacion
                    try {
                        String id = datos[0].trim();
                        String titulo = datos[1].trim();
                        String autor = datos[2].trim();
                        int anoPublicacion = Integer.parseInt(datos[3].trim());
                        Comic comic = new Comic(id, titulo, autor, anoPublicacion);
                        listaComics.add(comic);
                        comicsUnicos.add(comic);
                        catalogoOrdenado.add(comic);
                    } catch (NumberFormatException e) {
                        System.err.println("Advertencia: Línea con formato de año de publicación incorrecto saltada: " + linea + " | Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Advertencia: Línea con formato incorrecto saltada: " + linea);
                }
            }
            System.out.println("Cómics cargados exitosamente desde " + rutaArchivo);
        } catch (java.io.FileNotFoundException e) {
            throw new ArchivoNoEncontradoException("El archivo de cómics CSV no se encontró en la ruta: " + rutaArchivo);
        } catch (IOException e) {
            throw new ErrorLecturaArchivoException("Error al leer el archivo CSV de cómics: " + e.getMessage(), e);
        }
    }

    public String consultarDisponibilidadComic(String idComic) throws ComicNoEncontradoException {
        Comic comic = listaComics.stream()
                .filter(c -> c.getIdComic().equalsIgnoreCase(idComic))
                .findFirst()
                .orElseThrow(() -> new ComicNoEncontradoException("No se encontró el cómic con ID: " + idComic));
        StringBuilder sb = new StringBuilder("📘 " + comic + "\n");
        if (!comic.getDisponible()) { 
            String rut = comic.getIdUsuarioPrestado();
            Usuario u = usuarios.get(rut);
            String prestadoPor = (u != null) ? u.getNombre() : "ID " + rut + " (usuario no encontrado)";
            sb.append("Estado: ¡El cómic se encuentra PRESTADO a ").append(prestadoPor).append("!");
        } else {
            sb.append("Estado: ¡El cómic está DISPONIBLE!");
        }
        return sb.toString();
    }

    private void registrarEventoPrestamo(String rutUsuario, String idComic, String tituloComic) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaPrestamos, true))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            writer.println("PRESTAMO;" + fecha + ";" + rutUsuario + ";" + idComic + ";" + tituloComic);
        } catch (IOException e) {
            System.err.println("Error al registrar préstamo en el archivo: " + e.getMessage());
        }
    }

    private void registrarEventoDevolucion(String rutUsuario, String idComic, String tituloComic) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaPrestamos, true))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            writer.println("DEVOLUCION;" + fecha + ";" + rutUsuario + ";" + idComic + ";" + tituloComic);
        } catch (IOException e) {
            System.err.println("Error al registrar devolución en el archivo: " + e.getMessage());
        }
    }
    
    private void guardarUsuarios() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaUsuarios))) {
            for (Usuario usuario : usuarios.values()) {
                writer.println(usuario.getRut() + "," + usuario.getNombre());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos de usuarios en " + rutaUsuarios + ": " + e.getMessage());
        }
    }

}