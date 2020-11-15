package com.NoSQl;

public class Breed<IDType> {
    private IDType breedId;
    private String name = "";

    public Breed(IDType breedId, String name) {
        this.breedId = breedId;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IDType getBreedId() {
        return this.breedId;
    }

    public void setBreedId(IDType breedId) {
        this.breedId = breedId;
    }

}
