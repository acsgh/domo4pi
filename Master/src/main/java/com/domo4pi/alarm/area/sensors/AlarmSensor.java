package com.domo4pi.alarm.area.sensors;

import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class AlarmSensor extends Sensor {

    @XmlAttribute(name = "name")
    public String name;
    @XmlAttribute
    public boolean defaultState = false;
    @XmlTransient
    public Boolean value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append("{");
        sb.append("pin=").append(pin);
        sb.append(", name=").append(name);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void onStatusChange(boolean newState) {
        if (!area.getUnderSabotage()) {
            boolean newValue = getRealValue(newState);

            if (newValue) {
                switch (area.getStatus()) {
                    case Activated:
                    case Suspicius:
                        area.changeStatus(ProtocolStatus.Suspicius);
                        area.activeSensors.add(getName());
                        break;
                }
            }
        }
    }

    protected boolean getRealValue(boolean newState) {
        boolean result = newState;

        if (defaultState) {
            result = !result;
        }

        return result;
    }
}
