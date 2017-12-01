package com.domo4pi.core.mock.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.trigger.GpioTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class InputPin implements GpioPinDigitalInput {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Pin gpioPin;

    private String name;
    private PinState state = PinState.LOW;


    private final List<GpioPinListener> listeners = new ArrayList<>();

    public InputPin(String name, Pin gpioPin) {
        this.name = name;
        this.gpioPin = gpioPin;
    }

    public void setState(PinState state) {
        this.state = state;

        log.info("GPIO Input changed {} - {}", gpioPin, state);

        callListener(state);
    }

    private void callListener(PinState state) {
        for (GpioPinListener rawListener : listeners) {
            GpioPinListenerDigital digitalListener = (GpioPinListenerDigital) rawListener;
            digitalListener.handleGpioPinDigitalStateChangeEvent(new GpioPinDigitalStateChangeEvent(this, this, state));
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

    @Override
    public Collection<GpioPinListener> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(GpioPinListener... listener) {
        addListener(Arrays.asList(listener));
    }

    @Override
    public void addListener(List<? extends GpioPinListener> listeners) {
        for (GpioPinListener listener : listeners) {
            this.listeners.add(listener);
        }
    }

    @Override
    public boolean hasListener(GpioPinListener... listener) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void removeListener(GpioPinListener... listener) {
        listeners.removeAll(listeners);
    }

    @Override
    public void removeListener(List<? extends GpioPinListener> listeners) {
        listeners.removeAll(listeners);
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public Collection<GpioTrigger> getTriggers() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void addTrigger(GpioTrigger... trigger) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void addTrigger(List<? extends GpioTrigger> triggers) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void removeTrigger(GpioTrigger... trigger) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void removeTrigger(List<? extends GpioTrigger> triggers) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void removeAllTriggers() {
        throw new RuntimeException("Not implemented yet");
    }
}