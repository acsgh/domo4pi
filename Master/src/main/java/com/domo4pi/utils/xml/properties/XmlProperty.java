package com.domo4pi.utils.xml.properties;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlProperty {

    @XmlAttribute(name = "key", required = true)
    private final String key;

    @XmlValue
    private final String value;

    public XmlProperty() {
        key = null;
        value = null;
    }

    public XmlProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}