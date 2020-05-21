package com.example.smartcity.models.groupe;

import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.models.Ville;

public class Groupe {

    private String id;
    private String nom;
    private boolean rejoint;

    private String idVille;
    private Ville ville;

    private String idInteret;

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isRejoint() {
        return rejoint;
    }

    public void setRejoint(boolean rejoint) {
        this.rejoint = rejoint;
    }

    public String getIdVille() {
        return idVille;
    }

    public void setIdVille(String idVille) {
        this.idVille = idVille;
    }

    public String getIdInteret() {
        return idInteret;
    }

    public void setIdInteret(String idInteret) {
        this.idInteret = idInteret;
    }

    private Interet interet;

    public Groupe() {}


    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
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

    public void setInteret(Interet interet) {
        this.interet = interet;
    }

    public Interet getInteret() {
        return interet;
    }

}
