package com.domo4pi.alarm.properties;

import com.domo4pi.utils.properties.Property;

public enum AlarmProperty implements Property {
    RemoteTimeInitMillis("Alarm.remoteTimeInitMillis", "2000"),
    MaxUnreadUntilSabotage("Alarm.maxUnreadUntilSabotage", "2"),
    SleepTimeMillis("Alarm.sleepTimeMillis", "500"),
    SocketMillisecondsTimeout("Alarm.remoteNodeSocketMillisecondsTimeout", "200"),
    SabotagePin("Alarm.sabotagePin", "4"),
    SirenPin("Alarm.sirenPin", "3"),
    ActivatingSeconds("Alarm.activatingSeconds", "60"),
    SuspiciousSeconds("Alarm.suspiciousSeconds", "60"),
    SabotageRearmTime("Alarm.sabotageWaitTime", "8000");


    private final String key;
    private final String defaultValue;

    AlarmProperty(String key) {
        this(key, null);
    }

    AlarmProperty(String key, String defaultValue) {
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
