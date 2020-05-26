package com.example.smartcity.Models;

import java.io.Serializable;

public class Ville implements Serializable {
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
