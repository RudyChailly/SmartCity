package com.example.smartcity.models.Interet;

import org.json.JSONException;
import org.json.JSONObject;

public class Interet {

    private Integer id;
    private String nom;

    public Interet(){}

    public Interet(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String toString() {
        return this.nom;
    }
}
