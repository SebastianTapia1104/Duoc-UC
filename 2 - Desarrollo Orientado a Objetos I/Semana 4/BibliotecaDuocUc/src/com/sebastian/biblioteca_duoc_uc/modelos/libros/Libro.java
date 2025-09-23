package com.sebastian.biblioteca_duoc_uc.modelos.libros;

public class Libro {
    
    // ATRIBUTOS
    private String idLibro;
    private String titulo;
    private String autor;
    private int anoPublicacion;
    private boolean prestado; // True si está prestado - False si está disponible
    private String idUsuarioPrestado;

    // CONSTRUCTOR
    public Libro(String idLibro, String titulo, String autor, int anoPublicacion) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.prestado = false;
        this.idUsuarioPrestado = null;
    }

    // GETTERS
    public String getIdLibro() {
        return idLibro;
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

    public boolean isPrestado() {
        return prestado;
    }
    
    public String getIdUsuarioPrestado() {
        return idUsuarioPrestado;
    }
    
    // SETTERS
    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
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

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }
    
    public void setIdUsuarioPrestado(String idUsuarioPrestado) {
        this.idUsuarioPrestado = idUsuarioPrestado;
    }
    
    // MÉTODOS
    @Override
    public String toString() {
        return "ID: " + idLibro + ", Título: " + titulo + ", Autor: " + autor + ", Año: " + anoPublicacion + ", Estado: " + (prestado ? "Prestado" : "Disponible");
    }
}