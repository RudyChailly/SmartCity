package com.example.smartcity.models.groupe;

import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.models.Ville;

public class Groupe {

    private Integer id;
    private String nom;
    private boolean rejoint;

    private Integer idVille;
    private Ville ville;

    private Integer idInteret;

    public void setId(Integer id) {
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

    public Integer getIdVille() {
        return idVille;
    }

    public void setIdVille(Integer idVille) {
        this.idVille = idVille;
    }

    public Integer getIdInteret() {
        return idInteret;
    }

    public void setIdInteret(Integer idInteret) {
        this.idInteret = idInteret;
    }

    private Interet interet;

    public Groupe() {}


    public Integer getId() {
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
