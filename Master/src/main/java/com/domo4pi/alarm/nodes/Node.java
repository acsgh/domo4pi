package com.domo4pi.alarm.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Node {

    @XmlTransient
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @XmlAttribute
    public String name;

    public abstract void addPinListener(NodePinListener nodePinListener);

}
