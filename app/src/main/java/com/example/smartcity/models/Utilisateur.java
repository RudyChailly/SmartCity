package com.example.smartcity.models;

import android.util.Log;

import com.example.smartcity.models.Interet;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.Ville;
import com.example.smartcity.models.groupe.Groupe;

import org.json.JSONArray;

import java.util.ArrayList;

public class Utilisateur {

    private int id;
    private String nom, prenom, email, password;
    private int idVille;
    private ArrayList<Integer> idCommerces;
    private ArrayList<Integer> idInterets;
    private ArrayList<Integer> idGroupes;
    public Utilisateur() {}

    public void setIdInterets(ArrayList<Integer> idInterets) {
        this.idInterets = idInterets;
    }

    public void setIdGroupes(ArrayList<Integer> idGroupes) {
        this.idGroupes = idGroupes;
    }

    public int getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getIdVille() {
        return idVille;
    }

    public ArrayList<Integer> getIdGroupes() {
        return idGroupes;
    }

    public Utilisateur(int id, String nom, String prenom, String email, String password, int idVille, ArrayList<Integer>  idCommerces, ArrayList<Integer>  idInterets, ArrayList<Integer>  idGroupes) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.idVille = idVille;
        this.idCommerces = idCommerces;
        this.idInterets = idInterets;
        this.idGroupes = idGroupes;
    }


    public String getNom() {
        return nom;
    }

    public void setId(int id) {
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

    public void setPassword(String password) {
        this.password = password;
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

    public void checkArrayList() {
        if (this.idCommerces == null) {
            this.idCommerces = new ArrayList<Integer>();
        }
        else {
            this.idCommerces = idCommerces;
        }

        if (idInterets == null) {
            this.idInterets = new ArrayList<Integer>();
        }
        else {
            this.idInterets = idInterets;
        }

        if (idGroupes == null) {
            this.idGroupes = new ArrayList<Integer>();
        }
        else {
            this.idGroupes = idGroupes;
        }
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

}