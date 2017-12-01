package com.domo4pi.core;

import com.domo4pi.core.gpio.GPIOManager;
import com.domo4pi.core.mock.MockApp;
import com.domo4pi.core.mock.gpio.GPIOManagerMock;
import com.domo4pi.core.mock.serial.SerialManagerMock;
import com.domo4pi.core.serial.SerialManager;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.inject.Injector;

public class CoreModuleMock extends CoreModule {

    @Override
    public void start() {
        MockApp.startGUI();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        MockApp.stopGUI();
    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(GPIOManager.class).as(GPIOManagerMock.class).singleton();
        injector.bind(SerialManager.class).as(SerialManagerMock.class).singleton();

        injector.bind(Dispatcher.class).singleton();
    }
}
