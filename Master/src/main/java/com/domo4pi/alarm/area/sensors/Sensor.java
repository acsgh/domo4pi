package com.domo4pi.alarm.area.sensors;

import com.domo4pi.alarm.area.Area;
import com.domo4pi.alarm.nodes.NodePinListener;
import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Sensor implements NodePinListener {
    @XmlAttribute
    public String node;
    @XmlAttribute
    public int pin;

    @XmlTransient
    public Area area;

    @Override
    public int getPinNumber() {
        return pin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append("{");
        sb.append("node=").append(node);
        sb.append(", pin=").append(pin);
        sb.append('}');
        return sb.toString();
    }

    public void onAreaStatusChange(ProtocolStatus protocolStatus) {

    }
}
