package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributesProvince {
    @SerializedName("attributes")
    @Expose
    private Province province;

    public Province getProvince() {
        return province;
    }
}
