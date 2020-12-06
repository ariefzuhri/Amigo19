package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributesGlobal {
    @SerializedName("attributes")
    @Expose
    private Global global;

    public Global getGlobal() {
        return global;
    }
}
