package com.domo4pi.core.serial;

import com.domo4pi.core.AbstractManager;
import com.domo4pi.core.properties.CoreProperties;
import com.domo4pi.core.properties.CoreProperty;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.inject.Inject;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

public class SerialManagerPi4J extends AbstractManager implements SerialManager {

    private final Serial serial;
    private final Dispatcher dispatcher;
    private final CoreProperties coreProperties;


    @Inject
    public SerialManagerPi4J(Dispatcher dispatcher, CoreProperties coreProperties) {
        serial = SerialFactory.createInstance();
        this.dispatcher = dispatcher;
        this.coreProperties = coreProperties;
    }

    @Override
    public void start() {
        int speed = coreProperties.getInteger(CoreProperty.Speed);
        log.info("Started serial at: {} baudios", speed);
        serial.open(Serial.DEFAULT_COM_PORT, speed);

        serial.addListener(new SerialDataListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                dispatcher.dispatchEvent(new IncomingDataEvent(event.getData()));
            }
        });
    }

    @Override
    public void stop() {
        serial.shutdown();
    }

    @Override
    public void flush() throws IllegalStateException {
        serial.flush();
    }

    @Override
    public char read() throws IllegalStateException {
        return serial.read();
    }

    @Override
    public void write(char data) throws IllegalStateException {
        serial.write(data);
    }

    @Override
    public void write(char[] data) throws IllegalStateException {
        serial.write(data);
    }

    @Override
    public void write(byte data) throws IllegalStateException {
        serial.write(data);
    }

    @Override
    public void write(byte[] data) throws IllegalStateException {
        serial.write(data);
    }

    @Override
    public void write(String data) throws IllegalStateException {
        serial.write(data);
    }

    @Override
    public void writeln(String data) throws IllegalStateException {
        serial.writeln(data);
    }

    @Override
    public void write(String data, String... args) throws IllegalStateException {
        serial.write(data, args);
    }

    @Override
    public void writeln(String data, String... args) throws IllegalStateException {
        serial.writeln(data, args);
    }

    @Override
    public int availableBytes() throws IllegalStateException {
        return serial.availableBytes();
    }
}
