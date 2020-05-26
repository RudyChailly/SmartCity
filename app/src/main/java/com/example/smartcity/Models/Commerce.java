package com.example.smartcity.Models;

import java.io.Serializable;

public class Commerce implements Serializable {

    private String id;
    private String nom, adresse;
    private boolean abonne;

    private String idVille;
    private Ville ville;

    private String idInteret;
    private Interet interet;

    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setId(String id) {
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


    public Commerce(){
        this.abonne = false;
    }

    public String getId() {
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
