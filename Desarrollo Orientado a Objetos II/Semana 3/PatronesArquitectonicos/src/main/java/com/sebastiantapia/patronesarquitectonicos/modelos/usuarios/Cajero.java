package com.sebastiantapia.patronesarquitectonicos.modelos.usuarios;

import com.sebastiantapia.patronesarquitectonicos.modelos.command.ShoppingCart;
import com.sebastiantapia.patronesarquitectonicos.modelos.decorator.BaseProduct;
import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import com.sebastiantapia.patronesarquitectonicos.modelos.singleton.DiscountManager;
import java.util.Map;
import java.util.Optional;

public class Cajero {
    private String nombre;
    private String contrasena;

    public Cajero(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }
    
    public boolean validarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }

    public void agregarProductoTienda(Tienda tienda, BaseProduct producto) {
        tienda.agregarProducto(producto);
        System.out.println("Cajero " + nombre + " ha agregado el producto " + producto.getNombre() + " a la tienda.");
    }
    
    public void eliminarProductoTienda(Tienda tienda, String nombreProducto) {
        tienda.buscarProducto(nombreProducto).ifPresent(p -> {
            tienda.eliminarProducto(p);
            System.out.println("Cajero " + nombre + " ha eliminado el producto " + p.getNombre() + " de la tienda.");
        });
    }

    public void procesarVenta(Tienda tienda, ShoppingCart carrito) {
        for (String nombreProducto : carrito.getItems().keySet()) {
            int cantidadVendida = carrito.getItems().get(nombreProducto);
            tienda.descontarCantidad(nombreProducto, cantidadVendida);
        }
    }
    
    public void emitirBoleta(Tienda tienda, ShoppingCart carrito) {
        double subtotal = carrito.calcularSubtotal(tienda);
        double total = carrito.calcularTotalConDescuento(tienda, DiscountManager.getInstance());
        double descuentoAplicado = subtotal - total;

        System.out.println("\n--- Boleta de Compra ---");
        System.out.println("Cajero: " + nombre);
        System.out.println("------------------------");
        
        DiscountManager dm = DiscountManager.getInstance();
        String categoriaDescuento20 = dm.getCategoriaConDescuento();
        
        for (Map.Entry<String, Integer> entry : carrito.getItems().entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();
            Optional<BaseProduct> productoOpt = tienda.buscarProducto(nombreProducto);
            if (productoOpt.isPresent()) {
                BaseProduct p = productoOpt.get();
                
                double descuento = dm.getDescuentoGlobal();
                if (p.getCategoria().equalsIgnoreCase(categoriaDescuento20)) {
                    descuento = dm.getDescuentoCategoria();
                }
                double precioUnitarioConDescuento = p.getPrecio() * (1 - descuento);
                double totalLinea = precioUnitarioConDescuento * cantidad;
                
                System.out.printf("Producto: %s\n", p.getNombre());
                System.out.printf("  Categoria: %s\n", p.getCategoria());
                System.out.printf("  Precio original: $%.2f\n", p.getPrecio());
                System.out.printf("  Precio descuento: $%.2f (dto. %.0f%%)\n", precioUnitarioConDescuento, descuento * 100);
                System.out.printf("  Cantidad: %d\n", cantidad);
                System.out.printf("  Total linea: $%.2f\n", totalLinea);
                System.out.println("---");
            }
        }
        
        System.out.println("------------------------");
        System.out.printf("Subtotal: $%.2f\n", subtotal);
        System.out.printf("Descuento total aplicado: $%.2f\n", descuentoAplicado);
        System.out.printf("Total a pagar: $%.2f\n", total);
        System.out.println("------------------------\n");
    }
}