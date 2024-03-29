package com.example.smartcity.Models;

import java.io.Serializable;

public class Actualite implements Serializable {
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    String imageURL;

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
