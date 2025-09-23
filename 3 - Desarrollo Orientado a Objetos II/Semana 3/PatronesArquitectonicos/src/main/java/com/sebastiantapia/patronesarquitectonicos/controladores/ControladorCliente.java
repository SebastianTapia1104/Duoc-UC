package com.sebastiantapia.patronesarquitectonicos.controladores;

import com.sebastiantapia.patronesarquitectonicos.vistas.VistaCliente;
import com.sebastiantapia.patronesarquitectonicos.modelos.usuarios.Cliente;
import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.TenPercDecorator;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.TwentyPercDecorator;
import com.sebastiantapia.patronesarquitectonicos.modelos.singleton.DiscountManager;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class ControladorCliente {
    private Cliente modelo;
    private VistaCliente vista;
    private Tienda tienda;
    private Scanner scanner;

    public ControladorCliente(Cliente modelo, VistaCliente vista, Tienda tienda, Scanner scanner) {
        this.modelo = modelo;
        this.vista = vista;
        this.tienda = tienda;
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcion = -1;
        do {
            vista.mostrarMenuCliente();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcion) {
                    case 1:
                        tienda.mostrarProductos();
                        break;
                    case 2:
                        agregarProducto();
                        break;
                    case 3:
                        eliminarProducto();
                        break;
                    case 4:
                        modelo.mostrarCarrito(tienda);
                        break;
                    case 5:
                        confirmarCompra();
                        break;
                    case 6:
                        vista.mostrarMensaje("Saliendo del menu de cliente.");
                        break;
                    default:
                        vista.mostrarMensaje("Opcion no valida. Por favor, ingrese un numero del 1 al 6.");
                }
            } catch (InputMismatchException e) {
                vista.mostrarMensaje("Entrada no valida. Por favor, ingrese un numero entero.");
                scanner.nextLine();
                opcion = -1;
            }
        } while (opcion != 6);
    }

    private void agregarProducto() {
        System.out.print("Ingrese el nombre del producto que desea agregar: ");
        String nombre = scanner.nextLine();
        Optional<BaseProduct> productoOpt = tienda.buscarProducto(nombre);
        if (productoOpt.isPresent()) {
            BaseProduct producto = productoOpt.get();
            if (producto.getCantidad() > 0) {
                String categoriaDescuento = DiscountManager.getInstance().getCategoriaConDescuento();
                
                if (producto.getCategoria().equalsIgnoreCase(categoriaDescuento)) {
                    producto = new TwentyPercDecorator(producto);
                } else {
                    producto = new TenPercDecorator(producto);
                }
                
                modelo.agregarProducto(producto);
            } else {
                vista.mostrarMensaje("No hay stock suficiente para este producto.");
            }
        } else {
            vista.mostrarMensaje("Producto no encontrado. Por favor, intente de nuevo.");
        }
    }
    
    private void eliminarProducto() {
        System.out.print("Ingrese el nombre del producto que desea eliminar del carrito: ");
        String nombre = scanner.nextLine();
        Optional<BaseProduct> productoOpt = tienda.buscarProducto(nombre);
        productoOpt.ifPresentOrElse(
            modelo::eliminarProducto,
            () -> vista.mostrarMensaje("Producto no encontrado en la tienda. No se puede eliminar del carrito.")
        );
    }
    
    private void confirmarCompra() {
        modelo.getCarrito().setConfirmado(true);
        vista.mostrarMensaje("Compra confirmada. Por favor, dirijase a caja para finalizar su compra.");
    }
}