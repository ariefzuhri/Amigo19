package com.ariefzuhri.amigo19.preference.model;

import java.util.List;

public class AssessmentPreference {
    private boolean isFillOut;
    private String dateAssessment;
    private String firstMeet;
    private String lastMeet;
    private int pointAssessment;
    private List<Integer> indexSuggestion;
    private int numberOfDelay;
    private int rangeMeet;
    private int levelMeet;

    public AssessmentPreference(){} // Dibutuhkan Firebase

    public AssessmentPreference(boolean isFillOut, String dateAssessment, String firstMeet, String lastMeet, int pointAssessment, List<Integer> indexSuggestion, int numberOfDelay, int rangeMeet, int levelMeet) {
        this.isFillOut = isFillOut;
        this.dateAssessment = dateAssessment;
        this.firstMeet = firstMeet;
        this.lastMeet = lastMeet;
        this.pointAssessment = pointAssessment;
        this.indexSuggestion = indexSuggestion;
        this.numberOfDelay = numberOfDelay;
        this.rangeMeet = rangeMeet;
        this.levelMeet = levelMeet;
    }

    public boolean getIsFillOut() {
        return isFillOut;
    }

    public String getDateAssessment() {
        return dateAssessment;
    }

    public String getFirstMeet() {
        return firstMeet;
    }

    public String getLastMeet() {
        return lastMeet;
    }

    public List<Integer> getIndexSuggestion() {
        return indexSuggestion;
    }

    public int getNumberOfDelay() {
        return numberOfDelay;
    }

    public int getRangeMeet() {
        return rangeMeet;
    }

    public int getLevelMeet() {
        return levelMeet;
    }

    /*Tidak boleh diakses pengguna, namun tetap wajib public untuk Firestore*/
    // Hanya untuk menentukan rangeMeet dan levelMeet
    public int getPointAssessment() {
        return pointAssessment;
    }
}
