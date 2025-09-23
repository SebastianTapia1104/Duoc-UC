package com.sebastian.drivequestrentals.modelos;

import com.sebastian.drivequestrentals.interfaces.CalculosArriendo;
import java.io.Serializable;

public abstract class Vehiculo implements Serializable, CalculosArriendo {
    
    // ATRIBUTOS
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private double precioArriendoDia;
    private int diasArriendo; 
    private boolean estaArrendado;

    // CCONTRUCTOR VACÍO
    public Vehiculo() {
        this.estaArrendado = false;
        this.diasArriendo = 0;
    }

    // CONSTRUCTOR SOBRECARGADO
    public Vehiculo(String patente, String marca, String modelo, int anio, double precioArriendoDia, int diasArriendo) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precioArriendoDia = precioArriendoDia;
        this.diasArriendo = diasArriendo;
        this.estaArrendado = false;
    }

    // GETTERS Y SETTERS
    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getPrecioArriendoDia() {
        return precioArriendoDia;
    }

    public void setPrecioArriendoDia(double precioArriendoDia) {
        this.precioArriendoDia = precioArriendoDia;
    }

    public int getDiasArriendo() {
        return diasArriendo;
    }

    public void setDiasArriendo(int diasArriendo) {
        this.diasArriendo = diasArriendo;
    }

    public boolean isEstaArrendado() {
        return estaArrendado;
    }

    public void setEstaArrendado(boolean estaArrendado) {
        this.estaArrendado = estaArrendado;
    }

    // MÉTODO ABSTRACTO
    public abstract String[] mostrarDatos();

    // METODO PARA CALCULAR Y MOSTRAR BOLETA
    @Override
    public void calcularYMostrarBoleta(Vehiculo vehiculo) {
        double precioBase = calcularValorBase(vehiculo);
        double descuento = 0;
        if (vehiculo instanceof VehiculoCarga) { // Se aplica el descuento según el tipo de vehículo
            descuento = precioBase * CalculosArriendo.DESCUENTO_CARGA;
        } else if (vehiculo instanceof VehiculoPasajeros) {
            descuento = precioBase * CalculosArriendo.DESCUENTO_PASAJEROS;
        }
        double subtotal = precioBase - descuento;
        double ivaCalculado = subtotal * CalculosArriendo.IVA;
        double totalAPagar = subtotal + ivaCalculado;
        System.out.println("\n--- Detalle de Boleta de Arriendo ---");
        System.out.println("Patente: " + vehiculo.getPatente());
        System.out.println("Marca: " + vehiculo.getMarca());
        System.out.println("Modelo: " + vehiculo.getModelo());
        System.out.println("Días de Arriendo: " + vehiculo.getDiasArriendo());
        System.out.printf("Precio Base Arriendo: $%.2f%n", precioBase);
        System.out.printf("Descuento Aplicado: $%.2f (%.0f%%)%n", descuento, (vehiculo instanceof VehiculoCarga ? CalculosArriendo.DESCUENTO_CARGA : CalculosArriendo.DESCUENTO_PASAJEROS) * 100);
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("IVA (%.0f%%): $%.2f%n", CalculosArriendo.IVA * 100, ivaCalculado);
        System.out.printf("Total a Pagar: $%.2f%n", totalAPagar);
        System.out.println("------------------------------------");
    }
}