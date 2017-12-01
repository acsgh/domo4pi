package com.domo4pi.core.mock.serial;

import com.domo4pi.core.AbstractManager;
import com.domo4pi.core.properties.CoreProperties;
import com.domo4pi.core.properties.CoreProperty;
import com.domo4pi.core.serial.IncomingDataEvent;
import com.domo4pi.core.serial.SerialManager;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.inject.Inject;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;

public class SerialManagerMock extends AbstractManager implements SerialManager {

    private final CoreProperties coreProperties;
    private final Dispatcher dispatcher;

    private SerialManagerMockController serialController;
    private String text = "";

    public void setSerialController(SerialManagerMockController serialController) {
        this.serialController = serialController;
    }

    private final Map<String, String> autoResponses = new HashMap<>();

    @Inject
    public SerialManagerMock(CoreProperties coreProperties, Dispatcher dispatcher) {
        this.coreProperties = coreProperties;
        this.dispatcher = dispatcher;

        autoResponses.put("AT", "OK\r\n");
        autoResponses.put("AT+COPS?", "YOIGO\r\nOK\r\n");
        autoResponses.put("AT+CMGD=1,4", "OK\r\n");
        autoResponses.put("AT+CMGF=1", "OK\r\n");
    }

    @Override
    public void flush() throws IllegalStateException {

    }

    @Override
    public char read() throws IllegalStateException {
        return 0;
    }

    public void incomingData(String data) {
        dispatcher.dispatchEvent(new IncomingDataEvent(data + "\r\n"));
    }

    @Override
    public void write(char data) throws IllegalStateException {
        text += data;
        updateGUIController();
    }

    @Override
    public void write(char[] data) throws IllegalStateException {
        text += data;
        updateGUIController();
    }

    @Override
    public void write(byte data) throws IllegalStateException {
        text += data;
        updateGUIController();

        if (data == ((byte) 0x1A)) {
            text += "\r\n";
            dispatcher.dispatchEvent(new IncomingDataEvent("OK\r\n"));
        }
    }

    @Override
    public void write(byte[] data) throws IllegalStateException {
        text += new String(data);
        updateGUIController();
    }

    @Override
    public void write(String data) throws IllegalStateException {
        text += data;
        updateGUIController();
    }


    @Override
    public void writeln(String data) throws IllegalStateException {
        text += data + "\r\n";
        updateGUIController();

        if (autoResponses.containsKey(data)) {
            dispatcher.dispatchEvent(new IncomingDataEvent(autoResponses.get(data)));
        } else if (data.startsWith("AT+CMGD=")) {
            dispatcher.dispatchEvent(new IncomingDataEvent("OK\r\n"));
        } else if (data.startsWith("AT+CUSD=1,\"*111#\"")) {
            dispatcher.dispatchEvent(new IncomingDataEvent("OK\r\n"));
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dispatcher.dispatchEvent(new IncomingDataEvent("+CUSD: \"Saldo 1.0 euros\"\r\n"));
                }
            }.start();
        }
    }

    @Override
    public void write(String data, String... args) throws IllegalStateException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void writeln(String data, String... args) throws IllegalStateException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int availableBytes() throws IllegalStateException {
        return 0;
    }

    public void updateGUIController() {
        if (serialController != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    serialController.updateState();
                }
            });
        }
    }

    @Override
    public void start() {
        int speed = coreProperties.getInteger(CoreProperty.Speed);
        log.info("Started serial at: {} baudios", speed);
    }

    @Override
    public void stop() {

    }

    public String getText() {
        return text;
    }
}
