package com.sebastian.safevotesystem.modelos;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class NumberProducer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int numerosProducidos;
    private final EventoTopic eventTopic; 

    public NumberProducer(BlockingQueue<Integer> queue, int numerosProducidos, EventoTopic eventTopic) {
        this.queue = queue;
        this.numerosProducidos = numerosProducidos;
        this.eventTopic = eventTopic;
    }

    @Override
    public void run() {
        Random random = new Random();
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " (Productor) ha comenzado a producir números.");
        try {
            for (int i = 0; i < numerosProducidos; i++) {
                int num = random.nextInt(999) + 2; // Generar un número aleatorio entre 2 y 1000
                queue.put(num); // Pone el número en la cola. Si la cola está llena, este método se bloquea hasta que haya espacio.
                eventTopic.publish(threadName + " generó y puso en cola el número: " + num); // Publica un mensaje en el Topic indicando que un número ha sido generado y puesto en la cola.
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción
            System.err.println(threadName + " (Productor) fue interrumpido mientras producía.");
        } catch (Exception e) { // Captura cualquier otra excepción, como IOException si se usara log dentro del bucle
            System.err.println(threadName + " (Productor) Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println(threadName + " (Productor) ha terminado de producir números.");
        }
    }
}