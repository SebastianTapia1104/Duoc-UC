package com.sebastian.drivequestrentals;

import com.sebastian.drivequestrentals.modelos.Arrendador;
import com.sebastian.drivequestrentals.modelos.Vehiculo;
import com.sebastian.drivequestrentals.modelos.VehiculoCarga;
import com.sebastian.drivequestrentals.modelos.VehiculoPasajeros;
import com.sebastian.drivequestrentals.modelos.GestionVehiculos;
import java.util.ArrayList;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DriveQuestRentals {

    private static final GestionVehiculos gestionVehiculos = new GestionVehiculos();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); 
                switch (opcion) {
                    case 1 -> agregarNuevoVehiculo();
                    case 2 -> gestionVehiculos.listarVehiculos();
                    case 3 -> { // Informar al usuario que las boletas se generan y guardan automáticamente.
                        System.out.println("\nRecuerda que las boletas se generan y guardan automáticamente");
                        System.out.println("Por lo que si quieres revisar con detencion el detalle revisa el documento 'Boletas.txt'");
                        mostrarBoletasGeneradas();
                    }
                    case 4 -> mostrarVehiculosConArriendoLargo();
                    case 5 -> arrendarVehiculoMenu();
                    case 6 -> devolverVehiculoMenu();
                    case 7 -> // Opción para salir
                        System.out.println("Saliendo del programa. ¡Hasta luego!");
                    default -> System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine();
                opcion = 0; // Para asegurar que el bucle continúe
            }
        } while (opcion != 7); // Condición de salida actualizada
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Sistema de Gestión de Flota DriveQuest Rentals ---");
        System.out.println("1. Añadir nuevo vehículo");
        System.out.println("2. Listar todos los vehículos");
        System.out.println("3. Mostrar todas las boletas generadas");
        System.out.println("4. Ver vehículos con arriendo de largo plazo (Más de 7 días)");
        System.out.println("5. Arrendar un vehículo");
        System.out.println("6. Devolver un vehículo");
        System.out.println("7. Salir");
        System.out.println("----------------------------------------------------");
    }

    private static void agregarNuevoVehiculo() {
        System.out.println("\n--- Añadir Nuevo Vehículo ---");
        System.out.print("Tipo de vehículo (carga/pasajeros): ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Patente: ");
        String patente = scanner.nextLine().trim();

        if (gestionVehiculos.buscarVehiculoPorPatente(patente) != null) {
            System.out.println("Error: Ya existe un vehículo con esa patente. No se puede añadir.");
            return;
        }
        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine().trim();
        int anio = 0;
        double precioArriendoDia = 0.0;
        int diasArriendo = 0;
        try {
            System.out.print("Año: ");
            anio = scanner.nextInt();
            System.out.print("Precio de arriendo por día: ");
            precioArriendoDia = scanner.nextDouble();
            System.out.print("Días de arriendo: ");
            diasArriendo = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea después de nextInt/nextDouble
        } catch (InputMismatchException e) {
            System.out.println("Entrada numérica inválida. Por favor, ingrese números para Año, Precio y Días de Arriendo.");
            scanner.nextLine(); // Limpiar el buffer del scanner
            return;
        }
        Vehiculo nuevoVehiculo = null;
        if (tipo.equalsIgnoreCase("carga")) {
            try {
                System.out.print("Capacidad de carga (KG): ");
                double capacidadCargaKG = scanner.nextDouble();
                scanner.nextLine();
                // Constructor de VehiculoCarga actualizado (sin valorBase)
                nuevoVehiculo = new VehiculoCarga(patente, marca, modelo, anio, precioArriendoDia, diasArriendo, capacidadCargaKG);
            } catch (InputMismatchException e) {
                System.out.println("Entrada numérica inválida para capacidad de carga.");
                scanner.nextLine();
                return;
            }
        } else if (tipo.equalsIgnoreCase("pasajeros")) {
            try {
                System.out.print("Número máximo de pasajeros: ");
                int numeroMaximoPasajeros = scanner.nextInt();
                scanner.nextLine();
                // Constructor de VehiculoPasajeros actualizado (sin valorBase)
                nuevoVehiculo = new VehiculoPasajeros(patente, marca, modelo, anio, precioArriendoDia, diasArriendo, numeroMaximoPasajeros);
            } catch (InputMismatchException e) {
                System.out.println("Entrada numérica inválida para número máximo de pasajeros.");
                scanner.nextLine();
                return;
            }
        } else {
            System.out.println("Tipo de vehículo no reconocido. Por favor, ingrese 'carga' o 'pasajeros'.");
            return;
        }
        if (nuevoVehiculo != null) {
            gestionVehiculos.agregarVehiculo(nuevoVehiculo);
            gestionVehiculos.registrarHistorialArriendo(patente, "AGREGADO AL SISTEMA");
        }
    }

    private static void mostrarVehiculosConArriendoLargo() {
        System.out.println("\n--- Vehículos con Arriendo de Largo Plazo (>= 7 días) ---");
        List<Vehiculo> arriendosLargos = gestionVehiculos.obtenerVehiculosArriendoLargo();
        if (arriendosLargos.isEmpty()) {
            System.out.println("No hay vehículos con arriendos de largo plazo actualmente.");
            return;
        }
        String[] headers = {"Tipo", "Patente", "Marca", "Modelo", "Año", "Precio/Día", "Días Arriendo", "Específico", "Estado Arriendo"};
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }
        List<String[]> rows = new ArrayList<>();
        for (Vehiculo vehiculo : arriendosLargos) {
            String[] rowData = vehiculo.mostrarDatos();
            String estadoArriendo = "Disponible"; // Ya sabemos que está arrendado para esta lista
            String diasArriendoInfo = String.valueOf(vehiculo.getDiasArriendo()) + " días"; // Muestra los días de arriendo del objeto
            String arrendadorInfo = "Desconocido";
            for (Arrendador arrendador : gestionVehiculos.getListaArrendadores().values()) {
                 if (arrendador.getVehiculoArrendado() != null && arrendador.getVehiculoArrendado().getPatente().equalsIgnoreCase(vehiculo.getPatente())) {
                    arrendadorInfo = arrendador.getNombre() + " (RUT: " + arrendador.getRut() + ")";
                    break;
                }
            }
            estadoArriendo = "Arrendado a " + arrendadorInfo;
            String[] finalRowData = new String[headers.length];
            System.arraycopy(rowData, 0, finalRowData, 0, 6);
            finalRowData[6] = diasArriendoInfo;
            finalRowData[7] = rowData[7];
            finalRowData[8] = estadoArriendo;
            rows.add(finalRowData);
            for (int i = 0; i < finalRowData.length; i++) {
                if (finalRowData[i] != null && finalRowData[i].length() > columnWidths[i]) {
                    columnWidths[i] = finalRowData[i].length();
                }
            }
        }
        for (int i = 0; i < columnWidths.length; i++) {
            columnWidths[i] = Math.max(columnWidths[i], headers[i].length());
            columnWidths[i] += 2;
        }
        System.out.print("| ");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf("%-" + columnWidths[i] + "s| ", headers[i]);
        }
        System.out.println();
        for (int i = 0; i < headers.length; i++) {
            System.out.print("+");
            for (int j = 0; j < columnWidths[i] + 2; j++) System.out.print("-");
        }
        System.out.println("+");
        for (String[] rowData : rows) {
            System.out.print("| ");
            for (int i = 0; i < rowData.length; i++) {
                System.out.printf("%-" + columnWidths[i] + "s| ", rowData[i]);
            }
            System.out.println();
        }
        for (int i = 0; i < headers.length; i++) {
            System.out.print("+");
            for (int j = 0; j < columnWidths[i] + 2; j++) System.out.print("-");
        }
        System.out.println("+\n");
    }

    private static void arrendarVehiculoMenu() {
        System.out.println("\n--- Arrendar Vehículo ---");
        System.out.print("Ingrese la patente del vehículo a arrendar: (Recuerda respetar las mayúsculas) ");
        String patente = scanner.nextLine().trim();
        if (gestionVehiculos.buscarVehiculoPorPatente(patente) == null) {
            System.out.println("Error: No existe un vehículo con esa patente.");
            return;
        }
        String rutArrendador;
        while (true) {
            System.out.print("Ingrese RUT del arrendador (7 a 9 dígitos numéricos): ");
            rutArrendador = scanner.nextLine().trim();
            if (rutArrendador.matches("^\\d{7,9}$")) {
                break;
            } else {
                System.out.println("Error: El RUT solo debe contener entre 7 y 9 dígitos numéricos. Intente de nuevo.");
            }
        }
        System.out.print("Ingrese nombre del arrendador: ");
        String nombreArrendador = scanner.nextLine().trim();
        System.out.print("Ingrese teléfono del arrendador: ");
        String telefonoArrendador = scanner.nextLine().trim();
        int diasArriendo = 0;
        try {
            System.out.print("Ingrese la cantidad de días de arriendo: ");
            diasArriendo = scanner.nextInt();
            scanner.nextLine();
            if (diasArriendo <= 0) {
                System.out.println("La cantidad de días de arriendo debe ser un número positivo.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número entero para los días de arriendo.");
            scanner.nextLine();
            return;
        }
        if (gestionVehiculos.arrendarVehiculo(patente, rutArrendador, nombreArrendador, telefonoArrendador, diasArriendo)) {
            System.out.println("Arriendo registrado con éxito y boleta generada.");
        } else {
            System.out.println("No se pudo realizar el arriendo. Verifique los datos o si el vehículo/arrendador ya está en uso.");
        }
    }

    private static void devolverVehiculoMenu() {
        System.out.println("\n--- Devolver Vehículo ---");
        System.out.print("Ingrese la patente del vehículo a devolver: ");
        String patente = scanner.nextLine().trim();

        if (gestionVehiculos.devolverVehiculo(patente)) {
            System.out.println("Devolución registrada con éxito.");
        } else {
            System.out.println("No se pudo realizar la devolución. Verifique la patente o el estado del vehículo.");
        }
    }

    private static void mostrarBoletasGeneradas() {
        List<List<String>> boletas = gestionVehiculos.obtenerBoletasGeneradas();
        if (boletas.isEmpty()) {
            System.out.println("No se han generado boletas en esta sesión aún.");
        } else {
            System.out.println("\n--- Todas las Boletas Generadas en Sesión Actual ---");
            for (int i = 0; i < boletas.size(); i++) {
                System.out.println("\nBoleta #" + (i + 1) + ":");
                for (String linea : boletas.get(i)) {
                    System.out.println(linea);
                }
            }
            System.out.println("----------------------------------------------------");
            System.out.println("Las boletas también se han guardado permanentemente en el archivo Boletas.txt");
        }
    }
}