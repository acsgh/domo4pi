package com.domo4pi.application;

import com.domo4pi.alarm.AlarmDefinition;
import com.domo4pi.utils.xml.properties.PropertiesAdaptor;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Properties;

@XmlRootElement(name = "Application")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationDefinition {

    @XmlAttribute(name = "name")
    public String name;

    @XmlElement(name = "Properties")
    @XmlJavaTypeAdapter(PropertiesAdaptor.class)
    public Properties properties;

    @XmlElement(name = "Alarm")
    public AlarmDefinition alarmDefinition;
}
