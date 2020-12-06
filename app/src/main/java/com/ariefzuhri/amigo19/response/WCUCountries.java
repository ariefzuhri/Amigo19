package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WCUCountries {
    @SerializedName("countries")
    @Expose
    private ArrayList<WCUCountry> countries;

    public ArrayList<WCUCountry> getCountries() {
        return countries;
    }
}
