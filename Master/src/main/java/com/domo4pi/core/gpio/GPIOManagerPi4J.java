package com.domo4pi.core.gpio;

import com.domo4pi.core.AbstractManager;
import com.domo4pi.utils.ExceptionUtils;
import com.pi4j.io.gpio.*;

public class GPIOManagerPi4J extends AbstractManager implements GPIOManager {

    private GpioController controller;

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(String name, Pin gpioPin) {
        return controller.provisionDigitalOutputPin(gpioPin);
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(String name, Integer gpioPin) {
        return provisionDigitalOutputPin(name, getGPIOPin(gpioPin));
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(String name, Pin gpioPin) {
        return controller.provisionDigitalInputPin(gpioPin);
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(String name, Integer gpioPin) {
        return provisionDigitalInputPin(name, getGPIOPin(gpioPin));
    }

    @Override
    public Pin getGPIOPin(Integer pinNumber) {
        Pin result = null;
        try {
            String pinString = Integer.toString(pinNumber);

            while (pinString.length() < 2) {
                pinString = "0" + pinString;
            }
            result = (Pin) RaspiPin.class.getDeclaredField("GPIO_" + pinString).get(RaspiPin.class);
        } catch (Exception e) {
            ExceptionUtils.throwRuntimeException(e);
        }
        return result;
    }

    @Override
    public void start() {
        controller = GpioFactory.getInstance();

    }

    @Override
    public void stop() {
        controller.shutdown();
    }
}
