package com.sebastian.safevotesystem.modelos;

import java.util.ArrayList;
import java.util.List;

interface TopicSubs { // Interfaz para los suscriptores del Topic
    void onMessage(String mensaje);
}

public class EventoTopic {
    private final List<TopicSubs> subs = new ArrayList<>();
    private final Object monitor = new Object(); // Sincroniza el acceso a la lista de suscriptores

    public void sub(TopicSubs sub) {
        synchronized (monitor) {
            subs.add(sub);
            if (sub instanceof Thread) {
                System.out.println("Nuevo suscriptor añadido al Topic: " + ((Thread)sub).getName());
            } else {
                System.out.println("Nuevo suscriptor añadido al Topic: " + sub.getClass().getSimpleName());
            }
        }
    }

    public void unsub(TopicSubs sub) {
        synchronized (monitor) {
            subs.remove(sub);
            if (sub instanceof Thread) {
                System.out.println("Suscriptor eliminado del Topic: " + ((Thread)sub).getName());
            } else {
                System.out.println("Suscriptor eliminado del Topic: " + sub.getClass().getSimpleName());
            }
        }
    }

    public void publish(String mensaje) {
        synchronized (monitor) {
            for (TopicSubs sub : subs) {
                sub.onMessage(mensaje);
            }
        }
    }
}