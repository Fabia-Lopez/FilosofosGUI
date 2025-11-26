package org.example;

public class Palillos {
    public final int id;
    public volatile boolean enUso;

    public Palillos(int id) {
        this.id = id;
        this.enUso = false;
    }

    public void usar(String lado, int filosofoId) {
        enUso = true;
        System.out.println("Filósofo " + filosofoId + " tomó el palillo " + lado + " (" + id + ") ");
    }

}
