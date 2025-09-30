package com.grupo11.magenta.modelos;

import com.grupo11.magenta.controladores.DBConector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cartelera {

    public void crearPelicula(Pelicula pelicula) {
        String sql = "INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setInt(4, pelicula.getDuracion());
            pstmt.setString(5, pelicula.getGenero());
            pstmt.executeUpdate();
            System.out.println("✅ Película creada exitosamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al crear la película: " + e.getMessage());
        }
    }

    public List<Pelicula> obtenerPeliculas() {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pelicula pelicula = new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getInt("anio"),
                    rs.getInt("duracion"),
                    rs.getString("genero")
                );
                peliculas.add(pelicula);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener las películas: " + e.getMessage());
        }
        return peliculas;
    }

    public void actualizarPelicula(Pelicula pelicula) {
        String sql = "UPDATE Cartelera SET titulo = ?, director = ?, anio = ?, duracion = ?, genero = ? WHERE id = ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setInt(4, pelicula.getDuracion());
            pstmt.setString(5, pelicula.getGenero());
            pstmt.setInt(6, pelicula.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Película actualizada exitosamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar la película: " + e.getMessage());
        }
    }

    public void eliminarPelicula(int id) {
        String sql = "DELETE FROM Cartelera WHERE id = ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Película eliminada exitosamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar la película: " + e.getMessage());
        }
    }
    
    public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera WHERE titulo LIKE ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%"); // El '%' permite buscar coincidencias parciales
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pelicula pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                    );
                    peliculas.add(pelicula);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar películas: " + e.getMessage());
        }
        return peliculas;
    }
    
    public Pelicula obtenerPeliculaPorId(int id) {
        Pelicula pelicula = null;
        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera WHERE id = ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si la película existe, crea el objeto Pelicula
                    pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar la película por ID: " + e.getMessage());
        }
        return pelicula;
    }
}