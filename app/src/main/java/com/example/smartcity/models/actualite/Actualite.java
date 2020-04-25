package com.example.smartcity.models.actualite;

public class Actualite {

    private String titre, url, date, source;

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

}
