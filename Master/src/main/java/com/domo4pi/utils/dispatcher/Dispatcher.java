package com.domo4pi.utils.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {
    private Logger log = LoggerFactory.getLogger(getClass());
    private final Map<Class<? extends DispatcherEvent>, List<DispatcherListener>> listeners = new HashMap<>();


    public void dispatchEvent(final DispatcherEvent event) {
        Class<? extends DispatcherEvent> eventCategory = event.getClass();

        if (listeners.containsKey(eventCategory)) {
            List<DispatcherListener> typeListeners = listeners.get(eventCategory);
            for (DispatcherListener typeListener : typeListeners) {
                try {
                    typeListener.processEvent(event);
                } catch (Exception e) {
                    log.error("Unable to call the listener: " + typeListener + " with the event : " + event, e);
                }
            }
        }
    }

    public boolean addListener(Class<? extends DispatcherEvent> eventCategory, DispatcherListener listener) {
        if (!listeners.containsKey(eventCategory))
            listeners.put(eventCategory, new ArrayList<DispatcherListener>());

        return listeners.get(eventCategory).add(listener);
    }

    public boolean removeListener(Class<? extends DispatcherEvent> eventCategory, DispatcherListener listener) {
        boolean result = false;

        if (listeners.containsKey(eventCategory))
            result = listeners.get(eventCategory).remove(listener);

        return result;
    }

    public boolean containsListener(Class<? extends DispatcherEvent> eventCategory, DispatcherListener listener) {
        boolean result = false;

        if (listeners.containsKey(eventCategory))
            result = listeners.get(eventCategory).contains(listener);

        return result;
    }
}
