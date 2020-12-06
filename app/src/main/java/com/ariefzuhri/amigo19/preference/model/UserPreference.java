package com.ariefzuhri.amigo19.preference.model;

public class UserPreference {
    private boolean isCustomLocation;
    private String location;
    private boolean isAbroad;

    public UserPreference(){} // Dibutuhkan Firebase

    public UserPreference(boolean isCustomLocation, String location, boolean isAbroad) {
        this.isCustomLocation = isCustomLocation;
        this.location = location;
        this.isAbroad = isAbroad;
    }

    public boolean getIsCustomLocation() {
        return isCustomLocation;
    }

    public String getLocation() {
        return location;
    }

    public boolean getIsAbroad() {
        return isAbroad;
    }
}
