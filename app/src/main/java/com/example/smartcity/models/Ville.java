package com.example.smartcity.models;

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

    public int getId() {
        return this.id;
    }

}
