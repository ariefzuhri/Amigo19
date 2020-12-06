package com.ariefzuhri.amigo19.preference.model;

import java.util.ArrayList;

public class UpdateInformation {
    private String date;
    private String latestVersion;
    private int latestVersionCode;
    private String link;
    private ArrayList<String> log;

    public UpdateInformation(){}

    public UpdateInformation(String date, String latestVersion, int latestVersionCode, String link, ArrayList<String> log) {
        this.date = date;
        this.latestVersion = latestVersion;
        this.latestVersionCode = latestVersionCode;
        this.link = link;
        this.log = log;
    }

    public String getDate() {
        return date;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public String getLink() {
        return link;
    }

    public ArrayList<String> getLog() {
        return log;
    }
}
