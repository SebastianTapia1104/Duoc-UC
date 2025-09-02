package com.grupo11.tiendaderopa.modelos.decorator;

import com.grupo11.tiendaderopa.interfaces.Component;

public class TwentyPercDecorator extends ComponetDecorator {

    public TwentyPercDecorator(Component decoratedProduct) {
        super(decoratedProduct);
    }

    @Override
    public double getPrice() {
        double originalPrice = super.getPrice();
        // Aplica un 20% de descuento si el producto es de la categoría "Pantalón".
        if (super.getDescription().contains("Pantalon")) {
            return originalPrice * 0.80;
        }
        return originalPrice;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (Descuento por categoria)";
    }
}