package com.domo4pi.alarm.area.registry;

import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class AreaPreview {
    @XmlAttribute
    public String name;

    @XmlAttribute(name = "status")
    public ProtocolStatus status = ProtocolStatus.Iddle;

    @XmlAttribute(name = "changeTime")
    public Long changeStatusTime = 0L;

    @XmlElementWrapper(name = "Sensors")
    @XmlElement(name = "Sensor")
    public Set<String> activeSensors = new HashSet<>();
}
