package com.sebastian.comiccollectorsystem.modelos.comics;

import java.util.Objects;

public class Comic {
    
    // ATRIBUTOS
    private String idComic;
    private String titulo;
    private String autor;
    private int anoPublicacion;
    private boolean disponible; 
    private String rutUsuarioPrestado;

    // CONSTRUCTOR
    public Comic(String idComic, String titulo, String autor, int anoPublicacion) {
        this.idComic = idComic;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.disponible = true;
        this.rutUsuarioPrestado = null;
    }

    // GETTERS
    public String getIdComic() {
        return idComic;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoPublicacion() {
        return anoPublicacion;
    }

    public boolean getDisponible() {
        return disponible;
    }
    
    public String getIdUsuarioPrestado() {
        return rutUsuarioPrestado;
    }
    
    // SETTERS
    public void setIdComic(String idLibro) {
        this.idComic = idLibro;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAnoPublicacion(int anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }
    
    public void setIdUsuarioPrestado(String rut) {
        this.rutUsuarioPrestado = rut;
    }
    
    // MÉTODOS
    public void prestar(String rut) {
        this.disponible = false; // Deja de estar disponible al prestarlo
        this.rutUsuarioPrestado = rut;
    }
    
    public void devolver() {
        this.disponible = true; // Vuelve a estar disponible cuando se devuelve
        this.rutUsuarioPrestado = null;
    }
    
    @Override
    public String toString() {
        return "ID: " + idComic + ", Título: " + titulo + ", Autor: " + autor + ", Año: " + anoPublicacion +
               " (Estado: " + (disponible ? "Disponible" : "Prestado a " + rutUsuarioPrestado) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return Objects.equals(idComic, comic.idComic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idComic);
    }
}