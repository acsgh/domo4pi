package com.domo4pi.utils.properties;

import com.domo4pi.application.Application;
import com.domo4pi.core.gpio.GPIOManager;
import com.pi4j.io.gpio.Pin;

public class PropertiesFacade<T extends Property> {

    public Pin getGPIOPin(T key) {
        return Application.getInstance(GPIOManager.class).getGPIOPin(getInteger(key));
    }

    public String getString(T key) {
        return Application.getInstance().getProperties().getProperty(key.getKey(), key.getDefaultValue());
    }

    public Boolean getBoolean(T key) {
        return Boolean.parseBoolean(getString(key));
    }

    public Integer getInteger(T key) {
        return Integer.parseInt(getString(key));
    }

    public Long getLong(T key) {
        return Long.parseLong(getString(key));
    }

    public Double getDouble(T key) {
        return Double.parseDouble(getString(key));
    }
}
