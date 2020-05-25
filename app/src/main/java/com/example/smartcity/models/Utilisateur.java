package com.example.smartcity.models;

import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.groupe.Groupe;

import java.io.Serializable;
import java.util.ArrayList;

public class Utilisateur implements Serializable {

    private String id, nom, prenom, email, imageURL;
    private String idVille;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private ArrayList<String> idCommerces;
    private ArrayList<String> idInterets;
    private ArrayList<String> idGroupes;
    public Utilisateur() {
        idCommerces = new ArrayList<>();
        idInterets = new ArrayList<>();
        idGroupes = new ArrayList<>();
    }

    public void setIdInterets(ArrayList<String> idInterets) {
        this.idInterets = idInterets;
    }

    public void setIdGroupes(ArrayList<String> idGroupes) {
        this.idGroupes = idGroupes;
    }

    public String getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getIdVille() {
        return idVille;
    }

    public ArrayList<String> getIdGroupes() {
        return idGroupes;
    }

    public String getNom() {
        return nom;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdVille(String idVille) {
        this.idVille = idVille;
    }

    public void setIdCommerces(ArrayList<String> idCommerces) {
        this.idCommerces = idCommerces;
    }

    public ArrayList<String>  getIdCommerces() {
        return idCommerces;
    }

    public ArrayList<String>  getIdInterets() {
        return idInterets;
    }

    public boolean estInteresse(Actualite actualite) {
        return idInterets.contains(actualite.getIdInteret());
    }

    public boolean estInteresse(Commerce commerce) {
        return idInterets.contains(commerce.getIdInteret());
    }

    public boolean estAbonne(String idCommerce) {
        return idCommerces.contains(idCommerce);
    }

    public boolean estInteresse(Groupe groupe) {
        return idInterets.contains(groupe.getIdInteret());
    }

    public boolean aRejoint(String idGroupe) {
        return idGroupes.contains(idGroupe);
    }

    public String toString() {
        return this.prenom + " " + this.nom;
    }

}