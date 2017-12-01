package com.domo4pi.alarm.area.registry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Alarm")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlarmRegistry {

    @XmlElement(name = "area")
    public List<AreaPreview> areas = new ArrayList<>();
}
