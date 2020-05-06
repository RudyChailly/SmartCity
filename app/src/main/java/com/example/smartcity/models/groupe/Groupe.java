package com.example.smartcity.models.groupe;

import com.example.smartcity.models.Interet;
import com.example.smartcity.models.Ville;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Groupe {

    private int id;
    private String nom;
    private boolean rejoint;

    private Ville ville;
    private Interet interet;

    public Groupe() {}

    public Groupe(int id, String nom) {
        this.id = id;
        this.nom = nom;
        rejoint = false;
    }

    public Groupe(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) { this.id = jsonObject.getInt("id"); }
        if (jsonObject.has("nom")) { this.nom = jsonObject.getString("nom"); }
        this.rejoint = false;
    }

    public int getId() {
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
