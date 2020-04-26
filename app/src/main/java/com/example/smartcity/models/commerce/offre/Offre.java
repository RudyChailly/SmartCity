package com.example.smartcity.models.commerce.offre;

import com.example.smartcity.models.commerce.Commerce;

import java.util.ArrayList;

public class Offre {

    private Commerce commerce;
    private int idCommerce;
    private String intitutle, intituleCourt, description, date;
    private double prix;

    public Offre(){}

    public Offre(int idCommerce, String intitutle, String description, String date, double prix) {
        this.idCommerce = idCommerce;
        if (intitutle.length() > 18) {
            intituleCourt = intitutle.substring(0,17)+"...";
        }
        else {
            intituleCourt = intitutle;
        }
        this.intitutle = intitutle;
        this.description = description;
        this.date = date;
        this.prix = prix;
        this.commerce = null;
    }

    public Commerce getCommerce() { return commerce; }

    public int getIdCommerce() { return idCommerce; }

    public String getIntitule() {
        return intitutle;
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

    public void setCommerce(ArrayList<Commerce> commerces) {
        for (Commerce commerce : commerces) {
            if (commerce.getId() == idCommerce) {
                this.commerce = commerce;
            }
        }
    }

}
