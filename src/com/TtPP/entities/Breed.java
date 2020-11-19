package com.TtPP.entities;

public class Breed {
    private int breedId;
    private String name = "";

    public Breed(int breedId, String name) {
        this.breedId = breedId;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBreedId() {
        return this.breedId;
    }

    public void setBreedId(int breedId) {
        this.breedId = breedId;
    }

}
