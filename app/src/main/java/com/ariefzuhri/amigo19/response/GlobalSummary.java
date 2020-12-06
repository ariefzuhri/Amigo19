package com.ariefzuhri.amigo19.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalSummary {
    @SerializedName("value")
    @Expose
    private String count;

    public int getCount() {
        return Integer.parseInt(count.replace(",", ""));
    }
}
