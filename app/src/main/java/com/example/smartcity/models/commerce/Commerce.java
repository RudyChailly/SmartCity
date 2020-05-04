package com.example.smartcity.models.commerce;

import com.example.smartcity.models.Interet;
import com.example.smartcity.models.Ville;

import java.util.ArrayList;

public class Commerce {

    private int id;
    private String nom, adresse;
    private Ville ville;
    private Interet interet;

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

    public void setVille(int idVille, ArrayList<Ville> villes) {
        for (Ville ville : villes) {
            if (ville.getId() == idVille) {
                this.ville = ville;
            }
        }
    }

    public Ville getVille() {
        return ville;
    }

    public void setInteret(int idInteret, ArrayList<Interet> interets) {
        for (Interet interet : interets) {
            if (interet.getId() == idInteret) {
                this.interet = interet;
            }
        }
    }

    public Interet getInteret() {
        return interet;
    }
}
