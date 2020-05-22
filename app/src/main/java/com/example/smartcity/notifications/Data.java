package com.example.smartcity.notifications;

import com.example.smartcity.models.Utilisateur;

public class Data {
    private String user, body, title, sented, idGroupe, nomGroupe;


    public Data() {}

    public String getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public Data(Utilisateur utilisateur, String user, String body, String title, String sented, String idGroupe, String nomGroupe) {
        this.user = user;
        this.body = utilisateur.toString() + " : " + body;
        this.title = title;
        this.sented = sented;
        this.idGroupe = idGroupe;
        this.nomGroupe = nomGroupe;
    }

}
