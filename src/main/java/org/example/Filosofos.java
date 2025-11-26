package org.example;

import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Filosofos extends Thread{
    private final Semaphore semaforo; // controla cuántos filósofos pueden intentar comer
    private final Palillos palilloIzq;
    private final Palillos palilloDer;
    private final int id;
    private final PanelFilosofos panel;
    public int comidas = 0; //  nuevo contador de comidas



    public volatile boolean comiendo = false;



    public Filosofos(int id, Semaphore semaforo, Palillos palilloIzq, Palillos palilloDer, PanelFilosofos panel) {
        this.id = id;
        this.semaforo = semaforo;
        this.palilloIzq = palilloIzq;
        this.palilloDer = palilloDer;
        this.panel = panel;

    }


    private void actualizarPanel() {
        try {
            SwingUtilities.invokeAndWait(panel::repaint);
        } catch (Exception ignored) {}
    }

    private void pensar() {
        comiendo = false;
        actualizarPanel();
        System.out.println("Filósofo " + id + " está pensando ️");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 4000));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void comer() {
        try {
            semaforo.acquire();

            // límite global, evita bloqueo total

            // Tomar palillos
            palilloIzq.usar("izquierdo", id);
            palilloDer.usar("derecho", id);
            actualizarPanel();
            Thread.sleep(4000); // pequeña pausa para simular acción
            comiendo = true;
            actualizarPanel();
            System.out.println("Filósofo " + id + " está comiendo ");

            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 6000));

            palilloIzq.enUso = false;
            palilloDer.enUso = false;
            comiendo = false;
            comidas++;
            actualizarPanel();
            System.out.println("Filósofo " + id + " terminó de comer ");

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            semaforo.release();
        }
    }

    @Override
    public void run() {
        pensar();
        comer();
        System.out.println("Filósofo " + id + " ha terminado su jornada ");

    }

}
