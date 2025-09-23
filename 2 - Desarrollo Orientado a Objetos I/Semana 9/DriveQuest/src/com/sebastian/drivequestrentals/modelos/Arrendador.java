package com.sebastian.drivequestrentals.modelos;

import java.io.Serializable;

public class Arrendador implements Serializable {
    
    // ATRIBUTOS
    private String rut;
    private String nombre;
    private String telefono;
    private Vehiculo vehiculoArrendado;
    
    // CONSTRUCTOR
    public Arrendador(String rut, String nombre, String telefono) {
        this.rut = rut;
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculoArrendado = null; // Parte sin vehículo arrendado
    }

    // GETTERS
    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public Vehiculo getVehiculoArrendado() {
        return vehiculoArrendado;
    }

    // SETTERS
    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setVehiculoArrendado(Vehiculo vehiculoArrendado) {
        this.vehiculoArrendado = vehiculoArrendado;
    }
    
    // MÉTODOS
    public void mostrarDatos() {
        System.out.println("--- Datos del Arrendador ---");
        System.out.println("RUT: " + rut);
        System.out.println("Nombre: " + nombre);
        System.out.println("Teléfono: " + telefono);
        if (vehiculoArrendado != null) {
            System.out.println("Vehículo Arrendado (Patente): " + vehiculoArrendado.getPatente() + " (" + vehiculoArrendado.getMarca() + " " + vehiculoArrendado.getModelo() + ")");
        } else {
            System.out.println("No tiene vehículo arrendado actualmente.");
        }
        System.out.println("----------------------------");
    }
}