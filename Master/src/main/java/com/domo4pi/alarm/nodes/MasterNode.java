package com.domo4pi.alarm.nodes;

import com.domo4pi.application.Application;
import com.domo4pi.core.gpio.GPIOManager;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MasterNode extends Node {

    @Override
    public void addPinListener(final NodePinListener nodePinListener) {
        GPIOManager gpioManager = Application.getInstance(GPIOManager.class);
        GpioPinDigitalInput input = gpioManager.provisionDigitalInputPin(nodePinListener.getName(), nodePinListener.getPinNumber());


        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                GpioPinDigitalInput pin = (GpioPinDigitalInput) event.getPin();
                nodePinListener.onStatusChange(pin.isHigh());
            }
        });
    }
}
