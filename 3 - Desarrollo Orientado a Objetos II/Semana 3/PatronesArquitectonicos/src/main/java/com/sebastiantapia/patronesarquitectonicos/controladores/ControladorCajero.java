package com.sebastiantapia.patronesarquitectonicos.controladores;

import com.sebastiantapia.patronesarquitectonicos.vistas.VistaCajero;
import com.sebastiantapia.patronesarquitectonicos.modelos.usuarios.Cajero;
import com.sebastiantapia.patronesarquitectonicos.modelos.command.ShoppingCart;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;
import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ControladorCajero {
    private Cajero modelo;
    private VistaCajero vista;
    private Tienda tienda;
    private ShoppingCart carrito;
    private Scanner scanner;

    public ControladorCajero(Cajero modelo, VistaCajero vista, Tienda tienda, ShoppingCart carrito, Scanner scanner) {
        this.modelo = modelo;
        this.vista = vista;
        this.tienda = tienda;
        this.carrito = carrito;
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcion = -1;
        do {
            vista.mostrarMenuCajero();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcion) {
                    case 1:
                        agregarProducto();
                        break;
                    case 2:
                        eliminarProducto();
                        break;
                    case 3:
                        if (carrito.isConfirmado()) {
                            modelo.procesarVenta(tienda, carrito);
                            modelo.emitirBoleta(tienda, carrito);
                            carrito.finalizarCompra();
                        } else {
                            vista.mostrarMensaje("El carrito del cliente no ha sido confirmado. Por favor, pida al cliente que confirme su compra.");
                        }
                        break;
                    case 4:
                        tienda.mostrarProductos();
                        break;
                    case 5:
                        vista.mostrarMensaje("Saliendo del menu de cajero.");
                        break;
                    default:
                        vista.mostrarMensaje("Opcion no valida. Por favor, ingrese un numero del 1 al 5.");
                }
            } catch (InputMismatchException e) {
                vista.mostrarMensaje("Entrada no valida. Por favor, ingrese un numero entero.");
                scanner.nextLine();
                opcion = -1;
            }
        } while (opcion != 5);
    }
    
    private void agregarProducto() {
        try {
            System.out.print("Ingrese nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Ingrese categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Ingrese color: ");
            String color = scanner.nextLine();
            System.out.print("Ingrese talla: ");
            String talla = scanner.nextLine();
            System.out.print("Ingrese cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();
            BaseProduct nuevoProducto = new BaseProduct(precio, nombre, categoria, color, talla, cantidad);
            modelo.agregarProductoTienda(tienda, nuevoProducto);
        } catch (InputMismatchException e) {
            vista.mostrarMensaje("Entrada no valida. El precio y la cantidad deben ser numeros.");
            scanner.nextLine();
        }
    }
    
    private void eliminarProducto() {
        System.out.print("Ingrese nombre del producto a eliminar: ");
        String nombre = scanner.nextLine();
        modelo.eliminarProductoTienda(tienda, nombre);
    }
}