package com.sebastiantapia.patronesarquitectonicos.vistas;

public class VistaAdmin {
    public void mostrarMenuAdmin() {
        System.out.println("\n--- Menu Administrador ---");
        System.out.println("1. Agregar producto");
        System.out.println("2. Eliminar producto");
        System.out.println("3. Editar precio de un producto");
        System.out.println("4. Editar cantidad de un producto");
        System.out.println("5. Editar categoria con descuento del 20%");
        System.out.println("6. Mostrar todos los productos de la tienda");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opcion: ");
    }
    
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}