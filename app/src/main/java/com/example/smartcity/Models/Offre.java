package com.example.smartcity.Models;

import java.io.Serializable;

public class Offre implements Serializable {

    private String intitule, intituleCourt, description, date;
    private double prix;
    private String idCommerce;
    private Commerce commerce;
    String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

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

    public String getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(String idCommerce) {
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
        return ((this.getIdCommerce().equals(offre.getIdCommerce())) && (this.getIntitule().equals(offre.getIntitule())) && (this.getDate().equals(offre.getDate())));
    }

}
