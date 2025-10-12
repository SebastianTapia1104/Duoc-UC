package com.sebatapia.computec.modelos;

public class Laptop extends Equipo {

    // Atributos específicos de la Laptop
    private double tamanoPantalla;
    private boolean esTouch; // Renombrado para seguir convenciones (antes era Touch)
    private int puertosUSB;

    // =======================================================
    // 1. CONSTRUCTOR COMPLETO (Corregido)
    // =======================================================
    public Laptop(int idEquipo, String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                  double tamanoPantalla, boolean esTouch, int puertosUSB) {
        
        // Llama al constructor base de Equipo con 'double' para el precio
        super(idEquipo, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Laptop"); 
        
        this.tamanoPantalla = tamanoPantalla;
        this.esTouch = esTouch;
        this.puertosUSB = puertosUSB;
    }

    // =======================================================
    // 2. CONSTRUCTOR PARA CREACIÓN (Corregido)
    // =======================================================
    public Laptop(String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                  double tamanoPantalla, boolean esTouch, int puertosUSB) {
        
        // Llama al constructor completo con ID 0 y 'double' para el precio
        super(0, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Laptop"); 
        
        this.tamanoPantalla = tamanoPantalla;
        this.esTouch = esTouch;
        this.puertosUSB = puertosUSB;
    }

    // =======================================================
    // 3. Método Abstracto Implementado
    // =======================================================
    @Override
    public String getDetalleEspecifico() {
        return String.format("Pantalla: %.1f\" (%s), USBs: %d", 
                             tamanoPantalla, esTouch ? "Táctil" : "No Táctil", puertosUSB);
    }
    
    @Override
    public String getTipoEquipo() {
        return "Laptop";
    }

    // =======================================================
    // Getters y Setters Específicos
    // =======================================================
    public double getTamanoPantalla() {
        return tamanoPantalla;
    }

    public void setTamanoPantalla(double tamanoPantalla) {
        this.tamanoPantalla = tamanoPantalla;
    }

    public boolean isEsTouch() { // Getter actualizado al nuevo nombre
        return esTouch;
    }

    public void setEsTouch(boolean esTouch) { // Setter actualizado
        this.esTouch = esTouch;
    }

    public int getPuertosUSB() {
        return puertosUSB;
    }

    public void setPuertosUSB(int puertosUSB) {
        this.puertosUSB = puertosUSB;
    }
}