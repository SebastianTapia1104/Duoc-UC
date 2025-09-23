package com.grupo11.tiendaderopa;

import com.grupo11.tiendaderopa.interfaces.Command;
import com.grupo11.tiendaderopa.modelos.command.AddProductCommand;
import com.grupo11.tiendaderopa.modelos.command.OrderInvoker;
import com.grupo11.tiendaderopa.modelos.command.RemoveProductCommand;
import com.grupo11.tiendaderopa.modelos.command.ShoppingCart;
import com.grupo11.tiendaderopa.modelos.decorator.Decorator;
import com.grupo11.tiendaderopa.modelos.decorator.TenPercDecorator;
import com.grupo11.tiendaderopa.modelos.decorator.TwentyPercDecorator;
import com.grupo11.tiendaderopa.modelos.singelton.DiscountManager;
import com.grupo11.tiendaderopa.interfaces.Component;

public class main {

    public static void main(String[] args) {

        System.out.println("--- DEMOSTRACION DEL PATRON SINGLETON ---");
        // Obtener la única instancia de DiscountManager
        DiscountManager dm1 = DiscountManager.getInstance();
        System.out.println("Instancia 1: " + dm1.getDescuentoGlobal());

        // Cambiar el descuento a través de la primera instancia
        dm1.setDescuentoGlobal(15.0);

        // Obtener la instancia nuevamente (debería ser la misma)
        DiscountManager dm2 = DiscountManager.getInstance();
        System.out.println("Instancia 2 (misma instancia): " + dm2.getDescuentoGlobal());

        // Verificamos que ambas referencias apuntan al mismo objeto
        System.out.println("Es la misma instancia: " + (dm1 == dm2));
        System.out.println("\n");

        System.out.println("--- DEMOSTRACION DEL PATRON DECORATOR ---");
        // Crear un producto base
        Component camisa = new Decorator("Camiseta", 25.0);
        System.out.println(camisa.getDescription() + " - Precio base: $" + camisa.getPrice());

        // Aplicar un descuento del 10%
        Component descuentoCamisa = new TenPercDecorator(camisa);
        System.out.println(descuentoCamisa.getDescription() + " - Precio final: $" + descuentoCamisa.getPrice());

        // Crear otro producto y aplicar un descuento por categoría
        Component pantalones = new Decorator("Pantalon vaquero", 50.0);
        Component descuentoPantalones = new TwentyPercDecorator(pantalones);
        System.out.println(pantalones.getDescription() + " - Precio base: $" + pantalones.getPrice());
        System.out.println(descuentoPantalones.getDescription() + " - Precio final: $" + descuentoPantalones.getPrice());
        System.out.println("\n");
        
        System.out.println("--- DEMOSTRACION DEL PATRON COMMAND ---");
        // El "receptor" de los comandos
        ShoppingCart carrito = new ShoppingCart();
        
        // El "invocador" que gestionará los comandos
        OrderInvoker invoker = new OrderInvoker();

        // Crear los comandos para las acciones
        Command addCamisaCommand = new AddProductCommand(carrito, "Camiseta");
        Command addPantalonesCommand = new AddProductCommand(carrito, "Pantalon vaquero");
        Command removeCamisaCommand = new RemoveProductCommand(carrito, "Camiseta");

        // Agregar los comandos a la lista del invocador
        invoker.addCommand(addCamisaCommand);
        invoker.addCommand(addPantalonesCommand);
        invoker.addCommand(removeCamisaCommand);

        // Ejecutar todos los comandos en la lista
        invoker.executeCommands();

        // Mostrar el estado final del carrito
        carrito.displayItems();
    }
}