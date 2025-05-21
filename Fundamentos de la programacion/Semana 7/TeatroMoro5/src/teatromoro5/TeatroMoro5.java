package teatromoro5;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TeatroMoro5 {

    static Scanner scanner = new Scanner(System.in);
    static double[] tarifas = {30000, 15000, 18000, 13000};
    static int[] asientos = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
            61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
            71, 72, 73, 74, 75, 76, 77, 78, 79, 80
    };
    static ArrayList<int[]> entradas = new ArrayList<>();
    static String[] salidaEntradas = {"VIP", "Platea Baja", "Platea Alta", "Palcos"};
    static String[] salidaTarifa = {"Estudiante", "Tercera Edad", "Público General"};

    public static void main(String[] args) {
        System.out.println("¡Bienvenido al teatro Moro!");

        int vendiendo = 0;
        while (vendiendo != 5) {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                vendiendo = scanner.nextInt();
                scanner.nextLine();

                switch (vendiendo) {
                    case 1:
                        comprarEntrada();
                        break;
                    case 2:
                        borrarEntrada();
                        break;
                    case 3:
                        mostrarEntradasCompradas();
                        break;
                    case 4:
                        mostrarIngresosTotales();
                        break;
                    case 5:
                        System.out.println("¡Gracias por su compra!");
                        vendiendo = 5;
                        break;
                    default:
                        System.out.println("Selección inválida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido para la opción del menú.");
                scanner.nextLine();
            }
            System.out.println();
        }
        scanner.close();
    }

    static void mostrarMenu() {
        System.out.println("Menú Principal:");
        System.out.println("1. Comprar entrada");
        System.out.println("2. Borrar entrada");
        System.out.println("3. Visualizar mis compras");
        System.out.println("4. Calcular Ingresos Totales");
        System.out.println("5. Salir");
    }

    static void comprarEntrada() {
        boolean comprando = true;
        while (comprando) {
            mostrarPlanoTeatro();
            try {
                System.out.println("Seleccione el tipo de entrada:");
                System.out.println("1. VIP\n2. Platea Baja\n3. Platea Alta\n4. Palcos");
                int tipoEntrada = scanner.nextInt();

                if (tipoEntrada < 1 || tipoEntrada > 4) {
                    System.out.println("Selección inválida. Intente nuevamente.");
                    continue;
                }

                System.out.println("Seleccione el número de asiento:");
                int asientoSeleccionado = scanner.nextInt();

                if (!esAsientoValido(tipoEntrada, asientoSeleccionado)) {
                    System.out.println("Selección inválida. Intente nuevamente.");
                    continue;
                }

                if (estaAsientoOcupado(asientoSeleccionado)) {
                    System.out.println("Asiento ocupado. Por favor seleccione otro asiento.");
                    continue;
                }

                System.out.println("Ingrese su edad para calcular su tarifa:");
                System.out.println("Estudiante (Menor de 18 años) 10% de descuento\nTercera edad (Mayor de 60 años) 15% de descuento\nPúblico General");
                int edad = scanner.nextInt();

                double descuento = 0;
                double descuentoporciento = 0;
                double valorEntrada = tarifas[tipoEntrada - 1];
                int tipoTarifa = 2;
                if (edad < 18 && edad > 0) {
                    descuento = tarifas[tipoEntrada - 1] * 0.1;
                    valorEntrada *= 0.9;
                    tipoTarifa = 0;
                    descuentoporciento = 10;
                } else if (edad > 60 && edad < 101) {
                    descuento = tarifas[tipoEntrada - 1] * 0.15;
                    valorEntrada *= 0.85;
                    tipoTarifa = 1;
                    descuentoporciento = 15;
                } else if (edad < 0 || edad > 100) {
                    System.out.println("Edad inválida. Intente nuevamente.");
                    continue;
                }
                int[] ent = {asientoSeleccionado, tipoEntrada - 1, (int) tarifas[tipoEntrada - 1], tipoTarifa, (int) descuento, (int) valorEntrada, (int) descuentoporciento};
                entradas.add(ent);
                asientos[asientoSeleccionado - 1] = 0;
                System.out.println("Entrada comprada.");
                mostrarEntradaComprada(ent);
                comprando = false;

            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                scanner.nextLine();
            }
        }
    }

    static void mostrarPlanoTeatro() {
        System.out.println("         =========================");
        System.out.println("         ||     ESCENARIO     ||");
        System.out.println("         ========================\n");

        int index = 0;
        for (String salidaEntrada : salidaEntradas) {
            System.out.printf("             ZONA: %s\n", salidaEntrada);
            for (int fila = 0; fila < 2; fila++) {
                for (int col = 0; col < 10; col++) {
                    int asientoActual = asientos[index];
                    if (asientoActual == 0) {
                        System.out.print("[XX] ");
                    } else {
                        System.out.printf("[%02d] ", asientoActual);
                    }
                    index++;
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    static boolean esAsientoValido(int tipoEntrada, int asiento) {
        return (tipoEntrada == 1 && asiento >= 1 && asiento <= 20) ||
               (tipoEntrada == 2 && asiento >= 21 && asiento <= 40) ||
               (tipoEntrada == 3 && asiento >= 41 && asiento <= 60) ||
               (tipoEntrada == 4 && asiento >= 61 && asiento <= 80);
    }

    static boolean estaAsientoOcupado(int asiento) {
        return asientos[asiento - 1] == 0;
    }

    static void borrarEntrada() {
        if (entradas.isEmpty()) {
            System.out.println("Aún no has comprado entradas.");
            return;
        }
        mostrarEntradasCompradas();
        try {
            System.out.print("Ingrese el número de entrada que desea eliminar: ");
            int eliminaEntrada = scanner.nextInt();
            scanner.nextLine();

            if (eliminaEntrada > 0 && eliminaEntrada <= entradas.size()) {
                int[] entradaEliminada = entradas.remove(eliminaEntrada - 1);
                asientos[entradaEliminada[0] - 1] = entradaEliminada[0]; // Libera el asiento
                System.out.println("Entrada número " + eliminaEntrada + " eliminada exitosamente (asiento " + entradaEliminada[0] + ").");
            } else {
                System.out.println("Número de entrada inválido. Intente nuevamente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para la entrada a eliminar.");
            scanner.nextLine();
        }
    }

    static void mostrarEntradasCompradas() {
        if (entradas.isEmpty()) {
            System.out.println("No hay entradas compradas actualmente.");
            return;
        }
        System.out.println("========================");
        System.out.println("     TUS COMPRAS    ");
        System.out.println("========================");
        mostrarListaDeEntradas(entradas);
        System.out.println("========================");
    }

    static void mostrarEntradaComprada(int[] ent) {
        if (!entradas.isEmpty()) {
            System.out.println("========================");
            System.out.println("         TU COMPRA    ");
            System.out.println("========================");
            System.out.println("  Zona del asiento: " + salidaEntradas[ent[1]]);
            System.out.println("  Número del asiento: " + ent[0]);
            System.out.println("  Precio base: $" + ent[2]);
            System.out.println("  Tipo de tarifa: " + salidaTarifa[ent[3]]);
            System.out.println("  Porcentaje de descuento: " + ent[6] + "%");
            System.out.println("  Descuento aplicado: $" + ent[4]);
            System.out.println("  Precio final a pagar: $" + ent[5]);
            System.out.println("========================");
        }
    }

    static void mostrarListaDeEntradas(ArrayList<int[]> listaEntradas) {
        int contadorEntradas = 1;
        for (int[] ent : listaEntradas) {
            System.out.println("  Entrada número: " + contadorEntradas);
            System.out.println("  Zona del asiento: " + salidaEntradas[ent[1]]);
            System.out.println("  Número del asiento: " + ent[0]);
            System.out.println("  Precio base: $" + ent[2]);
            System.out.println("  Tipo de tarifa: " + salidaTarifa[ent[3]]);
            System.out.println("  Porcentaje de descuento: " + ent[6] + "%");
            System.out.println("  Descuento aplicado: $" + ent[4]);
            System.out.println("  Precio final a pagar: $" + ent[5]);
            System.out.println("------------------------");
            contadorEntradas++;
        }
    }

    static void mostrarIngresosTotales() {
        double ingresosTotales = 0;
        for (int[] entrada : entradas) {
            ingresosTotales += entrada[5]; // El precio final está en el índice 5
        }
        System.out.println("================================");
        System.out.printf("Ingresos totales por ventas: $%.0f\n", ingresosTotales);
        System.out.println("================================");
    }
}