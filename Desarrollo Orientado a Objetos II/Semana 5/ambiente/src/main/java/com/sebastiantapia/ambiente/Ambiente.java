package com.sebastiantapia.ambiente;

import com.sebastiantapia.ambiente.modelos.Inventario;
import com.sebastiantapia.ambiente.modelos.MenuPrincipal;
import java.util.Scanner;

public class Ambiente {
    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        Scanner scanner = new Scanner(System.in);
        MenuPrincipal menuPrincipal = new MenuPrincipal(inventario, scanner);

        menuPrincipal.iniciar();

        scanner.close();
    }
}