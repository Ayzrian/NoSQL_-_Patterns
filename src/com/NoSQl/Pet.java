package com.NoSQl;

import java.util.Date;

public class Pet<IDType> {
    private IDType petId;
    private IDType fkParentId;
    private IDType fkOwnerId;
    private IDType fkKindId;
    private IDType fkBreedId;
    private String name = "";
    private Date dateOfBirth = new Date();
    private boolean sex = false;

    public Pet(
            IDType petId,
            IDType fkParentId,
            IDType fkOwnerId,
            IDType fkKindId,
            IDType fkBreedId,
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

    public IDType getPetId() {
        return this.petId;
    }

    public IDType getFkParentId() {
        return this.fkParentId;
    }

    public IDType getFkOwnerId() {
        return this.fkOwnerId;
    }

    public IDType getFkKindId() {
        return this.fkKindId;
    }

    public IDType getFkBreedId() {
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

    public void setPetId(IDType petId) {
        this.petId = petId;
    }

    public void setFkParentId(IDType fkParentId) {
        this.fkParentId = fkParentId;
    }

    public void setFkOwnerId(IDType fkOwnerId) {
        this.fkOwnerId = fkOwnerId;
    }

    public void setFkKindId(IDType fkKindId) {
        this.fkKindId = fkKindId;
    }

    public void setFkBreedId(IDType fkBreedId) {
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
