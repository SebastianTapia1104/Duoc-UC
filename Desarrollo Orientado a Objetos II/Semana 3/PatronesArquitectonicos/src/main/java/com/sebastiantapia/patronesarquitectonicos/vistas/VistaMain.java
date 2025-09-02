package com.sebastiantapia.patronesarquitectonicos.vistas;

public class VistaMain {

    public void mostrarMenuPrincipal() {
        System.out.println("--- Bienvenido al Sistema de Pedidos en Linea ---");
        System.out.println("Por favor, seleccione su rol:");
        System.out.println("1. Administrador");
        System.out.println("2. Cajero");
        System.out.println("3. Cliente");
        System.out.println("4. Salir del programa");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    
    public void pedirContrasena() {
        System.out.print("Ingrese la contrasena: ");
    }
    
    public void mostrarFormalidad(String rol) {
        System.out.println("Recuerda que tu contrasena esta escrita en el papel en la pared (" + rol + ").");
    }
}