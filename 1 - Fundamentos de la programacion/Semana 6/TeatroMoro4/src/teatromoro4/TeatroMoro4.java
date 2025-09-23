package teatromoro4;

import java.util.Scanner;

public class TeatroMoro4 {

    // Variables de los asientos (1 a 20)
    static String estado1 = "1"; static String comprador1 = ""; static double total1 = 25000;
    static String estado2 = "2"; static String comprador2 = ""; static double total2 = 25000;
    static String estado3 = "3"; static String comprador3 = ""; static double total3 = 25000;
    static String estado4 = "4"; static String comprador4 = ""; static double total4 = 25000;
    static String estado5 = "5"; static String comprador5 = ""; static double total5 = 25000;
    static String estado6 = "6"; static String comprador6 = ""; static double total6 = 20000;
    static String estado7 = "7"; static String comprador7 = ""; static double total7 = 20000;
    static String estado8 = "8"; static String comprador8 = ""; static double total8 = 20000;
    static String estado9 = "9"; static String comprador9 = ""; static double total9 = 20000;
    static String estado10 = "10"; static String comprador10 = ""; static double total10 = 20000;
    static String estado11 = "11"; static String comprador11 = ""; static double total11 = 15000;
    static String estado12 = "12"; static String comprador12 = ""; static double total12 = 15000;
    static String estado13 = "13"; static String comprador13 = ""; static double total13 = 15000;
    static String estado14 = "14"; static String comprador14 = ""; static double total14 = 15000;
    static String estado15 = "15"; static String comprador15 = ""; static double total15 = 15000;
    static String estado16 = "16"; static String comprador16 = ""; static double total16 = 10000;
    static String estado17 = "17"; static String comprador17 = ""; static double total17 = 10000;
    static String estado18 = "18"; static String comprador18 = ""; static double total18 = 10000;
    static String estado19 = "19"; static String comprador19 = ""; static double total19 = 10000;
    static String estado20 = "20"; static String comprador20 = ""; static double total20 = 10000;

    //PRINCIPAL
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== Menú Principal =====");
            System.out.println("1. Reservar asiento");
            System.out.println("2. Comprar entradas");
            System.out.println("3. Modificar una venta existente");
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Salir");
            System.out.println("***Recuerda que para comprar la entrada de un asiento, este debe estar reservado***");
            try {
                opcion = pedirNumeroEntero(scanner, "Seleccione una opción: ", 1, 5);
            } catch (Exception e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                scanner.nextLine(); // Limpiar el buffer
                opcion = 0; // Opción inválida para que se repita el menú
            }

            switch (opcion) {
                case 1:
                    reservarAsiento(scanner);
                    break;
                case 2:
                    comprarAsiento(scanner);
                    break;
                case 3:
                    menuModificarEliminarVenta(scanner);
                    break;
                case 4:
                    System.out.println("\n--- Boletas Vendidas ---");
                    for (int i = 1; i <= 20; i++) {
                        if (obtenerEstadoAsiento(i).equals("X")) {
                            imprimirBoleta(i);
                        }
                    }
                    break;
            }
        } while (opcion != 5);

        scanner.close();
    }

    // FUNCIONES DE OPCIONES
    
    // Función para reservar un asiento solo si está libre
    static void reservarAsiento(Scanner scanner) {
        try {
            mostrarMapaTeatro();

            int asiento = pedirNumeroEntero(scanner, "Ingrese el número de asiento que desea reservar (1-20): ", 1, 20);

            String estadoActual = obtenerEstadoAsiento(asiento);

            if (estadoActual.equals(String.valueOf(asiento))) {
                cambiarEstadoAsiento(asiento, "R");
                System.out.println("El asiento " + asiento + " ha sido reservado exitosamente.");
            } else {
                System.out.println("El asiento " + asiento + " no está disponible para reserva (ya está reservado o vendido).");
            }

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al intentar reservar el asiento. Intente nuevamente.");
        }
    }
    
    // Función para comprar un asiento solo si está reservado
    static void comprarAsiento(Scanner scanner) {
        try {
            mostrarMapaTeatro();

            int asiento = pedirNumeroEntero(scanner, "Ingrese el número de asiento que desea comprar (1-20): ", 1, 20);

            String estadoActual = obtenerEstadoAsiento(asiento);

            if (estadoActual.equals("R")) {
                int edad = pedirNumeroEntero(scanner, "Ingrese la edad del comprador: ", 1, 120);

                String tipoComprador = "adulto";
                double descuento = 0.0;

                if (edad < 18) {
                    tipoComprador = "estudiante";
                    descuento = 0.10;
                } else if (edad > 60) {
                    tipoComprador = "adulto mayor";
                    descuento = 0.15;
                }

                double precioOriginal = obtenerTotalAsiento(asiento);
                double precioConDescuento = precioOriginal * (1 - descuento);

                cambiarEstadoAsiento(asiento, "X");
                asignarComprador(asiento, tipoComprador);
                asignarTotal(asiento, precioConDescuento);

                System.out.println("Compra realizada con éxito. Asiento " + asiento + " comprado por un " + tipoComprador + ".");
                System.out.println("Total pagado: $" + precioConDescuento);

            } else {
                System.out.println("El asiento " + asiento + " no está reservado y por lo tanto no puede ser comprado.");
            }

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al intentar comprar el asiento. Intente nuevamente.");
        }
    }

    // Menú para modificar o eliminar una venta
    static void menuModificarEliminarVenta(Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n--- Modificar o Eliminar Venta ---");
            System.out.println("1. Modificar una venta");
            System.out.println("2. Eliminar una venta");
            System.out.println("3. Volver al menú principal");
            try {
                opcion = pedirNumeroEntero(scanner, "Seleccione una opción: ", 1, 3);
                switch (opcion) {
                    case 1:
                        modificarVenta(scanner);
                        break;
                    case 2:
                        eliminarVenta(scanner);
                        break;
                    case 3:
                        System.out.println("Volviendo al menú principal.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                scanner.nextLine(); // Limpiar el buffer
                opcion = 0; // Opción inválida para que se repita el menú
            }
        } while (opcion != 3);
    }

    // Función para modificar una venta existente
    static void modificarVenta(Scanner scanner) {
        mostrarMapaTeatro();
        System.out.println("\n--- Modificar una venta ---");
        try {
            int asientoActual = pedirNumeroEntero(scanner, "Ingrese el número del asiento vendido que desea cambiar: ", 1, 20);
            String estadoActual = obtenerEstadoAsiento(asientoActual);

            if (!estadoActual.equalsIgnoreCase("X")) {
                System.out.println("El asiento ingresado no está vendido. No se puede modificar.");
                return;
            }

            int nuevoAsiento = pedirNumeroEntero(scanner, "Ingrese el nuevo número de asiento reservado al que desea cambiar: ", 1, 20);
            String estadoNuevo = obtenerEstadoAsiento(nuevoAsiento);
            if (!estadoNuevo.equalsIgnoreCase("R")) {
                System.out.println("El nuevo asiento no está reservado. Seleccione un asiento con estado 'R'.");
                return;
            }

            // Copiar comprador y total
            String tipoComprador = obtenerComprador(asientoActual);
            double totalPagado = obtenerTotalAsiento(asientoActual);

            // Liberar asiento anterior (marcarlo como libre)
            cambiarEstadoAsiento(asientoActual, String.valueOf(asientoActual));
            asignarComprador(asientoActual, "");
            asignarTotal(asientoActual, obtenerPrecioOriginal(asientoActual)); // Restaurar precio base

            // Asignar asiento nuevo (marcarlo como vendido y copiar la información)
            cambiarEstadoAsiento(nuevoAsiento, "X");
            asignarComprador(nuevoAsiento, tipoComprador);
            asignarTotal(nuevoAsiento, totalPagado);

            System.out.println("La venta fue modificada exitosamente.");

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e.getMessage());
        }
    }

    // Función para eliminar una venta
    static void eliminarVenta(Scanner scanner) {
        mostrarMapaTeatro();
        System.out.println("\n--- Eliminar una venta ---");
        try {
            int asientoAEliminar = pedirNumeroEntero(scanner, "Ingrese el número del asiento cuya venta desea eliminar (vendido): ", 1, 20);
            String estadoAEliminar = obtenerEstadoAsiento(asientoAEliminar);

            if (!estadoAEliminar.equalsIgnoreCase("X")) {
                System.out.println("El asiento no ha sido vendido. No se puede eliminar una venta.");
                return;
            }

            // Liberar el asiento
            cambiarEstadoAsiento(asientoAEliminar, String.valueOf(asientoAEliminar));
            asignarComprador(asientoAEliminar, "");
            asignarTotal(asientoAEliminar, obtenerPrecioOriginal(asientoAEliminar)); // Restaurar precio base

            System.out.println("La venta del asiento " + asientoAEliminar + " ha sido eliminada.");

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al intentar eliminar la venta: " + e.getMessage());
        }
    }
    
    static void imprimirBoleta(int numeroAsiento) {
        String zona = obtenerZonaAsiento(numeroAsiento);
        double precioBase = obtenerPrecioOriginal(numeroAsiento);
        String tipoTarifa = obtenerComprador(numeroAsiento);
        double precioPagado = obtenerTotalAsiento(numeroAsiento);
        double descuento = precioBase - precioPagado;

        System.out.println("========================");
        System.out.println("   Zona del asiento: " + zona);
        System.out.println(" Número del asiento: " + numeroAsiento);
        System.out.println("       Precio base: $" + (int)precioBase);
        System.out.println("     Tipo de tarifa: " + tipoTarifa);
        System.out.println(" Descuento aplicado: $" + (int)descuento);
        System.out.println("Precio final a pagar: $" + (int)precioPagado);
        System.out.println("=========================");
    }
    
    // UTILIDADES
    
    static void mostrarMapaTeatro() {
        System.out.println("\n======= Mapa del Teatro =======");
        System.out.println("                   ESCENARIO \n");

        System.out.print("VIP           : ");     mostrarFila(1, 5);
        System.out.print("Platea Baja   : ");     mostrarFila(6, 10);
        System.out.print("Platea Alta   : ");     mostrarFila(11, 15);
        System.out.print("Palcos        : ");     mostrarFila(16, 20);

        System.out.println("\nLeyenda: 'R' = Reservado | 'X' = Vendido | Número = Libre");
    }
    
    static void mostrarFila(int desde, int hasta) {
        for (int i = desde; i <= hasta; i++) {
            System.out.print(obtenerEstadoAsiento(i) + " ");
        }
        System.out.println();
    }
    
    static double obtenerPrecioOriginal(int numero) {
        if (numero >= 1 && numero <= 5) return 25000;
        if (numero >= 6 && numero <= 10) return 20000;
        if (numero >= 11 && numero <= 15) return 15000;
        if (numero >= 16 && numero <= 20) return 10000;
        return 0;
    }
    
    static String obtenerZonaAsiento(int numero) {
        if (numero >= 1 && numero <= 5) return "VIP";
        if (numero >= 6 && numero <= 10) return "Platea Baja";
        if (numero >= 11 && numero <= 15) return "Platea Alta";
        if (numero >= 16 && numero <= 20) return "Palcos";
        return "Desconocida";
    }

    // GETTERS Y SETTERS
    
    static String obtenerEstadoAsiento(int numero) {
        switch (numero) {
            case 1: return estado1;
            case 2: return estado2;
            case 3: return estado3;
            case 4: return estado4;
            case 5: return estado5;
            case 6: return estado6;
            case 7: return estado7;
            case 8: return estado8;
            case 9: return estado9;
            case 10: return estado10;
            case 11: return estado11;
            case 12: return estado12;
            case 13: return estado13;
            case 14: return estado14;
            case 15: return estado15;
            case 16: return estado16;
            case 17: return estado17;
            case 18: return estado18;
            case 19: return estado19;
            case 20: return estado20;
        }
        return "";
    }
    
    static void cambiarEstadoAsiento(int numero, String nuevoEstado) {
        if (numero == 1) estado1 = nuevoEstado;
        if (numero == 2) estado2 = nuevoEstado;
        if (numero == 3) estado3 = nuevoEstado;
        if (numero == 4) estado4 = nuevoEstado;
        if (numero == 5) estado5 = nuevoEstado;
        if (numero == 6) estado6 = nuevoEstado;
        if (numero == 7) estado7 = nuevoEstado;
        if (numero == 8) estado8 = nuevoEstado;
        if (numero == 9) estado9 = nuevoEstado;
        if (numero == 10) estado10 = nuevoEstado;
        if (numero == 11) estado11 = nuevoEstado;
        if (numero == 12) estado12 = nuevoEstado;
        if (numero == 13) estado13 = nuevoEstado;
        if (numero == 14) estado14 = nuevoEstado;
        if (numero == 15) estado15 = nuevoEstado;
        if (numero == 16) estado16 = nuevoEstado;
        if (numero == 17) estado17 = nuevoEstado;
        if (numero == 18) estado18 = nuevoEstado;
        if (numero == 19) estado19 = nuevoEstado;
        if (numero == 20) estado20 = nuevoEstado;
    }
    
    // Devuelve el total original del asiento sin aplicar descuentos
    static double obtenerTotalAsiento(int numero) {
        switch (numero) {
            case 1: return total1;
            case 2: return total2;
            case 3: return total3;
            case 4: return total4;
            case 5: return total5;
            case 6: return total6;
            case 7: return total7;
            case 8: return total8;
            case 9: return total9;
            case 10: return total10;
            case 11: return total11;
            case 12: return total12;
            case 13: return total13;
            case 14: return total14;
            case 15: return total15;
            case 16: return total16;
            case 17: return total17;
            case 18: return total18;
            case 19: return total19;
            case 20: return total20;
            default: return 0.0;
        }
    }
    
    static void asignarComprador(int numero, String tipo) {
        switch (numero) {
            case 1: comprador1 = tipo; break;
            case 2: comprador2 = tipo; break;
            case 3: comprador3 = tipo; break;
            case 4: comprador4 = tipo; break;
            case 5: comprador5 = tipo; break;
            case 6: comprador6 = tipo; break;
            case 7: comprador7 = tipo; break;
            case 8: comprador8 = tipo; break;
            case 9: comprador9 = tipo; break;
            case 10: comprador10 = tipo; break;
            case 11: comprador11 = tipo; break;
            case 12: comprador12 = tipo; break;
            case 13: comprador13 = tipo; break;
            case 14: comprador14 = tipo; break;
            case 15: comprador15 = tipo; break;
            case 16: comprador16 = tipo; break;
            case 17: comprador17 = tipo; break;
            case 18: comprador18 = tipo; break;
            case 19: comprador19 = tipo; break;
            case 20: comprador20 = tipo; break;
        }
    }

    static void asignarTotal(int numero, double nuevoTotal) {
        switch (numero) {
            case 1: total1 = nuevoTotal; break;
            case 2: total2 = nuevoTotal; break;
            case 3: total3 = nuevoTotal; break;
            case 4: total4 = nuevoTotal; break;
            case 5: total5 = nuevoTotal; break;
            case 6: total6 = nuevoTotal; break;
            case 7: total7 = nuevoTotal; break;
            case 8: total8 = nuevoTotal; break;
            case 9: total9 = nuevoTotal; break;
            case 10: total10 = nuevoTotal; break;
            case 11: total11 = nuevoTotal; break;
            case 12: total12 = nuevoTotal; break;
            case 13: total13 = nuevoTotal; break;
            case 14: total14 = nuevoTotal; break;
            case 15: total15 = nuevoTotal; break;
            case 16: total16 = nuevoTotal; break;
            case 17: total17 = nuevoTotal; break;
            case 18: total18 = nuevoTotal; break;
            case 19: total19 = nuevoTotal; break;
            case 20: total20 = nuevoTotal; break;
        }
    }
    
    static String obtenerComprador(int numero) {
        switch (numero) {
            case 1: return comprador1;
            case 2: return comprador2;
            case 3: return comprador3;
            case 4: return comprador4;
            case 5: return comprador5;
            case 6: return comprador6;
            case 7: return comprador7;
            case 8: return comprador8;
            case 9: return comprador9;
            case 10: return comprador10;
            case 11: return comprador11;
            case 12: return comprador12;
            case 13: return comprador13;
            case 14: return comprador14;
            case 15: return comprador15;
            case 16: return comprador16;
            case 17: return comprador17;
            case 18: return comprador18;
            case 19: return comprador19;
            case 20: return comprador20;
            default: return "";
        }
    }
 
    // AUXILIARES
    
    static int pedirNumeroEntero(Scanner scanner, String mensaje, int min, int max) {
        int numero = -1;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            try {
                numero = Integer.parseInt(scanner.nextLine());
                if (numero >= min && numero <= max) {
                    valido = true;
                } else {
                    System.out.println("Por favor, ingrese un número entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
            }
        }
        return numero;
    }
}