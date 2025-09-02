package com.sebastiantapia.patronesarquitectonicos.modelos.command;

import com.sebastiantapia.patronesarquitectonicos.interfaces.Command;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;

public class AddProductCommand implements Command {
    private ShoppingCart carrito;
    private BaseProduct producto;

    public AddProductCommand(ShoppingCart carrito, BaseProduct producto) {
        this.carrito = carrito;
        this.producto = producto;
    }

    @Override
    public void execute() {
        carrito.agregarProducto(producto);
    }
}