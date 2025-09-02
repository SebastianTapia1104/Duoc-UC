package com.grupo11.tiendaderopa.modelos.command;

import com.grupo11.tiendaderopa.interfaces.Command;

public class RemoveProductCommand implements Command {
    private final ShoppingCart cart;
    private final String product;

    public RemoveProductCommand(ShoppingCart cart, String product) {
        this.cart = cart;
        this.product = product;
    }

    @Override
    public void ejecutar() {
        cart.removeProduct(product);
    }
}