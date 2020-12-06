package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WCUCountry {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("iso2")
    @Expose
    private String iso2;

    @SerializedName("iso3")
    @Expose
    private String iso3;

    public String getName() {
        return name;
    }

    public String getIso2() {
        return iso2;
    }

    public String getIso3() {
        return iso3;
    }
}
