package com.grupo11.tiendaderopa.modelos.decorator;

import com.grupo11.tiendaderopa.interfaces.Component;

public class Decorator implements Component {
    private double price;
    private String description;

    public Decorator(String description, double price) {
        this.description = description;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }
}