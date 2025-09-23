package com.sebastiantapia.patronesarquitectonicos.modelos.usuarios;

import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;
import com.sebastiantapia.patronesarquitectonicos.modelos.singleton.DiscountManager;

public class Admin {
    private String nombre;
    private String contrasena;

    public Admin(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public boolean validarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }

    public void agregarProductoTienda(Tienda tienda, double precio, String nombre, String categoria, String color, String talla, int cantidad) {
        BaseProduct nuevoProducto = new BaseProduct(precio, nombre, categoria, color, talla, cantidad);
        tienda.agregarProducto(nuevoProducto);
        System.out.println("Admin " + this.nombre + " ha agregado un producto a la tienda.");
    }

    public boolean eliminarProductoTienda(Tienda tienda, String nombre) {
        return tienda.buscarProducto(nombre).map(tienda::eliminarProducto).orElse(false);
    }

    public boolean editarPrecioProducto(Tienda tienda, String nombre, double nuevoPrecio) {
        return tienda.editarPrecio(nombre, nuevoPrecio);
    }
    
    public boolean editarCantidadProducto(Tienda tienda, String nombre, int nuevaCantidad) {
        return tienda.editarCantidad(nombre, nuevaCantidad);
    }

    public void editarCategoriaConDescuento(String categoria) {
        DiscountManager.getInstance().setCategoriaConDescuento(categoria);
        System.out.println("Admin " + this.nombre + " ha editado la categoría con descuento.");
    }
}