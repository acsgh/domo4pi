package com.domo4pi.telnet.properties;

import com.domo4pi.utils.properties.Property;

public enum TelnetProperty implements Property {
    Port("Telnet.port", "8080"),
    Password("Telnet.password", "admin");

    private final String key;
    private final String defaultValue;

    private TelnetProperty(String key, String defaultValue) {
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
