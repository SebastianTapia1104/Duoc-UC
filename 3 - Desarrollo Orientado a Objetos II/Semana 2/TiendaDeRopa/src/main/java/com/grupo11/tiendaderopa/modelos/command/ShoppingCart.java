package com.grupo11.tiendaderopa.modelos.command;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<String> items = new ArrayList<>();

    public void addProduct(String product) {
        items.add(product);
        System.out.println("Producto agregado al carrito: " + product);
    }

    public void removeProduct(String product) {
        items.remove(product);
        System.out.println("Producto eliminado del carrito: " + product);
    }
    
    public void displayItems() {
        System.out.println("Productos actuales en el carrito: " + items);
    }
}