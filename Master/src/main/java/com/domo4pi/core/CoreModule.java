package com.domo4pi.core;

import com.domo4pi.application.Module;
import com.domo4pi.core.gpio.GPIOManager;
import com.domo4pi.core.gpio.GPIOManagerPi4J;
import com.domo4pi.core.mock.MockApp;
import com.domo4pi.core.mock.gpio.GPIOManagerMock;
import com.domo4pi.core.mock.serial.SerialManagerMock;
import com.domo4pi.core.properties.CoreProperties;
import com.domo4pi.core.properties.CoreProperty;
import com.domo4pi.core.serial.SerialManager;
import com.domo4pi.core.serial.SerialManagerPi4J;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class CoreModule implements Module {

    @Inject
    private GPIOManager gpioManager;

    @Inject
    private SerialManager serialManager;

    @Inject
    private CoreProperties coreProperties;

    private GpioPinDigitalOutput runningPin;

    @Override
    public void configure() {
    }

    @Override
    public void start() {
        gpioManager.start();
        serialManager.start();

        runningPin = gpioManager.provisionDigitalOutputPin("Running", coreProperties.getGPIOPin(CoreProperty.RunningPin));
        runningPin.high();
    }

    @Override
    public void stop() {
        serialManager.stop();
        runningPin.low();
        gpioManager.stop();
    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(GPIOManager.class).as(GPIOManagerPi4J.class).singleton();
        injector.bind(SerialManager.class).as(SerialManagerPi4J.class).singleton();

        injector.bind(Dispatcher.class).singleton();
    }
}
