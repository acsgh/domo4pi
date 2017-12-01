package com.domo4pi.gsm.properties;

import com.domo4pi.utils.properties.Property;

public enum GSMProperty implements Property {
    SMSEnabled("GSM.smsEnabled", "true"),
    PowerPin("GSM.powerPin", "0"),
    PowerCheckPin("GSM.powerCheckPin", "1");

    private final String key;
    private final String defaultValue;

    GSMProperty(String key) {
        this(key, null);
    }

    GSMProperty(String key, String defaultValue) {
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
