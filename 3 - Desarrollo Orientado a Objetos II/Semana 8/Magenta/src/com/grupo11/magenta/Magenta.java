package com.grupo11.magenta;

import com.grupo11.magenta.controladores.ControladorMenu;
import com.grupo11.magenta.controladores.DBConector;

public class Magenta {
    public static void main(String[] args) {
        
        // Probar conexión con la base de datos al iniciar el programa
        DBConector.test();
        
        ControladorMenu controlador = new ControladorMenu();
        controlador.iniciar();
    }
}