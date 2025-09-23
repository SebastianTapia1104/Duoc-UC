package com.sebastiantapia.patronesarquitectonicos.modelos.singleton;

public class DiscountManager {
    private static DiscountManager instance;
    private final double descuentoGlobal = 0.10; // Descuento fijo del 10%
    private final double descuentoCategoria = 0.20; // Descuento fijo del 20%
    private String categoriaConDescuento = "";

    private DiscountManager() {}

    public static DiscountManager getInstance() {
        if (instance == null) {
            instance = new DiscountManager();
        }
        return instance;
    }

    public double getDescuentoGlobal() {
        return descuentoGlobal;
    }
    
    public double getDescuentoCategoria() {
        return descuentoCategoria;
    }

    public String getCategoriaConDescuento() {
        return categoriaConDescuento;
    }
    
    public void setCategoriaConDescuento(String categoria) {
        this.categoriaConDescuento = categoria;
    }
}