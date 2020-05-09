package com.example.smartcity.models.commerce.offre;

import com.example.smartcity.models.commerce.Commerce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Offre {

    private String intitule, intituleCourt, description, date;
    private double prix;
    private int idCommerce;
    private Commerce commerce;

    public Offre(){}

    public void setIntitule(String intitule) {
        this.intitule = intitule;
        if (intitule.length() > 20) {
            intituleCourt = intitule.substring(0,17);
        }
        else {
            intituleCourt = intitule;
        }
    }

    public void setIntituleCourt(String intituleCourt) {
        this.intituleCourt = intituleCourt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(int idCommerce) {
        this.idCommerce = idCommerce;
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

    public boolean equals(Offre offre) {
        return ((this.getIdCommerce() == offre.getIdCommerce()) && (this.getIntitule().equals(offre.getIntitule())) && (this.getDate().equals(offre.getDate())));
    }

}
