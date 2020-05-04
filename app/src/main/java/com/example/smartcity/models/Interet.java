package com.example.smartcity.models;

public class Interet {

    private int id;
    private String nom;

    public Interet(){}

    public Interet(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String toString() {
        return this.nom;
    }
}
