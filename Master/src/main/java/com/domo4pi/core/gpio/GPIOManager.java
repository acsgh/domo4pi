package com.domo4pi.core.gpio;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

public interface GPIOManager {
    public GpioPinDigitalOutput provisionDigitalOutputPin(String name, Pin gpioPin);

    public GpioPinDigitalOutput provisionDigitalOutputPin(String name, Integer gpioPin);

    public GpioPinDigitalInput provisionDigitalInputPin(String name, Pin gpioPin);

    public GpioPinDigitalInput provisionDigitalInputPin(String name, Integer gpioPin);

    public Pin getGPIOPin(Integer pinNumber);

    public void start();

    public void stop();
}
