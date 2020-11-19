package com.TtPP.entities;

public class City {
    private int cityId;
    private String name = "";

    public City(int cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public int getCityId() {
        return this.cityId;
    }

    public void setCityId(int cityId) {
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

    public void restore (String backup) {
        this.name = backup;
    }

    public String backup() {
        return this.name;
    }
}
