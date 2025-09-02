package com.sebastiantapia.patronesarquitectonicos.controladores;

import com.sebastiantapia.patronesarquitectonicos.vistas.VistaMain;
import com.sebastiantapia.patronesarquitectonicos.vistas.VistaAdmin;
import com.sebastiantapia.patronesarquitectonicos.vistas.VistaCajero;
import com.sebastiantapia.patronesarquitectonicos.vistas.VistaCliente;
import com.sebastiantapia.patronesarquitectonicos.modelos.usuarios.Admin;
import com.sebastiantapia.patronesarquitectonicos.modelos.usuarios.Cajero;
import com.sebastiantapia.patronesarquitectonicos.modelos.usuarios.Cliente;
import com.sebastiantapia.patronesarquitectonicos.modelos.tienda.Tienda;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ControladorMain {
    private VistaMain vistaPrincipal;
    private Scanner scanner;
    
    private Admin admin;
    private Cajero cajero;
    private Cliente cliente;
    private VistaAdmin vistaAdmin;
    private VistaCajero vistaCajero;
    private VistaCliente vistaCliente;
    private Tienda tienda;

    public ControladorMain() {
        this.vistaPrincipal = new VistaMain();
        this.scanner = new Scanner(System.in);
        
        this.admin = new Admin("Sebastian", "admin123");
        this.cajero = new Cajero("Aylen", "cajero456");
        this.cliente = new Cliente("Carlos");
        this.vistaAdmin = new VistaAdmin();
        this.vistaCajero = new VistaCajero();
        this.vistaCliente = new VistaCliente();
        this.tienda = new Tienda();
    }

    public void iniciar() {
        int tipoUsuario = -1;
        do {
            vistaPrincipal.mostrarMenuPrincipal();
            try {
                tipoUsuario = scanner.nextInt();
                scanner.nextLine();

                switch (tipoUsuario) {
                    case 1:
                        vistaPrincipal.mostrarFormalidad("admin123");
                        vistaPrincipal.pedirContrasena();
                        String contrasenaAdmin = scanner.nextLine();
                        if (admin.validarContrasena(contrasenaAdmin)) {
                            ControladorAdmin controladorAdmin = new ControladorAdmin(admin, vistaAdmin, tienda, scanner);
                            controladorAdmin.iniciar();
                        } else {
                            vistaPrincipal.mostrarMensaje("Contrasena incorrecta.");
                        }
                        break;
                    case 2:
                        vistaPrincipal.mostrarFormalidad("cajero456");
                        vistaPrincipal.pedirContrasena();
                        String contrasenaCajero = scanner.nextLine();
                        if (cajero.validarContrasena(contrasenaCajero)) {
                            ControladorCajero controladorCajero = new ControladorCajero(cajero, vistaCajero, tienda, cliente.getCarrito(), scanner);
                            controladorCajero.iniciar();
                        } else {
                            vistaPrincipal.mostrarMensaje("Contrasena incorrecta.");
                        }
                        break;
                    case 3:
                        ControladorCliente controladorCliente = new ControladorCliente(cliente, vistaCliente, tienda, scanner);
                        controladorCliente.iniciar();
                        break;
                    case 4:
                        vistaPrincipal.mostrarMensaje("Saliendo del programa.");
                        break;
                    default:
                        vistaPrincipal.mostrarMensaje("Opcion no valida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                vistaPrincipal.mostrarMensaje("Entrada no valida. Por favor, ingrese un numero entero.");
                scanner.nextLine();
                tipoUsuario = -1;
            }
        } while (tipoUsuario != 4);
        scanner.close();
    }
}