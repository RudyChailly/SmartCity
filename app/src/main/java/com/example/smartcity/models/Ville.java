package com.example.smartcity.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ville {
    private String nom, code;

    public Ville() {}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code+", "+this.nom;
    }

}
