package com.TtPP.memento;

import com.TtPP.entities.City;

import java.util.HashMap;
import java.util.Stack;

public class CitiesHistory {
    private HashMap<Integer, Stack<CityMemento>> map = new HashMap<>();

    public City revert (int cityId) {
        if (map.containsKey(cityId)) {
            Stack<CityMemento> stateHistory = map.get(cityId);

            if (stateHistory.size() > 0) {
                CityMemento cityMemento = stateHistory.pop();

                return cityMemento.restore();
            }
        }

        return null;
    }

    public void clearHistoryForCity (int cityId) {
        map.remove(cityId);
    }

    public void rememberState (int cityId, CityMemento memento) {
        if (!map.containsKey(cityId)) {
            Stack<CityMemento> stateHistory = new Stack<>();
            stateHistory.add(memento);

            map.put(cityId, stateHistory);
        } else {
            Stack<CityMemento> stateHistory = map.get(cityId);
            stateHistory.add(memento);

            map.put(cityId, stateHistory);
        }
    }
}
