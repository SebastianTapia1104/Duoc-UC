package com.sebastiantapia.patronesarquitectonicos.vistas;

public class VistaCajero {
    public void mostrarMenuCajero() {
        System.out.println("\n--- Menu Cajero ---");
        System.out.println("1. Agregar producto a la tienda");
        System.out.println("2. Eliminar producto de la tienda");
        System.out.println("3. Procesar venta del carrito");
        System.out.println("4. Mostrar todos los productos de la tienda");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opcion: ");
    }
    
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}