package com.example.printingapp;

public class Produit {

    int id;
    String nom;
    double prix_u;
    double prix_t;
    double tva;
    int qte = 1;
    int remise;


    public Produit(int id, String nom, double prix_u, double tva, int remise) {
        this.id = id;
        this.nom = nom;
        this.prix_u = prix_u;

        this.tva = tva;
        this.qte = qte;

        this.remise = remise;
        if(this.remise>100){this.remise=100;}// et oui

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(int remise) {
        this.remise = remise;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;

    }

    public double getPrix_t() {

        this.prix_t = (prix_u * qte) - ((prix_u * qte) * (remise / 100));
        return (prix_u * qte) - ((prix_u * qte) * (Double.valueOf(remise) / 100));
    }

    public void setPrix_t(double prix_t) {
        this.prix_t = prix_t;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix_u() {
        return prix_u;
    }

    public void setPrix_u(double prix_u) {
        this.prix_u = prix_u;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }
}
