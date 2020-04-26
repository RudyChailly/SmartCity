package com.example.smartcity.models.commerce;

public class Commerce {

    private int id;
    private String nom, adresse;

    public Commerce(){}

    public Commerce(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String toString() {
        return nom + ":\n - " + id + "\n - " + adresse + "\n";
    }
}
