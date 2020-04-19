package com.colman.fit_me.model;

public class Categorie {
    public String name;
    public String image;


    public Categorie() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Categorie(String name) {
        this.name = name;
    }
    public Categorie(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}