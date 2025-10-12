package com.sebatapia.computec.modelos;

public abstract class Equipo {
    
    // Atributos base
    protected int idEquipo;
    protected String modelo;
    protected String cpu;
    protected int discoDuroGB;
    protected int ramGB;
    protected double precio;
    protected String tipoEquipo;

    // Constructor completo
    public Equipo(int idEquipo, String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio, String tipoEquipo) {
        this.idEquipo = idEquipo;
        this.modelo = descripcionModelo;
        this.cpu = cpu;
        this.discoDuroGB = discoDuroMB;
        this.ramGB = ramGB;
        this.precio = precio; // Ahora es double
        this.tipoEquipo = tipoEquipo;
    }
    
    // Constructor especial de reporte
    public Equipo(int idEquipo, String descripcionModelo) {
        this.idEquipo = idEquipo;
        this.modelo = descripcionModelo;
        this.cpu = "N/A";
        this.discoDuroGB = 0;
        this.ramGB = 0;
        this.precio = 0.0; 
        this.tipoEquipo = "Reporte";
    }
    
    // Constructor especial de creación
    public Equipo(String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio, String tipoEquipo) {
        this(0, descripcionModelo, cpu, discoDuroMB, ramGB, precio, tipoEquipo);
    }

    // Constructor vacío
    public Equipo() {
    }

    // Getters
    public int getIdEquipo() {
        return idEquipo;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCpu() {
        return cpu;
    }

    public int getDiscoDuroGB() {
        return discoDuroGB;
    }

    public int getRamGB() {
        return ramGB;
    }

    public double getPrecio() { // Devuelve double
        return precio;
    }
    
    public String getTipoEquipo() {
        return tipoEquipo;
    }
    
    // Setters
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setDiscoDuroGB(int discoDuroGB) {
        this.discoDuroGB = discoDuroGB;
    }

    public void setRamGB(int ramGB) {
        this.ramGB = ramGB;
    }

    public void setPrecio(double precio) { // Acepta double
        this.precio = precio;
    }
    
    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }
    
    // Método abstracto
    public abstract String getDetalleEspecifico();
}