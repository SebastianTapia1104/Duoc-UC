package com.sebastiantapia.ambiente.modelos;

import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;

public class MenuPrincipal {
    
    private Inventario inventario;
    private Scanner scanner;

    public MenuPrincipal(Inventario inventario, Scanner scanner) {
        this.inventario = inventario;
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea
                ejecutarOpcion(opcion);
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer de entrada
                opcion = -1;
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- Menú de Gestión de Inventario ---");
        System.out.println("1. Agregar un nuevo producto");
        System.out.println("2. Eliminar un producto");
        System.out.println("3. Buscar producto por nombre");
        System.out.println("4. Buscar producto por código");
        System.out.println("5. Listar todos los productos");
        System.out.println("6. Generar informe de inventario");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarProducto();
                break;
            case 2:
                eliminarProducto();
                break;
            case 3:
                buscarPorNombre();
                break;
            case 4:
                buscarPorCodigo();
                break;
            case 5:
                listarProductos();
                break;
            case 6:
                inventario.generarInforme();
                break;
            case 0:
                System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
                break;
        }
    }

    private void agregarProducto() {
        System.out.println("\n--- Agregar Nuevo Producto ---");
        System.out.print("Ingrese el código del producto: ");
        String codigo = scanner.nextLine();
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la descripción del producto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Ingrese el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Ingrese la cantidad en stock: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Producto nuevoProducto = new Producto(codigo, nombre, precio, descripcion, cantidad);
        inventario.agregarProducto(nuevoProducto);
    }

    private void eliminarProducto() {
        System.out.println("\n--- Eliminar Producto ---");
        System.out.print("Ingrese el código del producto a eliminar: ");
        String codigo = scanner.nextLine();
        inventario.eliminarProducto(codigo);
    }

    private void buscarPorNombre() {
        System.out.println("\n--- Buscar Producto por Nombre ---");
        System.out.print("Ingrese el nombre del producto a buscar: ");
        String nombre = scanner.nextLine();
        List<Producto> resultados = inventario.buscarPorNombre(nombre);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron productos con ese nombre.");
        } else {
            System.out.println("Productos encontrados:");
            for (Producto p : resultados) {
                System.out.println(p.toString());
            }
        }
    }

    private void buscarPorCodigo() {
        System.out.println("\n--- Buscar Producto por Código ---");
        System.out.print("Ingrese el código del producto a buscar: ");
        String codigo = scanner.nextLine();
        Producto producto = inventario.buscarPorCodigo(codigo);

        if (producto != null) {
            System.out.println("Producto encontrado:");
            System.out.println(producto.toString());
        } else {
            System.out.println("No se encontró un producto con ese código.");
        }
    }

    private void listarProductos() {
        System.out.println("\n--- Lista de Todos los Productos ---");
        List<Producto> todos = inventario.listarTodosLosProductos();

        if (todos.isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            for (Producto p : todos) {
                System.out.println(p.toString());
            }
        }
    }
}