package com.TtPP.builders;

import com.TtPP.entities.City;

public class CityBuilder {
    private City city = new City(-1, "");

    public CityBuilder withName(String name) {
        this.city.setName(name);
        return this;
    }

    public CityBuilder withId (int id) {
        this.city.setCityId(id);
        return this;
    }

    public City build () {
        City tempCity = this.city;
        this.city = new City(-1, "");
        return tempCity;
    }
}
