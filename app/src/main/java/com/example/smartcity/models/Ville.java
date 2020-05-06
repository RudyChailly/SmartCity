package com.example.smartcity.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ville {
    private int id;
    private String nom, code;

    public Ville() {}

    public Ville(int id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

    public Ville(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) { this.id = jsonObject.getInt("id"); }
        if (jsonObject.has("nom")) { this.nom = jsonObject.getString("nom"); }
        if (jsonObject.has("code")) { this.code = jsonObject.getString("code"); }
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.code+", "+this.nom;
    }

}
