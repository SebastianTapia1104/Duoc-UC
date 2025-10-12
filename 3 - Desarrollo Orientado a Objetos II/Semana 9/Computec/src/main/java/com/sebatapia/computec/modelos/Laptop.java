package com.sebatapia.computec.modelos;

public class Laptop extends Equipo {

    // Atributos específicos de la Laptop
    private double tamanoPantalla;
    private boolean esTouch; 
    private int puertosUSB;

    // Constructor completo
    public Laptop(int idEquipo, String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                  double tamanoPantalla, boolean esTouch, int puertosUSB) {
        // Llama al constructor base de Equipo
        super(idEquipo, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Laptop"); 
        this.tamanoPantalla = tamanoPantalla;
        this.esTouch = esTouch;
        this.puertosUSB = puertosUSB;
    }

    // Constructor especial de creación
    public Laptop(String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                  double tamanoPantalla, boolean esTouch, int puertosUSB) {
        
        // Llama al constructor completo con ID 0 y 'double' para el precio
        super(0, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Laptop"); 
        
        this.tamanoPantalla = tamanoPantalla;
        this.esTouch = esTouch;
        this.puertosUSB = puertosUSB;
    }

    @Override
    public String getDetalleEspecifico() {
        return String.format("Pantalla: %.1f\" (%s), USBs: %d", 
                             tamanoPantalla, esTouch ? "Táctil" : "No Táctil", puertosUSB);
    }
    
    @Override
    public String getTipoEquipo() {
        return "Laptop";
    }

    // Getters
    public double getTamanoPantalla() {
        return tamanoPantalla;
    }

    public boolean isEsTouch() {
        return esTouch;
    }

    public int getPuertosUSB() {
        return puertosUSB;
    }
    
    // Setters
    public void setTamanoPantalla(double tamanoPantalla) {
        this.tamanoPantalla = tamanoPantalla;
    }

    public void setEsTouch(boolean esTouch) {
        this.esTouch = esTouch;
    }

    public void setPuertosUSB(int puertosUSB) {
        this.puertosUSB = puertosUSB;
    }
}