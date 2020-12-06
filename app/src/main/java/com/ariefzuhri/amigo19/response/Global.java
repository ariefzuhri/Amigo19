package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Global {
    @SerializedName("OBJECTID")
    @Expose
    private int id;

    @SerializedName("Country_Region")
    @Expose
    private String name;

    @SerializedName("Confirmed")
    @Expose
    private int infected;

    @SerializedName("Deaths")
    @Expose
    private int death;

    @SerializedName("Recovered")
    @Expose
    private int recovered;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getInfected() {
        return infected;
    }

    public int getDeath() {
        return death;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getPatient() {
        return infected-recovered-death;
    }
}
