package com.example.printingapp;


class Ticket {
    private int id;
    private String reference;
    private Boolean direct;
    private Boolean table;
    private double numero;
    private String details;



    private String vendeur;
    private String date; // a voir si auto declaration
    private Boolean cloture;

    public Ticket(){

    }

    public Ticket(int id, String reference, Boolean direct, Boolean table,Double numero, String details, String vendeur, Boolean cloture, String date) {
        this.id = id;
        this.reference = reference;
        this.direct = direct;
        this.table = table;
        this.numero = numero;
        this.details = details;
        this.vendeur = vendeur;
        this.cloture = cloture;
        this.date = date;

    }

    public Ticket(int id, String reference, Boolean direct, Boolean table, Double numero, String details, String vendeur, Boolean cloture) {
        this.id = id;
        this.reference = reference;
        this.direct = direct;
        this.table = table;
        this.numero = numero;
        this.details = details;
        this.vendeur = vendeur;
        this.cloture = cloture;
    }

    public Ticket(String reference, Boolean direct, Boolean table, Double numero, String details, String vendeur, Boolean cloture) {
        this.reference = reference;
        this.direct = direct;
        this.table = table;
        this.numero = numero;
        this.details = details;
        this.vendeur = vendeur;
        this.cloture = cloture;
    }

    public Ticket(String reference, Boolean direct, Boolean table, Double numero, String details, String vendeur, Boolean cloture, String date) {
        this.reference = reference;
        this.direct = direct;
        this.table = table;
        this.numero = numero;
        this.details = details;
        this.vendeur = vendeur;
        this.cloture = cloture;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getDirect() {
        return direct;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
    }

    public Boolean getTable() {
        return table;
    }

    public void setTable(Boolean table) {
        this.table = table;
    }

    public double getNumero() {
        return numero;
    }

    public void setNumero(double numero) {
        this.numero = numero;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getcloture() {
        return cloture;
    }

    public void setcloture(Boolean cloture) {
        this.cloture = cloture;
    }
}
