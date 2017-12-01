package com.domo4pi.alarm.area.sensors;

import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class RemovableSensor extends AlarmSensor {
    @XmlTransient
    public boolean enabled = true;

    @Override
    public void onAreaStatusChange(ProtocolStatus protocolStatus) {
        switch (protocolStatus) {
            case Activated:
                enabled = value;
                break;
            case Iddle:
                enabled = true;
                break;
        }
    }

    @Override
    public void onStatusChange(boolean newState) {
        if (enabled) {
            super.onStatusChange(newState);
        }
    }


}
