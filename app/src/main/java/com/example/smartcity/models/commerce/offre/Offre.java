package com.example.smartcity.models.commerce.offre;

import com.example.smartcity.models.commerce.Commerce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Offre {

    private String _id, intitule, intituleCourt, description, date;
    private double prix;
    private Commerce commerce;

    public Offre(){}

    public Offre(String _id, String intitule, String description, String date, double prix) {
        if (intitule.length() > 18) {
            intituleCourt = intitule.substring(0,17)+"...";
        }
        else {
            intituleCourt = intitule;
        }
        this._id = _id;
        this.intitule = intitule;
        this.description = description;
        this.date = date;
        this.prix = prix;
        this.commerce = null;
    }

    public Offre(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("_id")) { this._id = jsonObject.getString("_id"); }
        if (jsonObject.has("intitule")) {
            this.intitule = jsonObject.getString("intitule");
            if (intitule.length() > 18) {
                intituleCourt = intitule.substring(0,17)+"...";
            }
            else {
                intituleCourt = intitule;
            }
        }
        if (jsonObject.has("description")) { this.description = jsonObject.getString("description"); }
        if (jsonObject.has("date")) { this.date = jsonObject.getString("date"); }
        if (jsonObject.has("prix")) { this.prix = jsonObject.getDouble("prix"); }
    }

    public String getId() {
        return this._id;
    }

    public Commerce getCommerce() { return commerce; }

    public String getIntitule() {
        return intitule;
    }

    public String getIntituleCourt() {
        return intituleCourt;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double getPrix() {
        return prix;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    public void setCommerce(int idCommerce, ArrayList<Commerce> commerces) {
        for (Commerce commerce : commerces) {
            if (commerce.getId() == idCommerce) {
                this.commerce = commerce;
            }
        }
    }

    public boolean equals(Offre offre) {
        return this.getId().equals(offre.getId());
    }

}
