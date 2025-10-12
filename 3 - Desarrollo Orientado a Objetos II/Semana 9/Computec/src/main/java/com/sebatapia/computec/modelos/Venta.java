package com.sebatapia.computec.modelos;

import java.time.LocalDateTime;

public class Venta {

    private int idVenta;
    private LocalDateTime fechaHora;
    private Cliente cliente; // Objeto completo para tener todos los datos del cliente
    private Equipo equipo;   // Objeto completo para tener todos los datos del equipo
    private double precioFinal; // Precio al momento de la venta

    // Constructor completo
    public Venta(int idVenta, LocalDateTime fechaHora, Cliente cliente, Equipo equipo) {
        this.idVenta = idVenta;
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.equipo = equipo;
        if (equipo != null) {
            this.precioFinal = equipo.getPrecio(); // Guarda el precio del equipo en ese momento
        }
    }

    // Constructor especial de creación
    public Venta(Cliente cliente, Equipo equipo) {
        this.idVenta = 0; // El ID será asignado por la base de datos
        this.fechaHora = LocalDateTime.now(); // Captura la fecha y hora actual
        this.cliente = cliente;
        this.equipo = equipo;
        if (equipo != null) {
            this.precioFinal = equipo.getPrecio();
        }
    }
    
    // Constructor vacío
    public Venta() {
    }

    // Getters 
    public int getIdVenta() {
        return idVenta;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Equipo getEquipo() {
        return equipo;
    }
    public double getPrecioFinal() {
        return precioFinal;
    }
    
    // Setters
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
        // Actualiza el precio final si el equipo cambia
        if (equipo != null) {
            this.setPrecioFinal(equipo.getPrecio());
        }
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
}