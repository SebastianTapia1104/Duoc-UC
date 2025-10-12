package com.sebatapia.computec.modelos;

public class Desktop extends Equipo {

    // Atributos específicos del Desktop
    private String potenciaFuente; // Ej: "500W", "750W 80 Plus Gold"
    private String factorForma;    // Ej: "ATX", "Micro-ATX", "E-ATX"

    // Constructor completo
    public Desktop(int idEquipo, String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                   String potenciaFuente, String factorForma) {
        // Llama al constructor base de Equipo
        super(idEquipo, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Desktop");
        this.potenciaFuente = potenciaFuente;
        this.factorForma = factorForma;
    }

    // Constructor especial de creación
    public Desktop(String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                   String potenciaFuente, String factorForma) {
        // Llama al constructor completo con ID 0 y tipo "Desktop"
        super(0, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Desktop");
        this.potenciaFuente = potenciaFuente;
        this.factorForma = factorForma;
    }

    @Override
    public String getDetalleEspecifico() {
        return String.format("Fuente: %s, Forma: %s", 
                             potenciaFuente, factorForma);
    }

    @Override
    public String getTipoEquipo() {
        return "Desktop";
    }

    // Getters
    public String getPotenciaFuente() {
        return potenciaFuente;
    }

    public String getFactorForma() {
        return factorForma;
    }
    
    // Setters
    public void setPotenciaFuente(String potenciaFuente) {
        this.potenciaFuente = potenciaFuente;
    }
    
    public void setFactorForma(String factorForma) {
        this.factorForma = factorForma;
    }
}