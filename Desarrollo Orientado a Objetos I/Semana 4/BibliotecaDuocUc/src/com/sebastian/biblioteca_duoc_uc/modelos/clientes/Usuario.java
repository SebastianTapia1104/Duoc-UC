package com.sebastian.biblioteca_duoc_uc.modelos.clientes;

public class Usuario {
    
    // ATRIBUTOS
    private int idUsuario;
    private String nombre;
    private int rut; 
    private String dv;
    
    private static int contadorIdUsuario = 1;
    
    //CONSTRUCTOR
    public Usuario(String nombre, int rut, String dv) {
        this.idUsuario = Usuario.contadorIdUsuario; 
        this.nombre = nombre;
        this.rut = rut;
        this.dv = dv;
        Usuario.contadorIdUsuario++; // Incrementa el contador para el próximo usuario
    }

    // GETTERS
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRut() {
        return rut;
    }

    public String getDv() {
        return dv;
    }

    public static int getContadorIdUsuario() {
        return contadorIdUsuario;
    }
    
    // SETTERS
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRut(int rut) {
        this.rut = rut;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public static void setContadorIdUsuario(int contadorIdUsuario) {
        Usuario.contadorIdUsuario = contadorIdUsuario;
    }
    
    // MÉTODOS
    @Override
    public String toString() {
        return "ID Usuario: " + idUsuario + ", Nombre: " + nombre + ", RUT: " + rut + "-" + dv;
    }

    public boolean verificarRutCompleto(int rutIngresado, String dvIngresado) {
        return this.rut == rutIngresado && this.dv.equalsIgnoreCase(dvIngresado);
    }
}