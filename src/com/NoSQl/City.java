package com.NoSQl;

public class City<IDType> {
    private IDType cityId;
    private String name = "";

    public City(IDType cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public IDType getCityId() {
        return this.cityId;
    }

    public void setCityId(IDType cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "[ cityId = " + this.cityId + " ] " + this.name;
    }
}
