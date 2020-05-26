package com.example.smartcity.models.actualite.meteo;

import android.content.res.Resources;

import com.example.smartcity.R;

import java.io.Serializable;

public class Meteo implements Serializable {

    int temperature;
    String date, ville, ville_code, description;
    int image;

    public Meteo() {}

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Meteo(Resources ressources, String packageName, int temperature, String ville, String code) {
        this.temperature = temperature;
        this.ville = ville;
        int id = ressources.getIdentifier("meteo_description_" + code, "string", packageName);
        this.description = ressources.getString(id);
        if (code.equals("200") || code.equals("201") || code.equals("202")) {
            this.image = ressources.getIdentifier("meteo_eclair_pluie", "drawable", packageName);
        }
        else if (code.equals("231") || code.equals("232") || code.equals("233")) {
            this.image = ressources.getIdentifier("meteo_eclair", "drawable", packageName);
        }
        else if (code.equals("300") || code.equals("301") || code.equals("302")) {
            this.image = ressources.getIdentifier("meteo_pluie_legere", "drawable", packageName);
        }
        else if (code.equals("500") || code.equals("501") || code.equals("502") || code.equals("511") || code.equals("520") || code.equals("521") || code.equals("522") || code.equals("900")) {
            this.image = ressources.getIdentifier("meteo_pluie_lourde", "drawable", packageName);
        }
        else if (code.equals("611") || code.equals("612")) {
            this.image = ressources.getIdentifier("meteo_vent", "drawable", packageName);
        }
        else if (code.equals("600") || code.equals("601") || code.equals("602") || code.equals("610") || code.equals("621") || code.equals("622") || code.equals("623")) {
            this.image = ressources.getIdentifier("meteo_pluie_lourde", "drawable", packageName);
        }
        else if (code.equals("700") || code.equals("711") || code.equals("721") || code.equals("731") || code.equals("741") || code.equals("751")) {
            this.image = ressources.getIdentifier("meteo_brouillard", "drawable", packageName);
        }
        else if (code.equals("801") || code.equals("802")) {
            this.image = ressources.getIdentifier("meteo_nuage_soleil", "drawable", packageName);
        }
        else if (code.equals("803") || code.equals("804")) {
            this.image = ressources.getIdentifier("meteo_nuage", "drawable", packageName);
        }
        else if (code.equals("800")) {
            this.image = ressources.getIdentifier("meteo_soleil", "drawable", packageName);
        }
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
