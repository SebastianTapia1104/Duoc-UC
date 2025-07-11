package com.sebastian.safevotesystem;

import com.sebastian.safevotesystem.modelos.Archivos;
import com.sebastian.safevotesystem.modelos.PrimesList;
import com.sebastian.safevotesystem.modelos.NumberProducer; 
import com.sebastian.safevotesystem.modelos.PrimeConsumer; 
import com.sebastian.safevotesystem.modelos.EventoTopic; 
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue; 
import java.util.concurrent.atomic.AtomicInteger; 

public class SafeVoteSystem {

    private PrimesList primesList;

    // Rutas de los archivos
    private static final String NUMEROS_INICIALES = "src/com/sebastian/safevotesystem/resources/NumerosIniciales.csv";
    private static final String PRIMOS_PRUEBAS = "src/com/sebastian/safevotesystem/resources/NumerosPrimosPruebas.csv";
    private static final String CODIGOS_PRIMOS = "src/com/sebastian/safevotesystem/resources/CodigosPrimos.txt";
    private static final String PRIMOS_FINALES = "src/com/sebastian/safevotesystem/resources/NumerosPrimosFinales.txt";

    public SafeVoteSystem() {
        this.primesList = new PrimesList();
    }

    public void cargaNumeroIniciales() {
        System.out.println("Cargando números primos iniciales desde: " + NUMEROS_INICIALES);
        try {
            List<Integer> numerosIniciales = Archivos.lectorArchivos(NUMEROS_INICIALES);
            for (int num : numerosIniciales) {
                String numeroBinario = Archivos.numeroABinario(num);
                try {
                    primesList.add(num); // Intenta añadir el número a la lista de primos
                    Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + numeroBinario + ") es primo y se añadió a la lista (inicial).", true);
                } catch (IllegalArgumentException e) {
                    Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + numeroBinario + ") no es primo o ya existe, número eliminado/no añadido (inicial).", true);
                    System.err.println("Advertencia al cargar: " + num + " " + e.getMessage());
                }
            }
            System.out.println("Carga inicial completada. Primos en lista: " + primesList.getPrimesCount());
        } catch (IOException e) {
            System.err.println("Error de I/O al cargar números iniciales: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error de formato en números iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Opciones de SafeVoteSystem ---");
            System.out.println("1. Añadir número primo a la lista (Manual)");
            System.out.println("2. Eliminar un número de la lista (por posición)");
            System.out.println("3. Ver números en la lista");
            System.out.println("4. Ver cantidad de números primos en la lista");
            System.out.println("5. Ejecutar hilos Generadores"); // Sin Queue | Usa PrimesThread
            System.out.println("6. Ejecutar hilos Productor/Consumidor"); // Con Queue, Wait/Notify y Topic | Usa NumberProducer/PrimeConsumer
            System.out.println("7. Terminar programa");
            System.out.print("Elige una opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: // Añadir número primo manualmente
                        System.out.print("Introduce el número a añadir: ");
                        int prueba = Integer.parseInt(scanner.nextLine());
                        String pruebaBinaria = Archivos.numeroABinario(prueba);
                        try {
                            primesList.add(prueba);
                            Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + pruebaBinaria + ") es primo y se añadió a la lista (manual).", true);
                            Archivos.escritorArchivos(PRIMOS_PRUEBAS, String.valueOf(prueba), true);
                            System.out.println(prueba + " ha sido añadido a la lista.");
                        } catch (IllegalArgumentException e) {
                            Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + pruebaBinaria + ") no es primo o ya existe, número eliminado/no añadido (manual).", true);
                            System.out.println(prueba + " no es primo y no puede ser añadido, o ya existe.");
                        }
                        break;
                    case 2: // Eliminar un número por posición
                        System.out.print("Introduce la posición del número a eliminar (Parte desde el 1): ");
                        int posicionEliminada = Integer.parseInt(scanner.nextLine());
                        try {
                            Integer primoEliminado = primesList.remove(posicionEliminada - 1);
                            String binarioEliminado = Archivos.numeroABinario(primoEliminado);
                            Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + binarioEliminado + ") fue eliminado de la lista por el usuario.", true);
                            System.out.println("Se eliminó el número " + primoEliminado + " de la posición " + posicionEliminada + ".");
                        } catch (IndexOutOfBoundsException e) {
                            System.err.println("Error: Posición inválida. " + e.getMessage());
                        }
                        break;
                    case 3: // Ver todos los números en la lista
                        System.out.println("\n--- Números actuales en la lista ---");
                        if (primesList.isEmpty()) {
                            System.out.println("La lista está vacía.");
                        } else {
                            System.out.println(primesList.toString());
                        }
                        break;
                    case 4: // Ver la cantidad de números primos
                        System.out.println("Cantidad total de números primos en la lista: " + primesList.getPrimesCount());
                        break;
                    case 5: // Ejecutar el modo de hilos generadores original (sin Queue)
                        runGeneraThreads();
                        break;
                    case 6: // Ejecutar el nuevo modo de hilos productor/consumidor (con Queue, Wait/Notify y Topic)
                        runProduceConsumidorThreads();
                        break;
                    case 7: // Opción para terminar el programa
                        System.out.println("Finalizando el programa...");
                        break;
                    default: // Opción inválida
                        System.out.println("Opción inválida. Por favor, elige un número del 1 al 7.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, introduce un número.");
                opcion = 0; // Para que el bucle continúe en caso de entrada no numérica
            } catch (IOException e) {
                System.err.println("Error de I/O al escribir en el archivo de log: " + e.getMessage());
                e.printStackTrace();
                opcion = 7; // Terminar si hay un error de I/O grave
            }
        } while (opcion != 7);
        scanner.close();
        System.out.println("\n--- Resumen Final ---"); // Acciones finales al terminar el programa
        System.out.println("Lista final de números primos: " + primesList.toString());
        System.out.println("Cantidad total de números primos: " + primesList.getPrimesCount());
        try { // Guardar la lista final de primos en un archivo
            Archivos.escritorArchivos(PRIMOS_FINALES, "Lista Final de Números Primos (" + primesList.getPrimesCount() + " en total):", false); // false para sobreescribir
            for (Integer prime : primesList) {
                Archivos.escritorArchivos(PRIMOS_FINALES, String.valueOf(prime), true); // true para añadir al final
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la lista final de primos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void runGeneraThreads() {
        System.out.println("\n--- Ejecutando Hilos Generadores (modo sin Queue) ---");
        int numThreads = 3; // Cantidad de hilos a crear
        int numPorThread = 20; // Cuántos números generará cada hilo
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) { // Crea una instancia de PrimesThread para cada hilo
            com.sebastian.safevotesystem.modelos.PrimesThread run = new com.sebastian.safevotesystem.modelos.PrimesThread(primesList, numPorThread);
            threads[i] = new Thread(run, "PrimeGeneratorThread-" + (i + 1));
            threads[i].start(); // Inicia la ejecución del hilo
        }
        for (int i = 0; i < numThreads; i++) { // Espera a que todos los hilos generadores terminen su ejecución
            try {
                threads[i].join(); // El hilo principal espera a que este hilo termine
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                System.err.println("El hilo principal fue interrumpido mientras esperaba a los hilos generadores.");
            }
        }
        System.out.println("\nTodos los hilos generadores (sin Queue) han terminado.");
        System.out.println("Total de números primos en la lista: " + primesList.getPrimesCount());
    }

    private void runProduceConsumidorThreads() {
        System.out.println("\n--- Ejecutando Hilos Productor/Consumidor (con Queue, Wait/Notify y Topic) ---");
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(50); // Cola compartida entre productores y consumidores con una capacidad máxima
        Object monitor = new Object(); // Objeto monitor para la sincronización wait/notify entre el coordinador y los consumidores
        AtomicInteger consumidoresActivos = new AtomicInteger(); // Contador atómico para rastrear la cantidad de consumidores que aún están activos
        EventoTopic eventoTopic = new EventoTopic(); // Instancia del Topic para notificaciones
        int numProductores = 2; // Cantidad de hilos productores
        int numPorProductores = 30; // Cantidad de números que cada productor generará
        int numConsumidores = 3; // Cantidad de hilos consumidores
        Thread[] productoresThreads = new Thread[numProductores]; // Crea y lanza los hilos Productores
        for (int i = 0; i < numProductores; i++) {
            NumberProducer productor = new NumberProducer(queue, numPorProductores, eventoTopic); // Pasa la queue y el topic
            productoresThreads[i] = new Thread(productor, "ProducerThread-" + (i + 1));
            productoresThreads[i].start();
        } // Crea y lanza los hilos Consumidores
        PrimeConsumer[] consumidores = new PrimeConsumer[numConsumidores];
        Thread[] consumidoresThreads = new Thread[numConsumidores];
        consumidoresActivos.set(numConsumidores); // Inicia el contador de consumidores activos
        for (int i = 0; i < numConsumidores; i++) {
            PrimeConsumer consumer = new PrimeConsumer(queue, primesList, monitor, consumidoresActivos, eventoTopic); // Pasa la queue, primesList, monitor, contador y topic
            consumidores[i] = consumer; // Guarda la referencia al objeto PrimeConsumer
            consumidoresThreads[i] = new Thread(consumer, "ConsumerThread-" + (i + 1));
            consumidoresThreads[i].start();
        } // Esperar a que todos los hilos Productores terminen su ejecución
        for (int i = 0; i < numProductores; i++) {
            try {
                productoresThreads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("El hilo principal fue interrumpido mientras esperaba a los productores.");
            }
        }
        System.out.println("\nTodos los hilos productores han terminado su trabajo.");
        for (PrimeConsumer consumer : consumidores) { // 4. Notificar a los consumidores que ya no habrá más producción de números.
            consumer.setProductoresListos(); // Establece el flag 'producersFinished' en cada consumidor
        }
        synchronized (monitor) { // Despierta a consumidor esperando
            monitor.notifyAll();
        } // Esperar a que los hilos Consumidores terminen su ejecución.
        synchronized (monitor) {
            while (consumidoresActivos.get() > 0) { // Continúa esperando mientras haya consumidores activos
                try {
                    System.out.println("Hilo principal esperando a que los consumidores terminen... Consumidores activos restantes: " + consumidoresActivos.get());
                    monitor.wait(); // El hilo principal espera, liberando el monitor
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("El hilo principal fue interrumpido mientras esperaba a los consumidores.");
                }
            }
        }
        System.out.println("\nTodos los hilos consumidores han terminado.");
        System.out.println("Total de números primos en la lista después de Produc./Consum.: " + primesList.getPrimesCount());
    }


    public static void main(String[] args) {
        System.out.println("Iniciando SafeVoteSystem...");
        SafeVoteSystem app = new SafeVoteSystem();
        try {
            Archivos.escritorArchivos(CODIGOS_PRIMOS, "", false); // Limpia códigos primos (sobrescribir)
            Archivos.escritorArchivos(PRIMOS_PRUEBAS, "", false); // Limpia primos preuba (sobrescribir)
            Archivos.escritorArchivos(PRIMOS_FINALES, "", false); // Limpia primos finales (sobrescribir)
        } catch (IOException e) {
            System.err.println("Error al limpiar archivos de log/pruebas al inicio: " + e.getMessage());
        } 
        app.cargaNumeroIniciales(); // Carga números iniciales
        app.menu();// Ejecutar el menú interactivo para la interacción del usuario
        System.out.println("\nSafeVoteSystem ha finalizado completamente.");
        System.out.println("Revisa los siguientes archivos para los resultados:");
        System.out.println("- " + CODIGOS_PRIMOS + " para el detalle de acciones y notificaciones del Topic.");
        System.out.println("- " + PRIMOS_PRUEBAS + " para los todos los números primos añadidos manualmente y por los consumidores.");
        System.out.println("- " + PRIMOS_FINALES + " para la lista final de números primos.");
    }
}