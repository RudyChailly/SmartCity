package com.example.smartcity.models.actualite;

import com.example.smartcity.models.Interet.Interet;

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

    public void setIdInteret(String idInteret) {
        this.idInteret = idInteret;
    }

    private String titre, url, date, source, id;
    private Interet interet;
    private String idInteret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getIdInteret() {
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

    public boolean equals(Actualite actualite) {
        return this.getId().equals(actualite.getId());
    }

}
