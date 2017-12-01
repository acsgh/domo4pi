package com.domo4pi.alarm.area.sensors;

import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Key extends Sensor {
    @Override
    public String getName() {
        return "Key";
    }

    @Override
    public void onStatusChange(boolean newState) {
        if ((!area.getUnderSabotage()) && (!newState)) {
            switch (area.getStatus()) {
                case Iddle:
                    area.changeStatus(ProtocolStatus.Activating);
                    break;
                default:
                    area.changeStatus(ProtocolStatus.Iddle);
                    break;
            }
        }
    }
}
