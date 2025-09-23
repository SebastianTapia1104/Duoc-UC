package com.sebastiantapia.patronesarquitectonicos.modelos.decorator;

public class BaseProduct {
    private double precio;
    private String nombre;
    private String categoria;
    private String color;
    private String talla;
    private int cantidad;

    public BaseProduct(double precio, String nombre, String categoria, String color, String talla, int cantidad) {
        this.precio = precio;
        this.nombre = nombre;
        this.categoria = categoria;
        this.color = color;
        this.talla = talla;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseProduct that = (BaseProduct) obj;
        return nombre.equalsIgnoreCase(that.nombre) &&
               categoria.equalsIgnoreCase(that.categoria) &&
               talla.equalsIgnoreCase(that.talla);
    }
    
    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + categoria.hashCode();
        result = 31 * result + talla.hashCode();
        return result;
    }
}