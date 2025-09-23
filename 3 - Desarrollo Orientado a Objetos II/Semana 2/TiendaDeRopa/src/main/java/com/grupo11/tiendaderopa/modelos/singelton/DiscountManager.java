package com.grupo11.tiendaderopa.modelos.singelton;

public class DiscountManager {

    // Variable estática para guardar la única instancia
    private static DiscountManager instancia;

    // Variable para almacenar un descuento global
    private double descuentoGlobal;

    // Constructor privado → impide crear instancias con "new" desde fuera
    private DiscountManager() {
        this.descuentoGlobal = 0.0;
        System.out.println("Se ha creado una unica instancia del DiscountManager.");
    }

    // Método estático público para obtener la instancia única
    public static DiscountManager getInstance() {
        if (instancia == null) {
            instancia = new DiscountManager();
        }
        return instancia;
    }

    // Métodos para trabajar con el descuento
    public void setDescuentoGlobal(double descuento) {
        this.descuentoGlobal = descuento;
    }

    public double getDescuentoGlobal() {
        return this.descuentoGlobal;
    }
}