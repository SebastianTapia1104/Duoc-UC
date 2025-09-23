package teatromoro2;

import java.util.Scanner;
import java.util.ArrayList;

public class TeatroMoro2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido al teatro Moro!");
        // Definir precios de entradas
        double[] tarifas = {30000, 15000, 18000, 13000};
        int[] asientos = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
            61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
            71, 72, 73, 74, 75, 76, 77, 78, 79, 80
        };
        ArrayList<int[]> entradas = new ArrayList();
        // Tipos de entrada y tarifa
        String[] salidaEntradas = {"VIP", "Platea Baja", "Platea Alta", "Palcos"};
        String[] salidaTarifa = {"Estudiante", "Tercera Edad", "Público General"};
        // Variables de entrada de información
        int tipoEntrada = 0;
        int tipoTarifa  = 0;
        double descuento = 0;
        double valorEntrada = 0;
        int asiento = 0;
        int vendiendo = 1;
        int totalPagando;
        int contadorEntradas;
        
        while (vendiendo < 6){
            
            System.out.println("Menú Principal:");
            System.out.println("1. Comprar entrada");
            System.out.println("2. Borrar entrada");
            System.out.println("3. Promociones");
            System.out.println("4. Búsqueda de entradas");
            System.out.println("5. Confirmar entradas y salir");
            System.out.print("Seleccione una opción: ");
            vendiendo = scanner.nextInt();
            
            while (vendiendo < 1 || vendiendo > 5 ) {
                System.out.println("Selección inválida. Intente nuevamente.");
                vendiendo = scanner.nextInt();
            }
            
            if (vendiendo == 5) {
                System.out.println("¿Confirmas la compra de las siguientes entradas?");
                totalPagando = 0;
                contadorEntradas = 1;
                for (int ent[] : entradas) {
                    System.out.println("========================");
                    System.out.println("      Entrada número: " + contadorEntradas);
                    System.out.println("    Zona del asiento: " + salidaEntradas[ent[1]]);
                    System.out.println("  Número del asiento: " + ent[0]);
                    System.out.println("         Precio base: $" + ent[2]);
                    System.out.println("      Tipo de tarifa: " + salidaTarifa[ent[3]]);
                    System.out.println("  Descuento aplicado: $" + ent[4]);
                    System.out.println("Precio final a pagar: $" + ent[5]);
                    System.out.println("=========================");
                    totalPagando += ent[5];
                    contadorEntradas++;
                }
                System.out.println("Precio total de entradas: $" + totalPagando);
                System.out.println("1. Confirmo");
                System.out.println("2. Cancelar");
                int confirmacion = scanner.nextInt();
                while (confirmacion < 1 || confirmacion > 2) {
                    System.out.println("Selección inválida. Intente nuevamente.");
                    confirmacion = scanner.nextInt();
                } 
                
                if (confirmacion == 1) {
                    System.out.println("Entradas compradas. Disfrute la presentación.");
                    totalPagando = 0;
                    contadorEntradas = 1;
                    for (int ent[] : entradas) {
                        System.out.println("========================");
                        System.out.println("      Entrada número: " + contadorEntradas);
                        System.out.println("    Zona del asiento: " + salidaEntradas[ent[1]]);
                        System.out.println("  Número del asiento: " + ent[0]);
                        System.out.println("        Precio base: $" + ent[2]);
                        System.out.println("      Tipo de tarifa: " + salidaTarifa[ent[3]]);
                        System.out.println("  Descuento aplicado: $" + ent[4]);
                        System.out.println("Precio final a pagar: $" + ent[5]);
                        System.out.println("=========================");
                        totalPagando += ent[5];
                        contadorEntradas++;
                    }
                    System.out.println("Precio total de entradas: $" + totalPagando);
                    vendiendo = 6;
                } else {
                    vendiendo = 1;
                }
            } else if (vendiendo == 1) {
                boolean comprando = true;
                boolean tipo = true;
                boolean tarifa = true;
                boolean ocupado = true;
                while (comprando) {
                    System.out.println("            =========================");
                    System.out.println("            ||      ESCENARIO      ||");
                    System.out.println("            =========================\n");

                    int index = 0;
                    for (int z = 0; z < salidaEntradas.length; z++) {
                        System.out.printf("                 ZONA: %s\n", salidaEntradas[z]);
                        for (int fila = 0; fila < 2; fila++) {
                            System.out.print(""); // sangría para centrar
                            for (int col = 0; col < 10; col++) {
                                int asientoActual = asientos[index]; // obtenemos el valor real del asiento
                                if (asientoActual == 0) {
                                    System.out.print("[XX] ");
                                } else {
                                    System.out.printf("[%02d] ", asientoActual);
                                }
                                index++;
                            }
                            System.out.println(); // salto de línea después de cada fila
                        }
                        System.out.println(); // espacio entre zonas
                    }
                    // Solicitar tipo de entrada
                    System.out.println("Seleccione el tipo de entrada:");
                    System.out.println("1. VIP\n2. Platea Baja\n3. Platea Alta\n4. Palcos");

                    while (tipo){
                        boolean seccionOcupada = true;
                        tipoEntrada = scanner.nextInt();
                        // Validar entrada
                        switch (tipoEntrada) {
                            case 1:
                                for (int i = 1; i < 20; i++) {
                                    if (i != 0) {
                                        seccionOcupada = false;
                                    }
                                }   break;
                            case 2:
                                for (int i = 20; i < 40; i++) {
                                    if (i != 0) {
                                        seccionOcupada = false;
                                    }
                                }   break;
                            case 3:
                                for (int i = 40; i < 60; i++) {
                                    if (i != 0) {
                                        seccionOcupada = false;
                                    }
                                }   break;
                            case 4:
                                for (int i = 60; i < 80; i++) {
                                    if (i != 0) {
                                        seccionOcupada = false;
                                    }
                                }   break;
                            default:
                                System.out.println("Selección inválida. Intente nuevamente.");
                                tipoEntrada = scanner.nextInt();
                        }
                        if (seccionOcupada){
                            System.out.println("Sección llena. Por favor seleccione otra.");
                        } else if (!seccionOcupada) {
                            tipo = false;
                        }
                    }

                    while (ocupado) {
                        System.out.println("Seleccione el número de asiento:");
                        asiento = scanner.nextInt();
                        boolean valido = false;
                        // Validar asiento
                        if (tipoEntrada == 1 && (asiento < 21 && asiento > 0)) {
                            ocupado = false;
                            valido = true;
                        } else if (tipoEntrada == 2 && (asiento < 41 && asiento > 20)) {
                            ocupado = false;
                            valido = true;
                        } else if (tipoEntrada == 3 && (asiento < 61 && asiento > 40)) {
                            ocupado = false;
                            valido = true;
                        } else if (tipoEntrada == 4 && (asiento < 81 && asiento > 60)) {
                            ocupado = false;
                            valido = true;
                        } else {
                            System.out.println("Selección inválida. Intente nuevamente.");
                        }
                        //Validar ocuapación
                        if (valido){    
                            if (asiento != asientos[asiento - 1]){
                                System.out.println("Asiento ocupado. Por favor seleccione otro asiento.");
                                ocupado = true;
                            }
                        }    
                    }

                    while (tarifa) {
                        // Solicitar tarifa
                        System.out.println("Ingrese su edad para calcular su tarifa:");
                        System.out.println("Estudiante (Menor de 18 años) 10% de descuento\nTercera edad (Mayor de 60 años) 15% de descuento\nPúblico General");
                        int edad = scanner.nextInt();

                        // Validar entrada
                        if (edad < 19 && edad > 0) {
                            descuento = tarifas[tipoEntrada - 1] * 0.1;
                            valorEntrada = tarifas[tipoEntrada - 1] * 0.9;
                            tipoTarifa = 0;
                            tarifa = false;
                        } else if (edad < 101 && edad > 59) {
                            descuento = tarifas[tipoEntrada - 1] * 0.15;
                            valorEntrada = tarifas[tipoEntrada - 1] * 0.85;
                            tipoTarifa = 1;
                            tarifa = false;
                        } else if (edad < 60 && edad > 18){
                            descuento = 0;
                            valorEntrada = tarifas[tipoEntrada - 1];
                            tipoTarifa = 2;
                            tarifa = false;
                        } else {
                            System.out.println("Edad inválida. Intente nuevamente.");
                        }
                    }

                    entradas.add(new int[] {asiento, tipoEntrada, (int)tarifas[tipoEntrada - 1], tipoTarifa, (int)descuento, (int)valorEntrada});
                    //Añade la entrada a entradas
                    System.out.println("Entradas compradas hasta ahora");
                    totalPagando = 0;
                    contadorEntradas = 1;
                    for (int ent[] : entradas) {
                        System.out.println("========================");
                        System.out.println("      Entrada número: " + contadorEntradas);
                        System.out.println("    Zona del asiento: " + salidaEntradas[ent[1]]);
                        System.out.println("  Número del asiento: " + ent[0]);
                        System.out.println("         Precio base: $" + ent[2]);
                        System.out.println("      Tipo de tarifa: " + salidaTarifa[ent[3]]);
                        System.out.println("  Descuento aplicado: $" + ent[4]);
                        System.out.println("Precio final a pagar: $" + ent[5]);
                        System.out.println("=========================");
                        totalPagando += ent[5];
                        contadorEntradas++;
                    }
                    System.out.println("Precio total de entradas: $" + totalPagando);
                    asientos[asiento-1] = 0;
                    comprando = false;
                }
            
            } else if (vendiendo == 3) {
                System.out.println("===== PROMOCIONES DISPONIBLES =====");
                System.out.println("- Estudiantes (menores de 18 años): 10% de descuento.");
                System.out.println("- Tercera edad (mayores de 60 años): 15% de descuento.");
                System.out.println("- Compra de 3 o más entradas: descuento adicional del 5% en total (informativo).");
                System.out.println("--------------------------------------");
                System.out.println("Tarifas por zona y edad:");
                System.out.printf("%-15s %-20s %-20s %-20s\n", "Zona", "Estudiante", "Tercera Edad", "Público General");

                for (int i = 0; i < tarifas.length; i++) {
                    double base = tarifas[i];
                    double estudiante = base * 0.9;
                    double terceraEdad = base * 0.85;
                    double publico = base;

                    System.out.printf("%-15s $%-19.0f $%-19.0f $%-19.0f\n",
                            salidaEntradas[i],
                            estudiante,
                            terceraEdad,
                            publico
                    );
                }

                System.out.println("=======================================");
                vendiendo = 1;
            } else if (vendiendo == 4) {
                System.out.println("Buscar entradas por:");
                System.out.println("1. Número de asiento");
                System.out.println("2. Zona");
                System.out.println("3. Tipo de tarifa");

                // Aquí agregamos la validación de entrada
                int opcionBusqueda = scanner.nextInt();

                // Validación de la opción seleccionada
                while (opcionBusqueda < 1 || opcionBusqueda > 3) {
                    System.out.println("Selección inválida. Intente nuevamente.");
                    opcionBusqueda = scanner.nextInt();
                }

                ArrayList<int[]> resultados = new ArrayList<>();

                switch (opcionBusqueda) {
                    case 1:
                        System.out.print("Ingrese el número de asiento: ");
                        int asientoBuscado = scanner.nextInt();

                        // Validación del número de asiento
                        while (asientoBuscado < 0 || asientoBuscado > 80) {
                            System.out.println("Número de asiento inválido. Intente nuevamente.");
                            asientoBuscado = scanner.nextInt();
                        }

                        for (int[] ent : entradas) {
                            if (ent[0] == asientoBuscado) {
                                resultados.add(ent);
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Seleccione zona:");
                        System.out.println("1. VIP\n2. Platea Baja\n3. Platea Alta\n4. Palcos");
                        int zonaBuscada = scanner.nextInt();

                        // Validación de zona
                        while (zonaBuscada < 1 || zonaBuscada > 4) {
                            System.out.println("Zona inválida. Intente nuevamente.");
                            zonaBuscada = scanner.nextInt();
                        }

                        for (int[] ent : entradas) {
                            if (ent[1] == zonaBuscada) {
                                resultados.add(ent);
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Seleccione tipo de tarifa:");
                        System.out.println("1. Estudiante\n2. Tercera Edad\n3. Público General");
                        int tarifaBuscada = scanner.nextInt();

                        // Validación de tarifa (ahora validamos entre 1 y 3)
                        while (tarifaBuscada < 1 || tarifaBuscada > 3) {
                            System.out.println("Tarifa inválida. Intente nuevamente.");
                            tarifaBuscada = scanner.nextInt();
                        }

                        for (int[] ent : entradas) {
                            if (ent[3] == tarifaBuscada - 1) {  // Ajustamos la comparación para que coincida con el índice de tarifa
                                resultados.add(ent);
                            }
                        }
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }

                if (resultados.isEmpty()) {
                    System.out.println("No se encontraron entradas con los criterios dados.");
                } else {
                    System.out.println("Entradas encontradas:");
                    int contador = 1;
                    for (int[] ent : resultados) {
                        System.out.println("========================");
                        System.out.println("      Entrada número: " + contador);
                        System.out.println("    Zona del asiento: " + salidaEntradas[ent[1]]);
                        System.out.println("  Número del asiento: " + ent[0]);
                        System.out.println("         Precio base: $" + ent[2]);
                        System.out.println("      Tipo de tarifa: " + salidaTarifa[ent[3]]);
                        System.out.println("  Descuento aplicado: $" + ent[4]);
                        System.out.println("Precio final a pagar: $" + ent[5]);
                        System.out.println("=========================");
                        contador++;
                    }
                }

                vendiendo = 1; // Regresamos al menú principal
            } if (vendiendo == 2 && entradas.size() == 0) { 
                System.out.println("Aún no haz comprado entradas");
            } else if (vendiendo == 2 && entradas.size() >= 0){
                System.out.println("Entradas compradas hasta ahora");
                totalPagando = 0;
                contadorEntradas = 1;
                for (int ent[] : entradas) {
                    System.out.println("========================");
                    System.out.println("      Entrada número: " + contadorEntradas);
                    System.out.println("    Zona del asiento: " + salidaEntradas[ent[1]]);
                    System.out.println("  Número del asiento: " + ent[0]);
                    System.out.println("         Precio base: $" + ent[2]);
                    System.out.println("      Tipo de tarifa: " + salidaTarifa[ent[3]]);
                    System.out.println("  Descuento aplicado: $" + ent[4]);
                    System.out.println("Precio final a pagar: $" + ent[5]);
                    System.out.println("=========================");
                    totalPagando += ent[5];
                    contadorEntradas++;
                }
                System.out.println("Precio total de entradas: $" + totalPagando);
                System.out.println("Ingrese la entrada que desea eliminar:");
                boolean borrando = true;
                while (borrando) {
                    int eliminaEntrada = scanner.nextInt();
                    if (eliminaEntrada <= entradas.size() && eliminaEntrada > 0) {
                        int[] entradaEliminada = entradas.remove(eliminaEntrada - 1);
                        System.out.println("Entrada número " + eliminaEntrada + " eliminada exitosamente (asiento " + entradaEliminada[0] + ").");
                        asientos[entradaEliminada[0] - 1] = entradaEliminada[0]; // libera el asiento
                        borrando = false;
                    } else {
                        System.out.println("Número de entrada inválido. Intente nuevamente.");
                    }
                }
            }
        }
    }
}