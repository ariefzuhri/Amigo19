package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("positif")
    @Expose
    private String infected;

    @SerializedName("sembuh")
    @Expose
    private String recovered;

    @SerializedName("meninggal")
    @Expose
    private String death;

    public Country(){}

    public Country(String name, int infected, int recovered, int death) {
        this.name = name;
        this.infected = String.valueOf(infected);
        this.recovered = String.valueOf(recovered);
        this.death = String.valueOf(death);
    }

    public String getName() {
        return name;
    }

    public int getInfected() {
        return Integer.parseInt(infected.replace(",", ""));
    }

    public int getRecovered() {
        return Integer.parseInt(recovered.replace(",", ""));
    }

    public int getDeath() {
        return Integer.parseInt(death.replace(",", ""));
    }

    public int getPatient(){
        return getInfected()-getRecovered()-getDeath();
    }
}
