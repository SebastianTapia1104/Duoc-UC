package com.sebastiantapia.patronesarquitectonicos.modelos.command;

import com.sebastiantapia.patronesarquitectonicos.interfaces.Command;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;

public class RemoveProductCommand implements Command {
    private ShoppingCart carrito;
    private BaseProduct producto;

    public RemoveProductCommand(ShoppingCart carrito, BaseProduct producto) {
        this.carrito = carrito;
        this.producto = producto;
    }

    @Override
    public void execute() {
        carrito.eliminarProducto(producto);
    }
}