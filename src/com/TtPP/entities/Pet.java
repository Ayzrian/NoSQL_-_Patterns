package com.TtPP.entities;

import java.util.Date;

public class Pet {
    private int petId;
    private int fkParentId;
    private int fkOwnerId;
    private int fkKindId;
    private int fkBreedId;
    private String name = "";
    private Date dateOfBirth = new Date();
    private boolean sex = false;

    public Pet(
            int petId,
            int fkParentId,
            int fkOwnerId,
            int fkKindId,
            int fkBreedId,
            String name,
            Date dateOfBirth,
            boolean sex
    ) {
        this.petId = petId;
        this.fkParentId = fkParentId;
        this.fkOwnerId = fkOwnerId;
        this.fkKindId = fkKindId;
        this.fkBreedId = fkBreedId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public int getPetId() {
        return this.petId;
    }

    public int getFkParentId() {
        return this.fkParentId;
    }

    public int getFkOwnerId() {
        return this.fkOwnerId;
    }

    public int getFkKindId() {
        return this.fkKindId;
    }

    public int getFkBreedId() {
        return this.fkBreedId;
    }

    public String getName() {
        return this.name;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public boolean getSex() {
        return this.sex;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public void setFkParentId(int fkParentId) {
        this.fkParentId = fkParentId;
    }

    public void setFkOwnerId(int fkOwnerId) {
        this.fkOwnerId = fkOwnerId;
    }

    public void setFkKindId(int fkKindId) {
        this.fkKindId = fkKindId;
    }

    public void setFkBreedId(int fkBreedId) {
        this.fkBreedId = fkBreedId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String toString() {
        return "[petId = " + this.petId + "] " + this.name;
    }
}
