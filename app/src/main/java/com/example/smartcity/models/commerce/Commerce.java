package com.example.smartcity.models.commerce;

import com.example.smartcity.models.Interet;
import com.example.smartcity.models.Ville;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Commerce {

    private int id;
    private String nom, adresse;
    private boolean abonne;
    private Ville ville;
    private Interet interet;

    public Commerce(){
        this.abonne = false;
    }

    public Commerce(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.abonne = false;
    }

    public Commerce(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) { this.id = jsonObject.getInt("id"); }
        if (jsonObject.has("nom")) { this.nom = jsonObject.getString("nom"); }
        if (jsonObject.has("adresse")) { this.adresse = jsonObject.getString("adresse"); }
        this.abonne = false;
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
