package com.example.smartcity.models.groupe.message;

import com.example.smartcity.models.Utilisateur;

public class Message {

    private String idUtilisateur;
    private Utilisateur utilisateur;

    private Integer idGroupe;

    private String contenu;

    public Message(){}

    public Message(String idUtilisateur, Integer idGroupe, String contenu) {
        this.idUtilisateur = idUtilisateur;
        this.idGroupe = idGroupe;
        this.contenu = contenu;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}