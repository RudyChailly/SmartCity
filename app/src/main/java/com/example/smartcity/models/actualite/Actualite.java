package com.example.smartcity.models.actualite;

import com.example.smartcity.models.Interet;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Actualite(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("titre")) { this.titre = jsonObject.getString("titre"); }
        if (jsonObject.has("url")) { this.url = jsonObject.getString("url"); }
        if (jsonObject.has("date")) { this.date = jsonObject.getString("date"); }
        if (jsonObject.has("source")) { this.source = jsonObject.getString("source"); }
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
