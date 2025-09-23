package com.sebastiantapia.patronesarquitectonicos.modelos.tienda;

import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;
import com.sebastiantapia.patronesarquitectonicos.modelos.singleton.DiscountManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tienda {
    private List<BaseProduct> productos;

    public Tienda() {
        this.productos = new ArrayList<>();
        productos.add(new BaseProduct(75.0, "Pantalon Jeans", "Pantalones", "Azul", "L", 10));
        productos.add(new BaseProduct(30.0, "Polera Algodon", "Poleras", "Blanca", "M", 5));
        productos.add(new BaseProduct(150.0, "Chaqueta Cuero", "Chaquetas", "Negra", "XL", 3));
        DiscountManager.getInstance().setCategoriaConDescuento("Pantalones"); // Se establece una categoría por defecto con 20% de descuento
    }

    public void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos en la tienda.");
            return;
        }
        
        DiscountManager dm = DiscountManager.getInstance();
        String categoriaDescuento = dm.getCategoriaConDescuento();
        
        System.out.println("Aviso! Todos los productos tienen un " + (dm.getDescuentoGlobal() * 100) + "% de descuento.");
        
        if (!categoriaDescuento.isEmpty()) {
            System.out.println("Aviso! Ademáas hay un " + (dm.getDescuentoCategoria() * 100) + "% de descuento en la categoría de " + categoriaDescuento + ".");
        }
        
        System.out.println("--- Productos de la Tienda ---");
        for (BaseProduct producto : productos) {
            System.out.println("Nombre: " + producto.getNombre() + 
                               " | Precio: $" + producto.getPrecio() +
                               " | Categoria: " + producto.getCategoria() +
                               " | Talla: " + producto.getTalla() + 
                               " | Cantidad disponible: " + producto.getCantidad());
        }
        System.out.println("-----------------------------");
    }
    
    public void agregarProducto(BaseProduct producto) {
        Optional<BaseProduct> productoExistente = productos.stream()
            .filter(p -> p.equals(producto))
            .findFirst();

        if (productoExistente.isPresent()) {
            BaseProduct p = productoExistente.get();
            p.setCantidad(p.getCantidad() + producto.getCantidad());
            System.out.println("Cantidad de " + p.getNombre() + " actualizada. Nueva cantidad: " + p.getCantidad());
        } else {
            this.productos.add(producto);
            System.out.println("Producto " + producto.getNombre() + " agregado a la tienda.");
        }
    }
    
    public boolean eliminarProducto(BaseProduct producto) {
        return this.productos.remove(producto);
    }

    public Optional<BaseProduct> buscarProducto(String nombre) {
        return productos.stream()
                        .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                        .findFirst();
    }
    
    public Optional<BaseProduct> buscarProductoCompleto(String nombre) {
        return productos.stream()
                        .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                        .findFirst();
    }
    
    public boolean editarPrecio(String nombre, double nuevoPrecio) {
        Optional<BaseProduct> productoExistente = buscarProducto(nombre);
        if (productoExistente.isPresent()) {
            productoExistente.get().setPrecio(nuevoPrecio);
            return true;
        }
        return false;
    }
    
    public boolean editarCantidad(String nombre, int nuevaCantidad) {
        Optional<BaseProduct> productoExistente = buscarProducto(nombre);
        if (productoExistente.isPresent()) {
            productoExistente.get().setCantidad(nuevaCantidad);
            return true;
        }
        return false;
    }

    public void descontarCantidad(String nombreProducto, int cantidad) {
        Optional<BaseProduct> productoExistente = buscarProducto(nombreProducto);
        if (productoExistente.isPresent()) {
            BaseProduct producto = productoExistente.get();
            int nuevaCantidad = producto.getCantidad() - cantidad;
            if (nuevaCantidad >= 0) {
                producto.setCantidad(nuevaCantidad);
                System.out.println("Se han descontado " + cantidad + " unidades de " + nombreProducto + ". Nueva cantidad: " + nuevaCantidad);
            } else {
                System.out.println("No hay suficiente stock para descontar " + cantidad + " unidades de " + nombreProducto);
            }
        }
    }
}