package com.example.smartcity.models;

import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.groupe.Groupe;

import java.util.ArrayList;

public class Utilisateur {

    private String id, nom, prenom, email;
    private int idVille;
    private ArrayList<Integer> idCommerces;
    private ArrayList<Integer> idInterets;
    private ArrayList<Integer> idGroupes;
    public Utilisateur() {
        idCommerces = new ArrayList<>();
        idInterets = new ArrayList<>();
        idGroupes = new ArrayList<>();
    }

    public void setIdInterets(ArrayList<Integer> idInterets) {
        this.idInterets = idInterets;
    }

    public void setIdGroupes(ArrayList<Integer> idGroupes) {
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

    public int getIdVille() {
        return idVille;
    }

    public ArrayList<Integer> getIdGroupes() {
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

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public void setIdCommerces(ArrayList<Integer> idCommerces) {
        this.idCommerces = idCommerces;
    }

    public ArrayList<Integer>  getIdCommerces() {
        return idCommerces;
    }

    public ArrayList<Integer>  getIdInterets() {
        return idInterets;
    }

    public boolean estInteresse(Actualite actualite) {
        for (Integer idInteret : idInterets) {
            if (idInteret == actualite.getIdInteret()) {
                return true;
            }
        }
        return false;
    }

    public boolean estInteresse(Commerce commerce) {
        return idInterets.contains(commerce.getIdInteret());
    }

    public boolean estAbonne(Integer idCommerce) {
        return idCommerces.contains(idCommerce);
    }

    public boolean estInteresse(Groupe groupe) {
        return idInterets.contains(groupe.getIdInteret());
    }

    public boolean aRejoint(Integer idGroupe) {
        return idGroupes.contains(idGroupe);
    }

    public String toString() {
        return this.prenom + " " + this.nom;
    }

}