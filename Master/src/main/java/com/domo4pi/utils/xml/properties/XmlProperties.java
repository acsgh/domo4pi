package com.domo4pi.utils.xml.properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlProperties {
    @XmlElement(name = "Property", required = true)
    private final List<XmlProperty> properties = new ArrayList<XmlProperty>();

    public List<XmlProperty> getProperties() {
        return properties;
    }
}