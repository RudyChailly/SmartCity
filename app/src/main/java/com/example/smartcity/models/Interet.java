package com.example.smartcity.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Interet {

    private int id;
    private String nom;

    public Interet(){}

    public Interet(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Interet(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) { this.id = jsonObject.getInt("id"); }
        if (jsonObject.has("nom")) { this.nom = jsonObject.getString("nom"); }
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String toString() {
        return this.nom;
    }
}
