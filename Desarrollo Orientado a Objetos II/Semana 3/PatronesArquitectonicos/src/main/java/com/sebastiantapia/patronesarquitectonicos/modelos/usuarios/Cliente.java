package com.sebastiantapia.patronesarquitectonicos.modelos.usuarios;

import com.sebastiantapia.patronesarquitectonicos.modelos.command.AddProductCommand;
import com.sebastiantapia.patronesarquitectonicos.interfaces.Command;
import com.sebastiantapia.patronesarquitectonicos.modelos.command.RemoveProductCommand;
import com.sebastiantapia.patronesarquitectonicos.modelos.command.ShoppingCart;
import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;

public class Cliente {
    private String nombre;
    private ShoppingCart carrito;

    public Cliente(String nombre) {
        this.nombre = nombre;
        this.carrito = new ShoppingCart();
    }

    public void agregarProducto(BaseProduct producto) {
        Command agregar = new AddProductCommand(carrito, producto);
        agregar.execute();
    }

    public void eliminarProducto(BaseProduct producto) {
        Command eliminar = new RemoveProductCommand(carrito, producto);
        eliminar.execute();
    }
    
    public void mostrarCarrito(Tienda tienda) {
        carrito.mostrarCarrito(tienda);
    }
    
    public ShoppingCart getCarrito() {
        return this.carrito;
    }
}