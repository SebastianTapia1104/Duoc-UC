package com.grupo11.tiendaderopa.modelos.decorator;

import com.grupo11.tiendaderopa.interfaces.Component;

public class TenPercDecorator extends ComponetDecorator {

    public TenPercDecorator(Component decoratedProduct) {
        super(decoratedProduct);
    }

    @Override
    public double getPrice() {
        return super.getPrice() * 0.90; // Aplica un 10% de descuento al precio del producto decorado.
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (Promocion 10% de descuento)";
    }
}