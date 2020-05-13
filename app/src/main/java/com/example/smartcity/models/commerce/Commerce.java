package com.example.smartcity.models.commerce;

import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.models.Ville;

public class Commerce {

    private Integer id;
    private String nom, adresse;
    private boolean abonne;

    private int idVille;
    private Ville ville;

    private int idInteret;
    private Interet interet;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isAbonne() {
        return abonne;
    }

    public void setAbonne(boolean abonne) {
        this.abonne = abonne;
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public int getIdInteret() {
        return idInteret;
    }

    public void setIdInteret(int idInteret) {
        this.idInteret = idInteret;
    }


    public Commerce(){
        this.abonne = false;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void abonner(){
        abonne = true;
    }

    public void desabonner() {
        abonne = false;
    }

    public boolean estAbonne() {
        return abonne;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Ville getVille() {
        return ville;
    }

    public void setInteret(Interet interet) {
        this.interet = interet;
    }

    public Interet getInteret() {
        return interet;
    }

    public String toString() {
        return nom + ":\n - " + id + "\n - " + adresse + "\n";
    }

    public boolean equals(Commerce commerce) {
        return this.getId() == commerce.getId();
    }
}
