package com.domo4pi.saldo.properties;

import com.domo4pi.utils.properties.Property;

public enum SaldoProperty implements Property {
    SaldoAlarmThreshold("Saldo.threshold", "2");

    private final String key;
    private final String defaultValue;

    SaldoProperty(String key, String defaultValue) {
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
