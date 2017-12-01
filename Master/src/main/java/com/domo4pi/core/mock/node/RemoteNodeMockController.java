package com.domo4pi.core.mock.node;

import com.domo4pi.alarm.AlarmDefinition;
import com.domo4pi.alarm.area.Area;
import com.domo4pi.alarm.area.sensors.AlarmSensor;
import com.domo4pi.alarm.area.sensors.Safety;
import com.domo4pi.alarm.area.sensors.Sensor;
import com.domo4pi.alarm.nodes.RemoteNode;
import com.domo4pi.alarm.nodes.protocol.ProtocolCommand;
import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;
import com.domo4pi.alarm.nodes.protocol.Status;
import com.domo4pi.application.Application;
import com.domo4pi.core.gpio.GPIOManager;
import com.domo4pi.core.mock.gpio.GPIOManagerMock;
import com.domo4pi.core.mock.gpio.InputPin;
import com.pi4j.io.gpio.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.font.TextLabel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RemoteNodeMockController extends Thread {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @FXML
    private Label statusLabel;
    @FXML
    private CheckBox connected;

    @FXML
    private CheckBox pin0;
    @FXML
    private CheckBox pin1;
    @FXML
    private CheckBox pin2;
    @FXML
    private CheckBox pin3;
    @FXML
    private CheckBox pin4;
    @FXML
    private CheckBox pin5;


    @FXML
    private CheckBox pin8;
    @FXML
    private CheckBox pin9;
    @FXML
    private CheckBox pin10;
    @FXML
    private CheckBox pin11;

    private ProtocolStatus protocolStatus = ProtocolStatus.Iddle;
    private byte[] sensors = {0, 0};

    private AlarmDefinition alarmDefinition;
    private RemoteNode remoteNode;

    public void setRemoteNode(String nodeName) {
        alarmDefinition = Application.getInstance().getAlarmDefinition();
        remoteNode = (RemoteNode) alarmDefinition.getNode(nodeName);
        updateState();
        initCheckboxes();
        start();
    }

    private void initCheckboxes(int index, CheckBox checkBox) {
        AlarmDefinition alarmDefinition = Application.getInstance().getAlarmDefinition();

        boolean selected = false;
        for (Area area : alarmDefinition.areas) {
            for (Sensor sensor : area.sensors) {
                if (sensor.node.equals(remoteNode.name)) {
                    if (sensor.getPinNumber() == index) {
                        checkBox.setText(index + ": " + getName(sensor));
                        checkBox.setVisible(true);
                        selected = (sensor.getName().equals("Key")) || (sensor.getName().equals("Sabotage"));
                    }
                }
            }
        }
        checkBox.selectedProperty().addListener(new CheckBoxChangeListener(index));
        checkBox.setSelected(selected);
    }

    private String getName(Sensor sensor) {
        if (sensor instanceof AlarmSensor) {
            return ((AlarmSensor) sensor).name;
        } else if (sensor instanceof Safety) {
            return ((Safety) sensor).name;
        }
        return sensor.getClass().getSimpleName();
    }

    private void initCheckboxes() {
        initCheckboxes(0, pin0);
        initCheckboxes(1, pin1);
        initCheckboxes(2, pin2);
        initCheckboxes(3, pin3);
        initCheckboxes(4, pin4);
        initCheckboxes(5, pin5);
        initCheckboxes(8, pin8);
        initCheckboxes(9, pin9);
        initCheckboxes(10, pin10);
        initCheckboxes(11, pin11);
    }

    @Override
    public void run() {
        try {
            log.info("Start remote node mock '{}' at: {}", remoteNode.name, remoteNode.getPort());
            ServerSocket serverSocket = new ServerSocket(remoteNode.getPort());
            Socket socket;
            while (true) {
                socket = serverSocket.accept();

                if (connected.isSelected()) {

                    OutputStream out = socket.getOutputStream();
                    InputStream in = socket.getInputStream();

                    byte command = (byte) in.read();

                    ProtocolCommand protocolCommand = ProtocolCommand.getFromCode(command);
                    switch (protocolCommand) {
                        case GetStatus:
                            log.info("Ask for status");
                            writeStatus(out, protocolStatus);
                            break;
                        case SetStatus:
                            byte newStatus = (byte) in.read();
                            ProtocolStatus protocolStatus = ProtocolStatus.getFromCode(newStatus);

                            log.info("Request status change: " + protocolStatus);
                            this.protocolStatus = protocolStatus;

                            writeStatus(out, protocolStatus);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    updateState();
                                }
                            });
                            break;
                    }

                    out.flush();
                    out.close();
                } else {
                    socket.close();
                }
            }
        } catch (IOException e) {
            log.error("Exception: {}", e);
        }
    }

    private void writeStatus(OutputStream out, ProtocolStatus protocolStatus) throws IOException {
        out.write(protocolStatus.code);
        out.write(sensors);

        String b1 = String.format("%8s", Integer.toBinaryString(sensors[0] & 0xFF)).replace(' ', '0');
        String b2 = String.format("%8s", Integer.toBinaryString(sensors[1] & 0xFF)).replace(' ', '0');
        log.debug("Send status {} - {} node: {} {}", remoteNode.name, protocolStatus, b1, b2);
    }

    public void updateState() {
        statusLabel.setText("S: " + protocolStatus);
    }

    private class CheckBoxChangeListener implements ChangeListener<Boolean> {
        private final int pinKey;

        public CheckBoxChangeListener(int pinKey) {
            this.pinKey = pinKey;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (oldValue != newValue) {
                setBitValue(pinKey, newValue);
            }
        }
    }

    private void setBitValue(int index, boolean value) {
        int sensorIndex = index / 8;
        int sensorPosition = index % 8;

        if (value) {
            byte mask = (byte) (1 << sensorPosition);
            sensors[sensorIndex] |= mask;
        } else {
            byte mask = (byte) ~(1 << sensorPosition);
            sensors[sensorIndex] &= mask;
        }
    }
}
