package com.example.printingapp.printing;

class Vendeur {
    private int id;
    private String usual_name;

    public Vendeur() {
    }

    public Vendeur(int id, String usual_name) {
        this.id = id;
        this.usual_name = usual_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsual_name() {
        return usual_name;
    }

    public void setUsual_name(String usual_name) {
        this.usual_name = usual_name;
    }
}
