package com.ariefzuhri.amigo19.model;

public class Statistic {
    private int id;
    private String name;
    private int infected;
    private int recovered;
    private int death;

    public Statistic(int id, String name, int infected, int recovered, int death) { // Untuk provinsi Indonesia
        this.id = id; // Identitas menentukan lambang
        this.name = name;
        this.infected = infected;
        this.recovered = recovered;
        this.death = death;
    }

    public Statistic(String name, int infected, int recovered, int death) { // Untuk negara dunia
        this.id = 0; // Untuk menentukan bendera, pakai name
        this.name = name;
        this.infected = infected;
        this.recovered = recovered;
        this.death = death;
    }

    public Statistic(String name){ // Untuk global summary
        this.id = 0;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getInfected() {
        return infected;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getDeath() {
        return death;
    }

    public int getPatient(){
        return infected-recovered-death;
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public void setDeath(int death) {
        this.death = death;
    }
}
