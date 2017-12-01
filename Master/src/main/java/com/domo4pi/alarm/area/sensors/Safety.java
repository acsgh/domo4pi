package com.domo4pi.alarm.area.sensors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Safety extends Sensor {

    @XmlAttribute(name = "name")
    public String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onStatusChange(boolean newState) {
        if (newState) {
           area.setSafetySensor(this);
        }
    }
}
