package com.sebastian.bank_europe.main;

import com.sebastian.bank_europe.modelos.clientes.Cliente; 
import com.sebastian.bank_europe.modelos.cuentas.CuentaBancaria; 
import com.sebastian.bank_europe.modelos.cuentas.CuentaCorriente;
import com.sebastian.bank_europe.modelos.cuentas.CuentaAhorro;
import com.sebastian.bank_europe.modelos.cuentas.CuentaDigital;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Bank_Europe {

    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Cliente> clientes = new ArrayList<>(); // Lista de clientes (Provicional)

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenuPrincipal();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número para la opción.");
                scanner.nextLine();
                opcion = 0;
                continue;
            }
            switch (opcion) {
                case 1:
                    registrarOAnadirCuentaACliente();
                    break;
                case 2:
                    verDatosCliente();
                    break;
                case 3:
                    realizarOperacionEnCuenta("Depositar");
                    break;
                case 4:
                    realizarOperacionEnCuenta("Girar");
                    break;
                case 5:
                    realizarOperacionEnCuenta("Consultar Saldo");
                    break;
                case 6:
                    System.out.println("Saliendo de la aplicación. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (opcion != 6); // Mantenemos el código en loop hasta que se seleccione la opcion 6
        scanner.close();
    }

    // Menú principal
    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal Bank Europe ---");
        System.out.println("1. Registrar cliente o Abrir nueva cuenta");
        System.out.println("2. Ver datos de cliente");
        System.out.println("3. Depositar dinero");
        System.out.println("4. Girar dinero");
        System.out.println("5. Consultar saldo de cuenta");
        System.out.println("6. Salir");
        System.out.print("Ingrese una opción: ");
    }

    // Busqueda de cliente y si tiene cuenta
    private static void registrarOAnadirCuentaACliente() { // Se busca por rut
        System.out.println("\n--- Registrar Cliente o Abrir Nueva Cuenta ---");
        int rutBuscar = obtenerEnteroValido("Ingrese el RUT del cliente (solo números, sin puntos ni digito verificador): ");
        Cliente clienteEncontrado = buscarClientePorRut(rutBuscar);
        if (clienteEncontrado == null) { // Se busca el cliente
            System.out.println("Cliente no encontrado. Procediendo a registrar un nuevo cliente.");
            registrarNuevoCliente(rutBuscar);
        } else {
            if (clienteEncontrado.getCuentas().isEmpty()) { // Si no tiene cuenta
                System.out.println("Cliente '" + clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellidoPaterno() + "' encontrado.");
                System.out.println("El cliente no tiene cuentas. Procediendo a abrir una nueva cuenta.");
                abrirNuevaCuenta(clienteEncontrado);
            } else { // Si tiene cuenta
                System.out.println("Cliente '" + clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellidoPaterno() + "' encontrado.");
                System.out.println("ATENCIÓN: Este cliente ya tiene una cuenta asociada. Cada cliente puede tener una sola cuenta contratada.");
                System.out.println("No se puede abrir una nueva cuenta para este cliente.");
            }
        }
    }

    private static void registrarNuevoCliente(int rutExistente) { // Rut ya guardado en petición anterior
        System.out.println("\n--- Formulario de Registro de Nuevo Cliente ---");
        String nombre, apellidoPaterno, apellidoMaterno, domicilio, comuna, dvCaracter; // Solo validación de que no este vacío
        int telefonoNumerico;
        nombre = obtenerStringNoVacio("Ingrese nombre: ");
        apellidoPaterno = obtenerStringNoVacio("Ingrese apellido paterno: ");
        apellidoMaterno = obtenerStringNoVacio("Ingrese apellido materno: ");
        domicilio = obtenerStringNoVacio("Ingrese domicilio: ");
        comuna = obtenerStringNoVacio("Ingrese comuna: ");
        boolean telefonoValido = false;
        do { // Validación de número válido
            telefonoNumerico = obtenerEnteroValido("Ingrese teléfono (9 dígitos, ej: 912345678): ");
            if (String.valueOf(telefonoNumerico).length() != 9 || !String.valueOf(telefonoNumerico).startsWith("9")) {
                System.out.println("El teléfono debe tener exactamente 9 dígitos y comenzar con 9.");
            } else if (telefonoNumerico <= 0) {
                System.out.println("El teléfono debe ser un número positivo.");
            } else {
                telefonoValido = true;
            }
        } while (!telefonoValido);
        boolean dvValido = false;
        do { // Validación de carácter válido
            System.out.print("Ingrese Dígito Verificador del RUT (número 0-9 o 'K'): ");
            dvCaracter = scanner.nextLine().trim().toUpperCase();
            if (dvCaracter.isEmpty()) {
                System.out.println("El Dígito Verificador no puede estar vacío.");
            } else if (!dvCaracter.matches("[0-9K]")) {
                System.out.println("Dígito Verificador inválido. Debe ser un número (0-9) o la letra 'K'.");
            } else {
                dvValido = true;
            }
        } while (!dvValido);
        Cliente cliente = new Cliente(rutExistente, dvCaracter, nombre, apellidoPaterno, apellidoMaterno, domicilio, comuna, telefonoNumerico);
        clientes.add(cliente); // Cliente guardado
        System.out.println("Cliente '" + cliente.getNombre() + " " + cliente.getApellidoPaterno() + "' registrado exitosamente.");
        abrirNuevaCuenta(cliente); // Abrir cuenta
    }

    private static void abrirNuevaCuenta(Cliente cliente) {
        System.out.println("\n--- Apertura de Nueva Cuenta para " + cliente.getNombre() + " ---");
        CuentaBancaria nuevaCuenta = null;
        int tipoCuenta;
        boolean tipoCuentaValido = false;
        do { // Explicación para que sepa que cuenta abrir
            System.out.println("\nSeleccione el tipo de cuenta a abrir:"); // Descripciones de las capacidades de cada cuenta
            System.out.println("------------------------------------------------------------------");
            System.out.println("1. Cuenta Corriente:");
            System.out.println("   - Ideal para uso diario.");
            System.out.println("   - Incluye una línea de crédito asociada para sobregiros.");
            System.out.println("------------------------------------------------------------------");
            System.out.println("2. Cuenta de Ahorro:");
            System.out.println("   - Diseñada para acumular fondos a largo plazo.");
            System.out.println("   - Genera intereses sobre el saldo positivo.");
            System.out.println("------------------------------------------------------------------");
            System.out.println("3. Cuenta Digital Híbrida:");
            System.out.println("   - Combina características de ahorro y crédito.");
            System.out.println("   - Genera intereses sobre el saldo positivo (similar a ahorro).");
            System.out.println("   - Incluye una línea de crédito propia sin necesidad de otra cuenta base.");
            System.out.println("------------------------------------------------------------------");
            System.out.print("Ingrese una opción: ");
            tipoCuenta = obtenerEnteroValido("");
            int numeroCuenta = generarNumeroCuentaUnico();
            double saldoInicial = 0.0;
            System.out.print("Ingrese el saldo inicial de la cuenta: ");
            saldoInicial = obtenerDoubleValido(""); // Saldo para obtener valores futuros
            switch (tipoCuenta) {
                case 1: // Cuenta corriente
                    System.out.print("Ingrese la línea de crédito aprobada para la Cuenta Corriente: ");
                    double lineaCreditoCC = obtenerDoubleValido("");
                    nuevaCuenta = new CuentaCorriente(numeroCuenta, saldoInicial, lineaCreditoCC);
                    tipoCuentaValido = true;
                    break;
                case 2: // Cuenta ahorro
                    nuevaCuenta = new CuentaAhorro(numeroCuenta, saldoInicial);
                    tipoCuentaValido = true;
                    break;
                case 3: // Cuenta digiral
                    System.out.print("Ingrese el límite de crédito asociado para la Cuenta Digital Híbrida: ");
                    double limiteCreditoCD = obtenerDoubleValido("");
                    if (limiteCreditoCD <= 0) {
                        System.out.println("El límite de crédito debe ser un valor positivo. Intente de nuevo.");
                        continue;
                    }
                    nuevaCuenta = new CuentaDigital(numeroCuenta, saldoInicial, limiteCreditoCD);
                    tipoCuentaValido = true;
                    break;
                default:
                    System.out.println("Opción de tipo de cuenta no válida. Por favor, intente de nuevo.");
                    break;
            }
        } while (!tipoCuentaValido);
        if (nuevaCuenta != null) {
            cliente.agregarCuenta(nuevaCuenta);
            System.out.println("Cuenta " + nuevaCuenta.getClass().getSimpleName() + " (Número: " + nuevaCuenta.getNumeroCuenta() + ") abierta exitosamente para " + cliente.getNombre() + ".");
        }
    }

    // Muestra la informacion actual de las cuentas del cliente, luego de validarlo
    private static void verDatosCliente() {
        System.out.println("\n--- Ver Datos de Cliente ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados aún. Por favor, registre un cliente primero.");
            return;
        }
        int rutBuscar = obtenerEnteroValido("Ingrese el RUT del cliente (solo números, sin puntos ni digito verificador): ");
        Cliente clienteEncontrado = buscarClientePorRut(rutBuscar);
        if (clienteEncontrado != null) {
            clienteEncontrado.mostrarInformacionCliente(); // Muestra estadísticas de cálculo de intereses también
        } else {
            System.out.println("Cliente con RUT " + rutBuscar + " no encontrado.");
        }
    }

    // Operaciones bancarias disponibles luego de validar
    private static void realizarOperacionEnCuenta(String tipoOperacion) {
        System.out.println("\n--- " + tipoOperacion + " en Cuenta ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados aún. Por favor, registre un cliente primero.");
            return;
        }
        int rutBuscar = obtenerEnteroValido("Ingrese el RUT del cliente (solo números, sin puntos ni digito verificador): ");
        Cliente clienteEncontrado = buscarClientePorRut(rutBuscar);
        if (clienteEncontrado == null) {
            System.out.println("Cliente con RUT " + rutBuscar + " no encontrado.");
            return;
        }
        if (clienteEncontrado.getCuentas().isEmpty()) {
            System.out.println("El cliente '" + clienteEncontrado.getNombre() + "' no tiene cuentas registradas para realizar esta operación.");
            return;
        }
        CuentaBancaria cuentaSeleccionada = seleccionarCuenta(clienteEncontrado, tipoOperacion.toLowerCase()); // Función de tarea anterior para seleccionar cuenta
        if (cuentaSeleccionada == null) {
            return;
        }
        switch (tipoOperacion) {
            case "Depositar":
                double montoDeposito = obtenerDoubleValido("Ingrese el monto a depositar: ");
                cuentaSeleccionada.depositar(montoDeposito);
                break;
            case "Girar":
                double montoGiro = obtenerDoubleValido("Ingrese el monto a girar: ");
                cuentaSeleccionada.girar(montoGiro);
                break;
            case "Consultar Saldo":
                cuentaSeleccionada.consultarSaldo();  // Muestra estadísticas de cálculo de intereses también
                break;
            default:
                System.out.println("Operación no reconocida.");
        }
    }

    // Buscador de clientes por Rut
    private static Cliente buscarClientePorRut(int rut) {
        for (Cliente c : clientes) {
            if (c.getRut() == rut) {
                return c;
            }
        }
        return null;
    }

    // Validacion de que exista la cuenta con la que se quier trabajar
    private static CuentaBancaria seleccionarCuenta(Cliente cliente, String proposito) { // Función de cuando se podían tener más cuentas (Actualizada)
        List<CuentaBancaria> cuentasDelCliente = cliente.getCuentas();
        if (cuentasDelCliente.isEmpty()) {
            System.out.println("El cliente no tiene cuentas para " + proposito + ".");
            return null;
        }
        System.out.println("\n--- Cuentas disponibles para " + proposito + " de " + cliente.getNombre() + " ---");
        for (int i = 0; i < cuentasDelCliente.size(); i++) {
            CuentaBancaria c = cuentasDelCliente.get(i);
            String tipo = c.getClass().getSimpleName();
            System.out.println((i + 1) + ". " + tipo + " (Número: " + c.getNumeroCuenta() + ")");
        }
        int seleccion = obtenerEnteroValido("Seleccione el número de la cuenta para " + proposito + ": ");
        if (seleccion > 0 && seleccion <= cuentasDelCliente.size()) {
            return cuentasDelCliente.get(seleccion - 1);
        } else {
            System.out.println("Selección de cuenta inválida. Operación cancelada.");
            return null;
        }
    }

    // Generador de número de cuenta
    private static int generarNumeroCuentaUnico() {
        int nuevoNumero;
        boolean esUnico;
        do {
            nuevoNumero = (int) (100000000 + Math.random() * 900000000); // Números de 9 dígitos
            esUnico = true;
            for (Cliente cliente : clientes) {
                for (CuentaBancaria cuenta : cliente.getCuentas()) {
                    if (cuenta.getNumeroCuenta() == nuevoNumero) {
                        esUnico = false;
                        break;
                    }
                }
                if (!esUnico) break;
            }
        } while (!esUnico);
        return nuevoNumero;
    }

    // Validación de texto no vacio
    private static String obtenerStringNoVacio(String mensaje) {
        String input;
        do {
            System.out.print(mensaje);
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("El campo no puede estar vacío. Por favor, intente de nuevo.");
            }
        } while (input.trim().isEmpty());
        return input;
    }

    // Validación de enteros
    private static int obtenerEnteroValido(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese solo números enteros.");
                scanner.nextLine();
            }
        }
    }

    // Validación de doubles
    private static double obtenerDoubleValido(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número decimal o entero válido.");
                scanner.nextLine();
            }
        }
    }
}