package com.NoSQl;

public class Owner<IDType> {
    private IDType ownerId;
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String email = "";
    private IDType fkCityId;


    public Owner(
            IDType ownerId,
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            IDType fkCityId
    ) {
        this.ownerId = ownerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fkCityId = fkCityId;
    }

    public IDType getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(IDType ownerId) {
        this.ownerId = ownerId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return  this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IDType getFkCityId() {
        return this.fkCityId;
    }

    public void setFkCityId(IDType fkCityId) {
        this.fkCityId = fkCityId;
    }

    public String toString() {
        return "[ ownerId = " + this.ownerId + " ] " + this.firstName + " " + this.lastName + " " + this.email + " " + this.phoneNumber + " from city with id " + this.fkCityId;
    }
}
