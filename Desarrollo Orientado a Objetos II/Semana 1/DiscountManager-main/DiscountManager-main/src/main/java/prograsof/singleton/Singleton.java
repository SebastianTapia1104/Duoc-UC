package prograsof.singleton;

import prograsof.singleton.models.DiscountManager;

// Clase Main
public class Singleton {
    
    public static void main(String[] args) {

        // Obtenemos la instancia única
        DiscountManager dm1 = DiscountManager.getInstance();

        // Cambiamos el descuento global
        dm1.setDescuentoGlobal(15.0);

        // En "otro módulo" del sistema...
        DiscountManager dm2 = DiscountManager.getInstance();

        // Comprobamos que es la misma instancia y tiene el mismo valor
        System.out.println("Descuento aplicado d1: " + dm1.getDescuentoGlobal() + "%");
        System.out.println("Descuento aplicado d2: " + dm2.getDescuentoGlobal() + "%");

        // Verificamos que ambas referencias apuntan al mismo objeto
        System.out.println("¿Es la misma instancia? true or false? " + (dm1 == dm2)); // true
    }
}