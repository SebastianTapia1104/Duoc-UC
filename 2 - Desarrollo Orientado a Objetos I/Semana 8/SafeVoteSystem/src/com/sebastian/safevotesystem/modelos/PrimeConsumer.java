package com.sebastian.safevotesystem.modelos;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimeConsumer implements Runnable, TopicSubs { // Runnable (para ser un hilo) y TopicSubscriber (para recibir mensajes del Topic)
    private final BlockingQueue<Integer> queue;
    private final PrimesList primesList;
    private final Object monitor; // Para wait/notify
    private volatile boolean productoresListos = false;
    private final AtomicInteger consumidoresActivos; // Contador de número de consumidores activos
    private final EventoTopic eventoTopic;

    private static final String CODIGOS_PRIMOS = "src/com/sebastian/safevotesystem/resources/CodigosPrimos.txt";
    private static final String PRIMOS_PRUEBAS = "src/com/sebastian/safevotesystem/resources/NumerosPrimosPruebas.csv";

    public PrimeConsumer(BlockingQueue<Integer> queue, PrimesList primesList, Object monitor, AtomicInteger activeConsumers, EventoTopic eventoTopic) {
        this.queue = queue;
        this.primesList = primesList;
        this.monitor = monitor;
        this.consumidoresActivos = activeConsumers;
        this.eventoTopic = eventoTopic;
    }

    public void setProductoresListos() {
        this.productoresListos = true;
    }

    @Override
    public void onMessage(String mensaje) {
        try { // Registra el mensaje recibido del Topic en el archivo de Códigos Primos.
            Archivos.escritorArchivos(CODIGOS_PRIMOS, Thread.currentThread().getName() + " (Consumidor) recibió del Topic: " + mensaje, true);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + " Error al loggear mensaje del Topic: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " (Consumidor) ha comenzado a procesar números.");
        eventoTopic.sub(this);// El consumidor se suscribe al Topic al inicio de su ejecución.
        try {
            while (true) { // Intenta tomar un número de la cola sin bloquear. Devuelve null si la cola está vacía.
                Integer num = queue.poll();
                if (num == null) { // Si la cola está vacía, el consumidor debe decidir si esperar o terminar.
                    synchronized (monitor) {
                        if (productoresListos && queue.isEmpty()) { // Verifica si los productores han terminado y si la cola sigue vacía.
                            int consumidoresRestantes = consumidoresActivos.decrementAndGet(); // -x contador de consumidores activos.
                            if (consumidoresRestantes == 0) { // Si este es el último consumidor activo, notifica al hilo principal para que despierte.
                                monitor.notifyAll();
                            }
                            System.out.println(threadName + " (Consumidor) terminado: Cola vacía y productores finalizados. Consumidores restantes: " + consumidoresRestantes);
                            break; // Sale del bucle para terminar el hilo.
                        }
                        monitor.wait(500); // Si la cola está vacía pero los productores NO han terminado, el consumidor espera.
                    }
                    continue; // Vuelve al inicio del bucle para reintentar.
                }
                String numeroBinario = Archivos.numeroABinario(num); // Si se obtuvo un número, lo procesarlo.
                boolean esPrimo = primesList.isPrime(num);
                try {
                    if (esPrimo) {
                        primesList.add(num); // Añade el primo a la lista.
                        Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + numeroBinario + ") es primo y fue añadido por " + threadName + " (Consumidor).", true);
                        Archivos.escritorArchivos(PRIMOS_PRUEBAS, String.valueOf(num), true);
                    } else {
                        Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + numeroBinario + ") no es primo, número descartado por " + threadName + " (Consumidor).", true);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println(threadName + " (Consumidor) Error inesperado al añadir: " + num + " - " + e.getMessage());
                    Archivos.escritorArchivos(CODIGOS_PRIMOS, "(" + numeroBinario + ") error inesperado al probar/añadir por " + threadName + ": " + e.getMessage(), true);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción.
            System.err.println(threadName + " (Consumidor) fue interrumpido.");
        } catch (IOException e) {
            System.err.println(threadName + " (Consumidor) Error de I/O al escribir en el archivo de log/pruebas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            eventoTopic.unsub(this); // El consumidor se desuscribe del Topic al terminar.
            System.out.println(threadName + " (Consumidor) ha terminado su ejecución.");
        }
    }
}