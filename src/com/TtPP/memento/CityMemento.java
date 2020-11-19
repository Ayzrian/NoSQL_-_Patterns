package com.TtPP.memento;

import com.TtPP.entities.City;

public class CityMemento {
    private String backup;
    private City city;

    public CityMemento (City city) {
        this.city = city;
        this.backup = this.city.backup();
    }

    public City restore () {
        city.restore(backup);

        return city;
    }
}
