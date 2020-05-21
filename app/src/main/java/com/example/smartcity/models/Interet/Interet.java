package com.example.smartcity.models.Interet;

import org.json.JSONException;
import org.json.JSONObject;

public class Interet {

    private String id, nom;

    public Interet(){}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String toString() {
        return this.nom;
    }
}
