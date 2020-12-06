package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("FID")
    @Expose
    private int id;

    @SerializedName("Provinsi")
    @Expose
    private String name;

    @SerializedName("Kasus_Posi")
    @Expose
    private int infected;

    @SerializedName("Kasus_Semb")
    @Expose
    private int recovered;

    @SerializedName("Kasus_Meni")
    @Expose
    private int death;

    public Province(int id, String name, int infected, int recovered, int death) {
        this.id = id;
        this.name = name;
        this.infected = infected;
        this.recovered = recovered;
        this.death = death;
    }

    public Province(){}

    public Province(int id, String name) {
        this.id = id;
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

    public int getPatient() {
        return infected-recovered-death;
    }
}
