package com.sebastiantapia.ambiente.modelos;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private String descripcion;
    private int cantidadEnStock;

    // Constructor que inicializa todos los atributos
    public Producto(String codigo, String nombre, double precio, String descripcion, int cantidadEnStock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.cantidadEnStock = cantidadEnStock;
    }

    // Getters: Métodos para obtener el valor de los atributos
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidadEnStock() {
        return cantidadEnStock;
    }

    // Setters: Métodos para modificar el valor de los atributos
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidadEnStock(int cantidadEnStock) {
        this.cantidadEnStock = cantidadEnStock;
    }

    // Método para actualizar el precio del producto
    public void actualizarPrecio(double nuevoPrecio) {
        this.precio = nuevoPrecio;
    }

    // Método que devuelve una descripción detallada del producto
    @Override
    public String toString() {
        return "Código: " + codigo +
               ", Nombre: " + nombre +
               ", Descripción: " + descripcion +
               ", Precio: $" + precio +
               ", Cantidad en stock: " + cantidadEnStock;
    }
}