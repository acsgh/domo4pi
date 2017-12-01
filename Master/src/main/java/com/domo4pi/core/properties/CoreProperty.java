package com.domo4pi.core.properties;

import com.domo4pi.utils.properties.Property;

public enum CoreProperty implements Property {
    RunningPin("Core.runningPin", "6"),
    Speed("Core.serialSpeed", "19200");

    private final String key;
    private final String defaultValue;

    CoreProperty(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
