package com.sebastiantapia.patronesarquitectonicos.modelos.command;

import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;
import com.sebastiantapia.patronesarquitectonicos.modelos.singleton.DiscountManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShoppingCart {
    private Map<String, Integer> items = new HashMap<>();
    private boolean confirmado = false;

    public void agregarProducto(BaseProduct producto) {
        items.put(producto.getNombre(), items.getOrDefault(producto.getNombre(), 0) + 1);
        System.out.println("Producto " + producto.getNombre() + " agregado al carrito.");
    }
    
    public void eliminarProducto(BaseProduct producto) {
        if (items.containsKey(producto.getNombre())) {
            items.put(producto.getNombre(), items.get(producto.getNombre()) - 1);
            if (items.get(producto.getNombre()) <= 0) {
                items.remove(producto.getNombre());
            }
            System.out.println("Producto " + producto.getNombre() + " eliminado del carrito.");
        }
    }
    
    public void mostrarCarrito(Tienda tienda) {
        if (items.isEmpty()) {
            System.out.println("El carrito esta vacio.");
            return;
        }
        System.out.println("--- Productos en el carrito ---");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Optional<BaseProduct> producto = tienda.buscarProducto(entry.getKey());
            if (producto.isPresent()) {
                System.out.printf("Producto: %s, Categoria: %s, Cantidad: %d\n", 
                                  producto.get().getNombre(), 
                                  producto.get().getCategoria(), 
                                  entry.getValue());
            }
        }
        System.out.println("-----------------------------");
    }
    
    public double calcularSubtotal(Tienda tienda) {
        double subtotal = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();
            Optional<BaseProduct> producto = tienda.buscarProducto(nombreProducto);
            if (producto.isPresent()) {
                subtotal += producto.get().getPrecio() * cantidad;
            }
        }
        return subtotal;
    }
    
    public double calcularTotalConDescuento(Tienda tienda, DiscountManager discountManager) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();
            Optional<BaseProduct> producto = tienda.buscarProducto(nombreProducto);
            if (producto.isPresent()) {
                double precioUnitario = producto.get().getPrecio();
                String categoriaProducto = producto.get().getCategoria();
                // Si la categoría coincide con la del descuento del 20%, se aplica ese descuento
                if (categoriaProducto.equalsIgnoreCase(discountManager.getCategoriaConDescuento())) {
                    total += (precioUnitario * (1 - discountManager.getDescuentoCategoria())) * cantidad;
                } else { // Si no, se aplica el descuento global del 10%
                    total += (precioUnitario * (1 - discountManager.getDescuentoGlobal())) * cantidad;
                }
            }
        }
        return total;
    }
    
    public void finalizarCompra() {
        System.out.println("Compra finalizada. Gracias por su compra!");
        items.clear();
        confirmado = false;
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
    
    public Map<String, Integer> getItems() {
        return items;
    }
}