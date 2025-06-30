package com.sebastian.comiccollectorsystem.modelos.usuarios;

import com.sebastian.comiccollectorsystem.modelos.comics.Comic;
import java.util.ArrayList;
import java.util.Objects;

public class Usuario implements Comparable<Usuario>{
    
    // ATRIBUTOS
    private String nombre;
    private String rut; 
    private final ArrayList<Comic> comicsPrestados;
    
    //CONSTRUCTOR
    public Usuario(String nombre, String rut) {
        this.nombre = nombre;
        this.rut = rut;
        this.comicsPrestados = new ArrayList<>();
    }

    // GETTERS
    public String getNombre() {
        return nombre;
    }

    public String getRut() {
        return rut;
    }
    
    public ArrayList<Comic> getComicsPrestados() {
        return comicsPrestados;
    }
    
    // SETTERS

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
    
    // MÉTODOS
    public void prestarComic(Comic comic) {
        if (comic.getDisponible()) {
            this.comicsPrestados.add(comic);
        }
    }

    public void devolverComic(Comic comic) {
        if (this.comicsPrestados.contains(comic)) {
            this.comicsPrestados.remove(comic);
        }
    }
    
    @Override
    public String toString() {
        return "Nombre: " + nombre + ", RUT: " + rut + ", Cómics prestados: " + comicsPrestados.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(rut, usuario.rut); // Comparar por RUT para unicidad
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }

    @Override
    public int compareTo(Usuario other) {
        return this.nombre.compareToIgnoreCase(other.nombre); // Ordenar por nombre alfabéticamente
    }
    
}