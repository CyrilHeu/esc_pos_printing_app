package com.example.printingapp.printing;

public class Entete {
    private String nom_enseigne;
    private String adresse_enseigne;
    private String telephone;
    private String adresse_web;
    private String horaire;
    private String info_vendeur;
    private String info_ouverture;

    public Entete(String nom_enseigne, String adresse_enseigne, String telephone, String adresse_web,String info_ouverture, String horaire) {
        this.nom_enseigne = nom_enseigne;
        this.adresse_enseigne = adresse_enseigne;
        this.telephone = telephone;
        this.adresse_web = adresse_web;
        this.horaire = horaire;
        this.info_ouverture = info_ouverture;
    }

    public String getInfo_ouverture() {
        return info_ouverture;
    }

    public void setInfo_ouverture(String info_ouverture) {
        this.info_ouverture = info_ouverture;
    }

    public String getNom_enseigne() {
        return nom_enseigne;
    }

    public void setNom_enseigne(String nom_enseigne) {
        this.nom_enseigne = nom_enseigne;
    }

    public String getAdresse_enseigne() {
        return adresse_enseigne;
    }

    public void setAdresse_enseigne(String adresse_enseigne) {
        this.adresse_enseigne = adresse_enseigne;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse_web() {
        return adresse_web;
    }

    public void setAdresse_web(String adresse_web) {
        this.adresse_web = adresse_web;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getInfo_vendeur() {
        return info_vendeur;
    }

    public void setInfo_vendeur(String info_vendeur) {
        this.info_vendeur = info_vendeur;
    }
}
