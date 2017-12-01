package com.domo4pi.core.mock.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.*;
import java.util.concurrent.Future;

public class OutpuPin implements GpioPinDigitalOutput {

    private final Pin gpioPin;

    private String name;
    private PinState state = PinState.LOW;

    private final GPIOManagerMock gpioManagerMock;

    private final List<GpioPinListener> listeners = new ArrayList<>();

    public OutpuPin(String name, Pin gpioPin, GPIOManagerMock gpioManagerMock) {
        this.name = name;
        this.gpioPin = gpioPin;
        this.gpioManagerMock = gpioManagerMock;
    }


    @Override
    public void high() {
        state = PinState.HIGH;
        gpioManagerMock.updateGUIController();
        callListener();
    }

    @Override
    public void low() {
        state = PinState.LOW;
        gpioManagerMock.updateGUIController();
        callListener();
    }

    @Override
    public void toggle() {
        if (isHigh()) {
            low();
        } else {
            high();
        }
    }

    private void callListener() {
        for (GpioPinListener rawListener : listeners) {
            GpioPinListenerDigital digitalListener = (GpioPinListenerDigital) rawListener;
            digitalListener.handleGpioPinDigitalStateChangeEvent(new GpioPinDigitalStateChangeEvent(this, this, state));
        }
    }

    public Collection<GpioPinListener> getListeners() {
        return listeners;
    }

    public void addListener(GpioPinListener... listener) {
        addListener(Arrays.asList(listener));
    }

    public void addListener(List<? extends GpioPinListener> listeners) {
        for (GpioPinListener listener : listeners) {
            this.listeners.add(listener);
        }
    }

    public boolean hasListener(GpioPinListener... listener) {
        throw new RuntimeException("Not implemented yet");
    }

    public void removeListener(GpioPinListener... listener) {
        listeners.removeAll(listeners);
    }

    public void removeListener(List<? extends GpioPinListener> listeners) {
        listeners.removeAll(listeners);
    }

    public void removeAllListeners() {
        listeners.clear();
    }


    @Override
    public Future<?> blink(long delay) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> blink(long delay, PinState blinkState) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> blink(long delay, long duration) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> blink(long delay, long duration, PinState blinkState) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> pulse(long duration) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> pulse(long duration, boolean blocking) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, boolean blocking) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setState(PinState state) {
        this.state = state;
        gpioManagerMock.updateGUIController();
        callListener();
    }

    @Override
    public void setState(boolean state) {
        if (state) {
            high();
        } else {
            low();
        }
    }

    @Override
    public boolean isHigh() {
        return state.isHigh();
    }

    @Override
    public boolean isLow() {
        return state.isLow();
    }

    @Override
    public PinState getState() {
        return state;
    }

    @Override
    public boolean isState(PinState state) {
        return (this.state == state);
    }

    @Override
    public GpioProvider getProvider() {
        return null;
    }

    @Override
    public Pin getPin() {
        return gpioPin;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        gpioManagerMock.updateGUIController();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setTag(Object tag) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Object getTag() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setProperty(String key, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean hasProperty(String key) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public String getProperty(String key) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Map<String, String> getProperties() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void removeProperty(String key) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void clearProperties() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void export(PinMode mode) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void unexport() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean isExported() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setMode(PinMode mode) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public PinMode getMode() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean isMode(PinMode mode) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setPullResistance(PinPullResistance resistance) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public PinPullResistance getPullResistance() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public GpioPinShutdown getShutdownOptions() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown options) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setShutdownOptions(Boolean unexport) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode) {
        throw new RuntimeException("Not implemented yet");
    }
}