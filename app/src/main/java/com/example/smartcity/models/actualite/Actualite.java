package com.example.smartcity.models.actualite;

import com.example.smartcity.models.Interet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Actualite {
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setIdInteret(int idInteret) {
        this.idInteret = idInteret;
    }

    private String titre, url, date, source;
    private Interet interet;
    private int idInteret;

    public Actualite(){}

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

    public long getIdInteret() {
        return  idInteret;
    }

    public void setInteret(Interet interet) {
        this.interet = interet;
    }

    public Interet getInteret() {
        return interet;
    }

    public String toString() {
        return titre + ":\n - " + source + "\n - " + date + "\n";
    }

}
