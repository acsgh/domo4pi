package com.domo4pi.alarm.nodes;

import com.domo4pi.alarm.nodes.protocol.ProtocolCommand;
import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;
import com.domo4pi.alarm.nodes.protocol.Status;
import com.domo4pi.alarm.properties.AlarmProperties;
import com.domo4pi.alarm.properties.AlarmProperty;
import com.domo4pi.application.Application;
import com.domo4pi.utils.ExceptionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@XmlAccessorType(XmlAccessType.FIELD)
public class RemoteNode extends Node {

    @XmlTransient
    private ReentrantLock reentrantLock = new ReentrantLock();

    @XmlTransient
    private ProtocolStatus protocolStatus;

    @XmlTransient
    private byte[] oldSensors = {0, 0};

    @XmlAttribute
    private String address;

    @XmlAttribute
    private int port;

    @XmlTransient
    private List<NodePinListener> pinListeners = new ArrayList<>();

    @Override
    public void addPinListener(NodePinListener listener) {
        pinListeners.add(listener);
    }

    public int getPort() {
        return port;
    }

    public void checkStatus() {
        Status status = sendComand(ProtocolCommand.GetStatus);
        onStatusReceived(status);
    }

    public ProtocolStatus getProtocolStatus() {
        return protocolStatus;
    }

    public void setStatus(ProtocolStatus protocolStatus) {
        log.info("Setting status of {} to {}", name, protocolStatus);

        try {
            sendComand(ProtocolCommand.SetStatus, protocolStatus.code);
        } catch (Exception e) {
            log.error("Unable to change status for remote node: " + name, e);
        }
    }

    private void onStatusReceived(Status status) {
        if (status.currentStatus != protocolStatus) {
            protocolStatus = status.currentStatus;
        }

        String b1 = String.format("%8s", Integer.toBinaryString(status.sensors[0] & 0xFF)).replace(' ', '0');
        String b2 = String.format("%8s", Integer.toBinaryString(status.sensors[1] & 0xFF)).replace(' ', '0');
        log.trace("New status {} node: {} {}", name, b1, b2);

        for (int i = 0; i < 16; i++) {
            boolean oldValue = getBitValue(oldSensors, i);
            boolean newValue = getBitValue(status.sensors, i);

            if (oldValue != newValue) {
                notifyListeners(i, newValue);
            }
        }

        oldSensors = status.sensors;
    }

    private void notifyListeners(int pinNumber, boolean newValue) {
        for (NodePinListener pinListener : pinListeners) {
            if (pinListener.getPinNumber() == pinNumber) {
                pinListener.onStatusChange(newValue);
            }
        }
    }

    private boolean getBitValue(byte[] sensors, int pin) {
        int sensorIndex = pin / 8;
        int sensorPosition = pin % 8;

        byte mask = (byte) (1 << sensorPosition);

        return ((mask & sensors[sensorIndex]) != 0);

    }

    private Status sendComand(ProtocolCommand command, byte... extraBytes) {
        int timeout = Application.getInstance(AlarmProperties.class).getInteger(AlarmProperty.SocketMillisecondsTimeout);
        Status status = new Status();
        byte[] response = new byte[command.responseLength];

        reentrantLock.lock();
        try {

            Socket socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(new InetSocketAddress(address, port), timeout);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write(command.code);
            out.write(extraBytes);

            int readed = 0;

            while (readed != response.length) {
                int read = in.read();

                if (read == -1) {
                    throw new IllegalArgumentException("Not enough bytes " + readed + "/" + response.length);
                }

                response[readed] = (byte) (read & (0xFF));
                readed++;
            }

            status.currentStatus = ProtocolStatus.getFromCode(response[0]);

            for (int i = 1; i < response.length; i++) {
                status.sensors[i - 1] = response[i];
            }

            socket.close();

        } catch (Exception e) {
            ExceptionUtils.throwRuntimeException(e);
        } finally {
            reentrantLock.unlock();
        }
        return status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SlaveArea{");
        sb.append("name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", port=").append(port);
        sb.append(", sensors=").append(oldSensors);
        sb.append('}');
        return sb.toString();
    }
}
