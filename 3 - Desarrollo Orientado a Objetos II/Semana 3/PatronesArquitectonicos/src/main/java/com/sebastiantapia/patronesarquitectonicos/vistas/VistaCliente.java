package com.sebastiantapia.patronesarquitectonicos.vistas;

public class VistaCliente {
    public void mostrarMenuCliente() {
        System.out.println("\n--- Menu Cliente ---");
        System.out.println("1. Mostrar productos disponibles");
        System.out.println("2. Agregar producto al carrito");
        System.out.println("3. Eliminar producto del carrito");
        System.out.println("4. Mostrar carrito");
        System.out.println("5. Confirmar compra");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opcion: ");
    }
    
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}