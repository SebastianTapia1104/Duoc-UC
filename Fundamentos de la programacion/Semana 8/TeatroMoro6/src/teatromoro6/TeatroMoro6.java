package teatromoro6;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class TeatroMoro6 {
    
    // Variables estáticas
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
    static ArrayList<int[]> entradas = new ArrayList<>();                               //USO DE ARRAYLIST
    static LinkedList<Integer> asientosReservados = new LinkedList<>();                 //USO DE LINKEDLIST
    static String[] salidaEntradas = {"VIP", "Platea Baja", "Platea Alta", "Palcos"};   //USO DE LISTAS
    static String[] salidaTarifa = {"Estudiante", "Tercera Edad", "Público General"};
    static int id = 0;

    //Clase principal
    public static void main(String[] args) {
        System.out.println("¡Bienvenido al teatro Moro!");
        int vendiendo = 0;
        while (vendiendo != 7) {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                vendiendo = scanner.nextInt();
                scanner.nextLine();
                switch (vendiendo) {
                    case 1:
                        reservarAsiento();
                        break;
                    case 2:
                        comprarEntrada();
                        break;
                    case 3:
                        editarVenta();
                        break;
                    case 4:
                        mostrarEntradasCompradas();
                        break;
                    case 5:
                        mostrarIngresosTotales();
                        break;
                    case 6:
                        mostrarPreciosYDescuentos();
                        break;
                    case 7:
                        System.out.println("¡Gracias por su compra!");
                        vendiendo = 7;
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
    
    //Menú principal
    static void mostrarMenu() {
        System.out.println("Menú Principal:");
        System.out.println("1. Reservar asiento");
        System.out.println("2. Comprar entrada");
        System.out.println("3. Editar venta");
        System.out.println("4. Visualizar mis compras");
        System.out.println("5. Calcular Ingresos Totales");
        System.out.println("6. Ver precios y descuentos");
        System.out.println("7. Salir");
        System.out.println("***Recuerda que para comprar la entrada de un asiento, este debe estar reservado***");
    }
    
    //FUNCIONES MENÚ
    
    //Reserva
    static void reservarAsiento() {
        mostrarPlanoTeatro();
        try {
            System.out.print("Seleccione el número de asiento que desea reservar: ");
            int asientoParaReservar = scanner.nextInt();
            scanner.nextLine();
            if (asientoParaReservar < 1 || asientoParaReservar > 80) {
                System.out.println("Número de asiento inválido. Intente nuevamente.");
                return;
            }
            if (estaAsientoOcupado(asientoParaReservar)) {
                System.out.println("El asiento " + asientoParaReservar + " ya está vendido. Seleccione otro.");
                return;
            }
            if (estaAsientoReservado(asientoParaReservar)) {
                System.out.println("El asiento " + asientoParaReservar + " ya está reservado.");
                return;
            }
            String zonaAsiento = obtenerZonaAsiento(asientoParaReservar);
            System.out.println("\nConfirmación de reserva:");
            System.out.println("Zona del asiento: " + zonaAsiento);
            System.out.println("Número del asiento: " + asientoParaReservar);
            System.out.println("¿Confirmar la reserva de este asiento?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            System.out.print("Seleccione una opción: ");
            int confirmacion = scanner.nextInt();
            scanner.nextLine();
            switch (confirmacion) {
                case 1:
                    asientos[asientoParaReservar - 1] = 99;
                    asientosReservados.add(asientoParaReservar);
                    System.out.println("El asiento " + asientoParaReservar + " ha sido reservado.");
                    break;
                case 2:
                    System.out.println("La reserva del asiento " + asientoParaReservar + " ha sido cancelada.");
                    break;
                default:
                    System.out.println("Selección inválida. La reserva ha sido cancelada.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para el asiento o la confirmación.");
            scanner.nextLine();
        }
    }
    
    //Compra
    static void comprarEntrada() {
        boolean comprando = true;
        while (comprando) {
            mostrarPlanoTeatro();
            try {
                System.out.print("Seleccione el número de asiento reservado que desea comprar: ");
                int asientoSeleccionado = scanner.nextInt();
                scanner.nextLine();
                if (asientoSeleccionado < 1 || asientoSeleccionado > 80) {
                    System.out.println("Número de asiento inválido. Intente nuevamente.");
                    continue;
                }
                if (!estaAsientoReservado(asientoSeleccionado)) {
                    System.out.println("El asiento " + asientoSeleccionado + " no está reservado. Solo se pueden comprar asientos reservados.");
                    continue;
                }
                if (estaAsientoOcupado(asientoSeleccionado)) {
                    System.out.println("El asiento " + asientoSeleccionado + " ya ha sido vendido.");
                    continue;
                }
                String zonaAsiento = obtenerZonaAsiento(asientoSeleccionado);
                int tipoEntrada = 0; // Inicializamos
                for (int i = 0; i < salidaEntradas.length; i++) {
                    if (salidaEntradas[i].equals(zonaAsiento)) {
                        tipoEntrada = i + 1;
                        break;
                    }
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
                } else if (edad < 1 || edad > 100) {
                    System.out.println("Edad inválida. Intente nuevamente.");
                    continue;
                }
                System.out.println("\nConfirmación de compra:");
                System.out.println("Zona del asiento: " + zonaAsiento);
                System.out.println("Número del asiento: " + asientoSeleccionado);
                System.out.println("Precio base: $" + tarifas[tipoEntrada - 1]);
                System.out.println("Tipo de tarifa: " + salidaTarifa[tipoTarifa]);
                System.out.println("Descuento aplicado: $" + (int) descuento + " (" + (int) descuentoporciento + "%)");
                System.out.println("Precio final a pagar: $" + (int) valorEntrada);
                System.out.println("¿Confirmar la compra de esta entrada?");
                System.out.println("1. Sí");
                System.out.println("2. No");
                System.out.print("Seleccione una opción: ");
                int confirmacionCompra = scanner.nextInt();
                scanner.nextLine();
                switch (confirmacionCompra) {
                    case 1:
                        id += 1;
                        int[] ent = {asientoSeleccionado, tipoEntrada - 1, (int) tarifas[tipoEntrada - 1], tipoTarifa, (int) descuento, (int) valorEntrada, (int) descuentoporciento, id};
                        entradas.add(ent);
                        asientos[asientoSeleccionado - 1] = 0; // Marca el asiento como vendido
                        asientosReservados.remove(Integer.valueOf(asientoSeleccionado)); // Remueve de reservados
                        System.out.println("¡Compra realizada con éxito!");
                        mostrarEntradaComprada(ent);
                        comprando = false;
                        break;
                    case 2:
                        System.out.println("La compra ha sido cancelada.");
                        comprando = false;
                        break;
                    default:
                        System.out.println("Selección inválida. La compra ha sido cancelada.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                scanner.nextLine();
            }
        }
    }
    
    
    //Menú edición
    static void editarVenta() {
        System.out.println("\n¿Qué desea hacer?");
        System.out.println("1. Borrar una reserva");
        System.out.println("2. Borrar una entrada vendida");
        System.out.println("3. Cambiar una reserva a un asiento disponible");
        System.out.println("4. Cambiar una entrada vendida a un asiento reservado");
        System.out.println("5. Cancelar y volver al menú principal");
        System.out.print("Seleccione una opción: ");
        try {
            int opcionEditar = scanner.nextInt();
            scanner.nextLine();

            switch (opcionEditar) {
                case 1:
                    borrarReserva();
                    break;
                case 2:
                    borrarVenta();
                    break;
                case 3:
                    cambiarReservaADisponible();
                    break;
                case 4:
                    cambiarVentaAReservado();
                    break;
                case 5:
                    System.out.println("Volviendo al menú principal.");
                    break;
                default:
                    System.out.println("Opción inválida. Volviendo al menú principal.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para la opción.");
            scanner.nextLine();
        }
        System.out.println();
    }
    
    //Borrar reserva
    static void borrarReserva() {
        if (asientosReservados.isEmpty()) {
            System.out.println("No hay asientos reservados para borrar.");
            return;
        }
        System.out.println("\nPlano del Teatro (Asientos Reservados: [RR])");
        mostrarPlanoTeatro();
        System.out.print("Ingrese el número del asiento reservado que desea liberar: ");
        try {
            int asientoABorrar = scanner.nextInt();
            scanner.nextLine();

            if (asientosReservados.contains(asientoABorrar)) {
                asientos[asientoABorrar - 1] = asientoABorrar;
                asientosReservados.remove(Integer.valueOf(asientoABorrar));
                System.out.println("La reserva del asiento " + asientoABorrar + " ha sido eliminada.");
            } else {
                System.out.println("El asiento ingresado no está en la lista de asientos reservados. Intente nuevamente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para el asiento.");
            scanner.nextLine();
        }
    }
    
    //Borrar venta
    static void borrarVenta() {
        if (entradas.isEmpty()) {
            System.out.println("No hay entradas vendidas para borrar.");
            return;
        }
        mostrarEntradasCompradas();
        try {
            System.out.print("Ingrese el número de entrada que desea eliminar: ");
            int eliminaEntrada = scanner.nextInt();
            scanner.nextLine();
            if (eliminaEntrada > 0 && eliminaEntrada <= entradas.size()) {
                int[] entradaEliminada = entradas.remove(eliminaEntrada - 1);
                asientos[entradaEliminada[0] - 1] = entradaEliminada[0];
                System.out.println("Entrada número " + eliminaEntrada + " (asiento " + entradaEliminada[0] + ") eliminada exitosamente.");
            } else {
                System.out.println("Número de entrada inválido. Intente nuevamente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para la entrada a eliminar.");
            scanner.nextLine();
        }
    }
    
    //Cambiar venta
    static void cambiarReservaADisponible() {
        if (asientosReservados.isEmpty()) {
            System.out.println("No hay asientos reservados para cambiar.");
            return;
        }
        System.out.println("\nPlano del Teatro (Asientos Reservados: [RR], Disponibles: [número])");
        mostrarPlanoTeatro();
        System.out.print("Ingrese el número del asiento reservado que desea mover: ");
        try {
            int asientoReservadoACambiar = scanner.nextInt();
            scanner.nextLine();
            if (asientosReservados.contains(asientoReservadoACambiar)) {
                System.out.print("Ingrese el número del asiento disponible al que desea mover la reserva: ");
                int nuevoAsientoDisponible = scanner.nextInt();
                scanner.nextLine();

                if (nuevoAsientoDisponible >= 1 && nuevoAsientoDisponible <= 80 && !estaAsientoOcupado(nuevoAsientoDisponible) && !estaAsientoReservado(nuevoAsientoDisponible)) {
                    asientos[asientoReservadoACambiar - 1] = asientoReservadoACambiar;
                    asientos[nuevoAsientoDisponible - 1] = 99;
                    asientosReservados.remove(Integer.valueOf(asientoReservadoACambiar));
                    asientosReservados.add(nuevoAsientoDisponible);
                    System.out.println("La reserva del asiento " + asientoReservadoACambiar + " ha sido movida al asiento disponible " + nuevoAsientoDisponible + ".");
                } else {
                    System.out.println("El asiento ingresado no es un asiento disponible válido. Intente nuevamente.");
                }
            } else {
                System.out.println("El asiento ingresado no está en la lista de asientos reservados. Intente nuevamente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para el asiento.");
            scanner.nextLine();
        }
    }
    
    //Cambiar venta
    static void cambiarVentaAReservado() {
        if (entradas.isEmpty()) {
            System.out.println("No hay entradas vendidas para cambiar.");
            return;
        }
        if (asientosReservados.isEmpty()) {
            System.out.println("No hay asientos reservados disponibles para realizar el cambio.");
            return;
        }
        System.out.println("\nPlano del Teatro (Asientos Vendidos: [XX], Reservados: [RR])");
        mostrarPlanoTeatro();
        try {
            System.out.print("Ingrese el número del asiento vendido que desea cambiar: ");
            int asientoVendidoACambiar = scanner.nextInt();
            scanner.nextLine();
            boolean encontrado = false;
            int indiceEntrada = -1;
            for (int i = 0; i < entradas.size(); i++) {
                if (entradas.get(i)[0] == asientoVendidoACambiar && estaAsientoOcupado(asientoVendidoACambiar)) {
                    indiceEntrada = i;
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                System.out.print("Ingrese el número del asiento reservado al que desea cambiar la venta: ");
                int nuevoAsientoReservado = scanner.nextInt();
                scanner.nextLine();
                if (estaAsientoReservado(nuevoAsientoReservado) && !estaAsientoOcupado(nuevoAsientoReservado)) {
                    int[] entradaAntigua = entradas.get(indiceEntrada);
                    asientos[entradaAntigua[0] - 1] = entradaAntigua[0];
                    asientos[nuevoAsientoReservado - 1] = 0;
                    asientosReservados.remove(Integer.valueOf(nuevoAsientoReservado));
                    String nuevaZona = obtenerZonaAsiento(nuevoAsientoReservado);
                    int nuevoTipoEntrada = 0;
                    for (int i = 0; i < salidaEntradas.length; i++) {
                        if (salidaEntradas[i].equals(nuevaZona)) {
                            nuevoTipoEntrada = i + 1;
                            break;
                        }
                    }
                    double descuentoPorcentaje = entradaAntigua[6] / 100.0;
                    double nuevoPrecioBase = tarifas[nuevoTipoEntrada - 1];
                    double nuevoDescuento = nuevoPrecioBase * descuentoPorcentaje;
                    double nuevoPrecioFinal = nuevoPrecioBase - nuevoDescuento;
                    int[] entradaActualizada = entradaAntigua.clone();
                    entradaActualizada[0] = nuevoAsientoReservado;
                    entradaActualizada[1] = nuevoTipoEntrada - 1;
                    entradaActualizada[2] = (int) nuevoPrecioBase;
                    entradaActualizada[4] = (int) nuevoDescuento;
                    entradaActualizada[5] = (int) nuevoPrecioFinal;
                    entradas.set(indiceEntrada, entradaActualizada);
                    System.out.println("La venta del asiento " + asientoVendidoACambiar + " ha sido cambiada al asiento reservado " + nuevoAsientoReservado + " (Zona: " + nuevaZona + ", Precio: $" + (int)nuevoPrecioFinal + ").");
                } else {
                    System.out.println("El asiento ingresado no está reservado o ya está vendido. Intente nuevamente.");
                }
            } else {
                System.out.println("No se encontró una venta para el asiento ingresado. Intente nuevamente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido para el asiento.");
            scanner.nextLine();
        }
    }
    
    //Ingresos de ventas
    static void mostrarIngresosTotales() {
        double ingresosTotales = 0;
        for (int[] entrada : entradas) {
            ingresosTotales += entrada[5]; // El precio final está en el índice 5
        }
        System.out.println("================================");
        System.out.printf("Ingresos totales por ventas: $%.0f\n", ingresosTotales);
        System.out.println("================================");
    }
    
    //Precios y descuentos
    static void mostrarPreciosYDescuentos() {
        System.out.println("\n=====================================================");
        System.out.println("                   Precios de Entradas");
        System.out.println("======================================================");
        for (int i = 0; i < salidaEntradas.length; i++) {
            System.out.printf("%s: $%d\n", salidaEntradas[i], (int) tarifas[i]);
        }
        System.out.println("=====================================================");
        System.out.println("                  Descuentos Disponibles");
        System.out.println("======================================================");
        System.out.println("Estudiante (Menor de 18 años): 10% de descuento");
        System.out.println("Tercera edad (Mayor de 60 años): 15% de descuento");
        System.out.println("Público General: Sin descuento");
        System.out.println("======================================================\n");
    }
    
    //AUXILIARES
    
    //Mapa teatro
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
                    switch (asientoActual) {
                        case 0:
                            System.out.print("[XX] "); // Asiento vendido
                            break;
                        case 99:
                            System.out.print("[RR] "); // Asiento reservado
                            break;
                        default:
                            System.out.printf("[%02d] ", asientoActual); // Asiento disponible
                            break;
                    }
                    index++;
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    static boolean estaAsientoOcupado(int asiento) {
        return asientos[asiento - 1] == 0;
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
            System.out.println("  ID de venta: " + ent[7]);
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
            System.out.println("  ID de venta: " + ent[7]);
            System.out.println("  Precio base: $" + ent[2]);
            System.out.println("  Tipo de tarifa: " + salidaTarifa[ent[3]]);
            System.out.println("  Porcentaje de descuento: " + ent[6] + "%");
            System.out.println("  Descuento aplicado: $" + ent[4]);
            System.out.println("  Precio final a pagar: $" + ent[5]);
            System.out.println("------------------------");
            contadorEntradas++;
        }
    }
    
    static boolean estaAsientoReservado(int asiento) {
        return asientos[asiento - 1] == 99;
    }
    
    static String obtenerZonaAsiento(int asiento) {
        if (asiento >= 1 && asiento <= 20) {
            return salidaEntradas[0]; // VIP
        } else if (asiento >= 21 && asiento <= 40) {
            return salidaEntradas[1]; // Platea Baja
        } else if (asiento >= 41 && asiento <= 60) {
            return salidaEntradas[2]; // Platea Alta
        } else if (asiento >= 61 && asiento <= 80) {
            return salidaEntradas[3]; // Palcos
        } else {
            return "Esta zona no existe";
        }
    }
}