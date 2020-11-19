package com.TtPP.builders;

import com.TtPP.entities.Breed;

public class BreedBuilder {
    private Breed breed = new Breed(-1, "");

    public BreedBuilder withName (String name) {
        this.breed.setName(name);
        return this;
    }

    public BreedBuilder withBreedId (int id) {
        this.breed.setBreedId(id);
        return this;
    }

    public Breed build () {
        Breed tempBreed = this.breed;
        this.breed = new Breed(-1 , "");
        return tempBreed;
    }

}
