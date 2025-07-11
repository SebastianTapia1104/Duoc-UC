package com.sebastian.safevotesystem.modelos;

import java.io.IOException;
import java.util.Random;

public class PrimesThread implements Runnable {

    private final PrimesList primesList;
    private final int numerosGenerados;
    private static final String CODIGOS_PRIMOS = "src/com/sebastian/safevotesystem/resources/CodigosPrimos.txt";
    private static final String PRIMOS_PRUEBAS = "src/com/sebastian/safevotesystem/resources/NumerosPrimosPruebas.csv";

    public PrimesThread(PrimesList primesList, int numerosGenerados) {
        this.primesList = primesList;
        this.numerosGenerados = numerosGenerados;
    }

    @Override
    public void run() {
        Random random = new Random();
        System.out.println(Thread.currentThread().getName() + " ha comenzado a generar números.");
        for (int i = 0; i < numerosGenerados; i++) {
            int prueba = random.nextInt(999) + 2; // Rango de 2 a 1000
            String pruebaBinario = Archivos.numeroABinario(prueba);
            boolean pruebaPrimo = primesList.isPrime(prueba);
            try {
                if (pruebaPrimo) {
                    try {
                        primesList.add(prueba); 
                        Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + pruebaBinario + ") es primo y fue añadido por " + Thread.currentThread().getName() + ".", true); // Registro en CodigosPrimos.txt
                        Archivos.escritorArchivos(PRIMOS_PRUEBAS, String.valueOf(prueba), true); // Registro en NumerosPrimosPruebas.csv
                    } catch (IllegalArgumentException e) {
                        System.err.println(Thread.currentThread().getName() + " Error inesperado al añadir: " + prueba + " - " + e.getMessage());
                        Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + pruebaBinario + ") es primo pero hubo un error al añadir por " + Thread.currentThread().getName() + ": " + e.getMessage(), true);
                    }
                } else { // Registro si no es primo
                    Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + pruebaBinario + ") no es primo, número descartado por " + Thread.currentThread().getName() + ".", true);
                }
            } catch (IOException e) {
                System.err.println(Thread.currentThread().getName() + " Error de I/O al escribir en el archivo de log/pruebas: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " ha terminado de generar números.");
    }
}