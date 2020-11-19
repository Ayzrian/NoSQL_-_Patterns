package com.TtPP.builders;

import com.TtPP.entities.Pet;

import java.util.Date;

public class PetBuilder {
    private Pet pet = new Pet(
            -1,
            -1,
            -1,
            -1,
            -1,
            "",
            null,
            false
    );

    public PetBuilder withPetId (int id) {
        this.pet.setPetId(id);
        return this;
    }

    public PetBuilder withFkParentId (int id) {
        this.pet.setFkParentId(id);
        return this;
    }

    public PetBuilder withFkOwnerId (int id) {
        this.pet.setFkOwnerId(id);
        return this;
    }

    public PetBuilder withFkKindId (int id) {
        this.pet.setFkKindId(id);
        return this;
    }

    public PetBuilder withFkBreedID (int id) {
        this.pet.setFkBreedId(id);
        return this;
    }

    public PetBuilder withName (String name) {
        this.pet.setName(name);
        return this;
    }

    public PetBuilder withDateOfBirth (Date date) {
        this.pet.setDateOfBirth(date);
        return this;
    }

    public PetBuilder withSex (boolean sex) {
        this.pet.setSex(sex);
        return this;
    }

    public Pet build () {
        Pet tempPet = this.pet;
        this.pet = new Pet(
                -1,
                -1,
                -1,
                -1,
                -1,
                "",
                null,
                false
        );
        return tempPet;
    }
}
