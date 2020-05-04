package com.example.smartcity.models.groupe;

import com.example.smartcity.models.Interet;
import com.example.smartcity.models.Ville;

import java.util.ArrayList;

public class Groupe {

    private int id;
    private String nom;
    private Ville ville;
    private Interet interet;
    private boolean rejoint;

    public Groupe() {}

    public Groupe(int id, String nom) {
        this.id = id;
        this.nom = nom;
        rejoint = false;
    }

    public String getNom() {
        return nom;
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

    public void rejoindre() {
        rejoint = true;
    }

    public void quitter() {
        rejoint = false;
    }

    public boolean estRejoint() {
        return rejoint;
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
