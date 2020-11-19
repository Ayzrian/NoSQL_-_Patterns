package com.TtPP.entities;

public class Owner {
    private int ownerId;
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String email = "";
    private int fkCityId;


    public Owner(
            int ownerId,
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            int fkCityId
    ) {
        this.ownerId = ownerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fkCityId = fkCityId;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(int ownerId) {
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

    public int getFkCityId() {
        return this.fkCityId;
    }

    public void setFkCityId(int fkCityId) {
        this.fkCityId = fkCityId;
    }

    public String toString() {
        return "[ ownerId = " + this.ownerId + " ] " + this.firstName + " " + this.lastName + " " + this.email + " " + this.phoneNumber + " from city with id " + this.fkCityId;
    }
}
