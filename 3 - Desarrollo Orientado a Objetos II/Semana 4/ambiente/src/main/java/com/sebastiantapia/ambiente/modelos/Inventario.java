package com.sebastiantapia.ambiente.modelos;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Inventario {
    private HashMap<String, Producto> productos;

    public Inventario() {
        productos = new HashMap<>();
    }

    // Método para agregar productos al inventario
    public void agregarProducto(Producto producto) {
        if (producto != null && producto.getCodigo() != null) {
            productos.put(producto.getCodigo(), producto);
            System.out.println("Producto '" + producto.getNombre() + "' agregado al inventario.");
        } else {
            System.out.println("Error: No se puede agregar un producto nulo o sin código.");
        }
    }

    // Método para eliminar productos por su código
    public void eliminarProducto(String codigo) {
        if (productos.remove(codigo) != null) {
            System.out.println("Producto con código '" + codigo + "' eliminado del inventario.");
        } else {
            System.out.println("Error: Producto con código '" + codigo + "' no encontrado.");
        }
    }

    // Método para buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productos.values().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());
    }

    // Método para buscar productos por código (retorna un solo producto)
    public Producto buscarPorCodigo(String codigo) {
        return productos.get(codigo);
    }
    
    // Método para listar todos los productos
    public List<Producto> listarTodosLosProductos() {
        return productos.values().stream().collect(Collectors.toList());
    }

    // Método para generar un informe (ejemplo simple)
    public void generarInforme() {
        System.out.println("\n--- Informe de Inventario ---");
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            for (Producto p : productos.values()) {
                System.out.println(p.toString());
            }
        }
        System.out.println("-----------------------------");
    }
}