package com.example.smartcity.models.Interet;

import org.json.JSONException;
import org.json.JSONObject;

public class Interet {

    private String id, nom, imageURL;

    public Interet(){}

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNom() {
        return nom;
    }

    public String toString() {
        return this.nom;
    }
}
