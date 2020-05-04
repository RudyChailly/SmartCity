package com.example.smartcity.models.actualite;

import com.example.smartcity.models.Interet;

import java.util.ArrayList;

public class Actualite {

    private String titre, url, date, source;
    private Interet interet;

    public Actualite(){}

    public Actualite(String titre, String url, String source, String date) {
        this.titre = titre;
        this.url = url;
        this.source = source;
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        return titre + ":\n - " + source + "\n - " + date + "\n";
    }

    public void setInteret(int idInteret, ArrayList<Interet> interets) {
        for (Interet interet : interets) {
            if (interet.getId() == idInteret) {
                this.interet = interet;
            }
        }
    }

    public Interet getInteret() {
        return interet;
    }

}
