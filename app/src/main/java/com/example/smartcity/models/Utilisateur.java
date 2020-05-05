package com.example.smartcity.models;

import com.example.smartcity.models.Interet;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.Ville;

import org.json.JSONArray;

import java.util.ArrayList;

public class Utilisateur {

    private int id;
    private String nom, prenom, email, password;
    private Ville ville;
    private ArrayList<Commerce> commerces;
    private ArrayList<Interet> interets;
    //private ArrayList<Groupe> groupes;

    public Utilisateur() {}

    public Utilisateur(int id, String nom, String prenom, String email, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
    }

    public void setVille(int idVille, ArrayList<Ville> villes) {
        for (Ville ville : villes) {
            if (ville.getId() == idVille) {
                this.ville = ville;
            }
        }
    }

    /*public void setCommerces(JSONArray idCommerce, ArrayList<Commerce> commerces) {
        for (Commerce commerce : commerces) {
            if (commerce.getId() == idCommerce) {
                this.c = ville;
            }
        }
    }*/

}