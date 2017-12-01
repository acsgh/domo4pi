package com.domo4pi.core.mock.gpio;

import com.domo4pi.core.gpio.GPIOManager;
import com.domo4pi.gsm.properties.GSMProperties;
import com.domo4pi.gsm.properties.GSMProperty;
import com.domo4pi.utils.ExceptionUtils;
import com.domo4pi.utils.inject.Inject;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;

public class GPIOManagerMock implements GPIOManager {

    private final Map<Pin, GpioPinDigital> pins = new HashMap<>();
    private GPIOManagerMockController guiController;

    private GSMProperties gsmProperties;

    private GpioPinDigitalOutput gsmPowerPin;
    private GpioPinDigitalInput gsmPowerCheckPin;

    @Inject
    public GPIOManagerMock(GSMProperties gsmProperties) {
        this.gsmProperties = gsmProperties;
    }

    public void setGuiController(GPIOManagerMockController guiController) {
        this.guiController = guiController;
    }

    @Override
    public synchronized GpioPinDigitalOutput provisionDigitalOutputPin(String name, Pin gpioPin) {
        GpioPinDigital pin = pins.get(gpioPin);

        if (pin == null) {
            pin = new OutpuPin(name, gpioPin, this);
            pins.put(gpioPin, pin);
        } else if (!(pin instanceof GpioPinDigitalOutput)) {
            throw new IllegalStateException("The pin " + gpioPin.getName() + " is already assigned as input pin");
        } else if (!name.equals(pin.getName())) {
            throw new IllegalStateException("The pin " + gpioPin.getName() + " is already assigned as " + pin.getName());
        }
        updateGUIController();
        return (GpioPinDigitalOutput) pin;
    }

    @Override
    public synchronized GpioPinDigitalOutput provisionDigitalOutputPin(String name, Integer gpioPin) {
        return provisionDigitalOutputPin(name, getGPIOPin(gpioPin));
    }

    @Override
    public synchronized GpioPinDigitalInput provisionDigitalInputPin(String name, Pin gpioPin) {
        GpioPinDigital pin = pins.get(gpioPin);

        if (pin == null) {
            pin = new InputPin(name, gpioPin);
            pins.put(gpioPin, pin);

            if ((name.equals("Sabotage")) || (name.equals("Key"))) {
                ((InputPin) pin).setState(PinState.HIGH);
            }
        } else if (!(pin instanceof GpioPinDigitalInput)) {
            throw new IllegalStateException("The pin " + gpioPin.getName() + " is already assigned as output pin");
        } else {
            pin.setName(name);
        }
        updateGUIController();
        return (GpioPinDigitalInput) pin;
    }

    public void updateGUIController() {
        if (guiController != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    guiController.updateState();
                }
            });
        }
    }

    @Override
    public synchronized GpioPinDigitalInput provisionDigitalInputPin(String name, Integer gpioPin) {
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
        gsmPowerPin = provisionDigitalOutputPin("GSM Toggle Power", gsmProperties.getGPIOPin(GSMProperty.PowerPin));
        gsmPowerCheckPin = provisionDigitalInputPin("GSM Power", gsmProperties.getGPIOPin(GSMProperty.PowerCheckPin));

        final OutpuPin powerPin = (OutpuPin) gsmPowerPin;
        final InputPin inPin = (InputPin) gsmPowerCheckPin;
        powerPin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (gsmPowerPin.isHigh()) {
                    inPin.setState(inPin.isHigh() ? PinState.LOW : PinState.HIGH);
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    public Map<Pin, GpioPinDigital> getPins() {
        return pins;
    }
}
