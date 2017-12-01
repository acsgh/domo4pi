package com.domo4pi.alarm.area.sensors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Sabotage extends Sensor {

    @Override
    public String getName() {
        return "Sabotage";
    }

    @Override
    public void onStatusChange(boolean newState) {
        if (!newState) {
            area.setUnderSabotage(true);
        }
    }
}
