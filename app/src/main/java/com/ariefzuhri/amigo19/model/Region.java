package com.ariefzuhri.amigo19.model;

public class Region {
    private int id;
    private String name;
    private String iso;

    // Untuk provinsi di Indonesia
    public Region(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Untuk negara di dunia
    public Region(String name, String iso) {
        this.name = name;
        this.iso = iso;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }
}
