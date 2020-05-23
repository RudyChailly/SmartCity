package com.example.smartcity.models.actualite.meteo;

import android.content.res.Resources;

import com.example.smartcity.R;

public class Meteo {

    int temperature;
    String date, ville, ville_code, description;

    public Meteo() {}

    public Meteo(Resources ressources, String packageName, int temperature, String ville, String code) {
        this.temperature = temperature;
        this.ville = ville;
        int id = ressources.getIdentifier("meteo_description_" + code, "string", packageName);
        this.description = ressources.getString(id);
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getVille_code() {
        return ville_code;
    }

    public void setVille_code(String ville_code) {
        this.ville_code = ville_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
