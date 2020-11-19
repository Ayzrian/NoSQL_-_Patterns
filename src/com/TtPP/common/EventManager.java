package com.TtPP.common;

import com.TtPP.DAO.IDAO;

import java.util.*;

public class EventManager {
    private Map<String, List<IEventListener>> listeners = new HashMap<>();

    public void subscribe (String eventType, IEventListener listener) {
        if (listeners.containsKey(eventType)) {
            List<IEventListener> listenersArray = listeners.get(eventType);
            listenersArray.add(listener);
        } else {
            List <IEventListener> array = new ArrayList<>();
            array.add(listener);
            listeners.put(eventType, array);
        }
    }

    public void unsubscribe (String eventType, IEventListener listener) {
        listeners.get(eventType).remove(listener);
    }

    public void notify (String eventType, IDAO dao) {
        for (IEventListener listener: listeners.get(eventType))
            listener.update(dao);
    }
}
