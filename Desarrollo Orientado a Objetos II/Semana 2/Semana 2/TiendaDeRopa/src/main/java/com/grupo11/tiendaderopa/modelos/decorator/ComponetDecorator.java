package com.grupo11.tiendaderopa.modelos.decorator;

import com.grupo11.tiendaderopa.interfaces.Component;

public abstract class ComponetDecorator implements Component {
    protected Component decoratedProduct;

    public ComponetDecorator(Component decoratedProduct) {
        this.decoratedProduct = decoratedProduct;
    }

    @Override
    public double getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription();
    }
}