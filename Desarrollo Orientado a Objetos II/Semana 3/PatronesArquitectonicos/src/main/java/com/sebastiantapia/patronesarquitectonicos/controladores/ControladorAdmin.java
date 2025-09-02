package com.sebastiantapia.patronesarquitectonicos.controladores;

import com.sebastiantapia.patronesarquitectonicos.vistas.VistaAdmin;
import com.sebastiantapia.patronesarquitectonicos.modelos.usuarios.Admin;
import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ControladorAdmin {
    private Admin modelo;
    private VistaAdmin vista;
    private Tienda tienda;
    private Scanner scanner;

    public ControladorAdmin(Admin modelo, VistaAdmin vista, Tienda tienda, Scanner scanner) {
        this.modelo = modelo;
        this.vista = vista;
        this.tienda = tienda;
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcion = -1;
        do {
            vista.mostrarMenuAdmin();
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
                        editarPrecioProducto();
                        break;
                    case 4:
                        editarCantidadProducto();
                        break;
                    case 5:
                        editarDescuentoCategoria();
                        break;
                    case 6:
                        tienda.mostrarProductos();
                        break;
                    case 7:
                        vista.mostrarMensaje("Saliendo del menu de administrador.");
                        break;
                    default:
                        vista.mostrarMensaje("Opcion no valida. Por favor, ingrese un numero del 1 al 7.");
                }
            } catch (InputMismatchException e) {
                vista.mostrarMensaje("Entrada no valida. Por favor, ingrese un numero entero.");
                scanner.nextLine();
                opcion = -1;
            }
        } while (opcion != 7);
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
            modelo.agregarProductoTienda(tienda, precio, nombre, categoria, color, talla, cantidad);
        } catch (InputMismatchException e) {
            vista.mostrarMensaje("Entrada no valida. El precio y la cantidad deben ser numeros.");
            scanner.nextLine();
        }
    }
    
    private void eliminarProducto() {
        System.out.print("Ingrese nombre del producto a eliminar: ");
        String nombre = scanner.nextLine();
        if (modelo.eliminarProductoTienda(tienda, nombre)) {
            vista.mostrarMensaje("Producto " + nombre + " eliminado de la tienda.");
        } else {
            vista.mostrarMensaje("Producto no encontrado. No se pudo eliminar.");
        }
    }

    private void editarPrecioProducto() {
        try {
            System.out.print("Ingrese el nombre del producto para editar el precio: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el nuevo precio: ");
            double nuevoPrecio = scanner.nextDouble();
            scanner.nextLine();
            if (modelo.editarPrecioProducto(tienda, nombre, nuevoPrecio)) {
                vista.mostrarMensaje("El precio del producto " + nombre + " ha sido actualizado.");
            } else {
                vista.mostrarMensaje("Producto no encontrado. No se pudo editar el precio.");
            }
        } catch (InputMismatchException e) {
            vista.mostrarMensaje("Entrada no valida. El precio debe ser un numero.");
            scanner.nextLine();
        }
    }
    
    private void editarCantidadProducto() {
        try {
            System.out.print("Ingrese el nombre del producto para editar la cantidad: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese la nueva cantidad: ");
            int nuevaCantidad = scanner.nextInt();
            scanner.nextLine();
            if (modelo.editarCantidadProducto(tienda, nombre, nuevaCantidad)) {
                vista.mostrarMensaje("La cantidad del producto " + nombre + " ha sido actualizada a " + nuevaCantidad + ".");
            } else {
                vista.mostrarMensaje("Producto no encontrado. No se pudo editar la cantidad.");
            }
        } catch (InputMismatchException e) {
            vista.mostrarMensaje("Entrada no valida. La cantidad debe ser un numero entero.");
            scanner.nextLine();
        }
    }

    private void editarDescuentoCategoria() {
        System.out.print("Ingrese la categoria para aplicar el descuento del 20%: ");
        String categoria = scanner.nextLine();
        modelo.editarCategoriaConDescuento(categoria);
        vista.mostrarMensaje("El descuento del 20% ahora se aplica a la categoria: " + categoria);
    }
}