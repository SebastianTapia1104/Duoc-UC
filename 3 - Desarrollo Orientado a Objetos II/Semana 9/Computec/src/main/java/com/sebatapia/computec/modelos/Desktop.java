package com.sebatapia.computec.modelos;

public class Desktop extends Equipo {

    // Atributos específicos del Desktop
    private String potenciaFuente; // Ej: "500W", "750W 80 Plus Gold"
    private String factorForma;    // Ej: "ATX", "Micro-ATX", "E-ATX"

    // =======================================================
    // 1. CONSTRUCTOR COMPLETO (Para cargar desde la BD)
    // =======================================================
    public Desktop(int idEquipo, String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                   String potenciaFuente, String factorForma) {
        
        // Llama al constructor base de Equipo con el tipo "Desktop"
        super(idEquipo, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Desktop");
        
        this.potenciaFuente = potenciaFuente;
        this.factorForma = factorForma;
    }

    // =======================================================
    // 2. CONSTRUCTOR PARA CREACIÓN (Para guardar por primera vez)
    // =======================================================
    public Desktop(String descripcionModelo, String cpu, int discoDuroMB, int ramGB, double precio,
                   String potenciaFuente, String factorForma) {
        
        // Llama al constructor completo con ID 0 y tipo "Desktop"
        super(0, descripcionModelo, cpu, discoDuroMB, ramGB, precio, "Desktop");
        
        this.potenciaFuente = potenciaFuente;
        this.factorForma = factorForma;
    }

    // =======================================================
    // 3. Método Abstracto Implementado
    // =======================================================
    @Override
    public String getDetalleEspecifico() {
        return String.format("Fuente: %s, Forma: %s", 
                             potenciaFuente, factorForma);
    }

    @Override
    public String getTipoEquipo() {
        return "Desktop";
    }

    // =======================================================
    // Getters y Setters Específicos
    // =======================================================
    public String getPotenciaFuente() {
        return potenciaFuente;
    }

    public void setPotenciaFuente(String potenciaFuente) {
        this.potenciaFuente = potenciaFuente;
    }



    public String getFactorForma() {
        return factorForma;
    }

    public void setFactorForma(String factorForma) {
        this.factorForma = factorForma;
    }
}