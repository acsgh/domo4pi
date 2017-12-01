package com.domo4pi.alarm.area;

import com.domo4pi.alarm.AlarmDefinition;
import com.domo4pi.alarm.area.sensors.*;
import com.domo4pi.alarm.nodes.Node;
import com.domo4pi.alarm.nodes.RemoteNode;
import com.domo4pi.alarm.nodes.RemoteNodeStatusListener;
import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;
import com.domo4pi.alarm.properties.AlarmProperties;
import com.domo4pi.alarm.properties.AlarmProperty;
import com.domo4pi.notification.NotificationsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@XmlAccessorType(XmlAccessType.FIELD)
public class Area implements RemoteNodeStatusListener {
    @XmlTransient
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @XmlAttribute
    public String name;

    @XmlAttribute(name = "node")
    public String nodeName;

    @XmlElements({@XmlElement(name = "Sabotage", type = Sabotage.class), @XmlElement(name = "Safety", type = Safety.class), @XmlElement(name = "Key", type = Key.class), @XmlElement(name = "Alarm", type = AlarmSensor.class), @XmlElement(name = "Removable", type = RemovableSensor.class)})
    public List<Sensor> sensors = new ArrayList<>();

    @XmlTransient
    private ProtocolStatus status = ProtocolStatus.Iddle;

    @XmlTransient
    public Long changeStatusTime = 0L;

    @XmlTransient
    public Set<String> activeSensors = new HashSet<>();

    @XmlTransient
    private AlarmDefinition alarmDefinition;

    @XmlTransient
    private AlarmProperties alarmProperties;

    @XmlTransient
    private NotificationsManager notificationsManager;

    @XmlTransient
    private AtomicBoolean underSabotage = new AtomicBoolean(false);

    @XmlTransient
    private AtomicInteger unableToRead = new AtomicInteger(0);


    public void start(AlarmDefinition alarmDefinition, AlarmProperties alarmProperties, NotificationsManager notificationsManager) {
        this.alarmDefinition = alarmDefinition;
        this.alarmProperties = alarmProperties;
        this.notificationsManager = notificationsManager;

        for (Sensor sensor : sensors) {
            sensor.area = this;
            Node node = alarmDefinition.getNode(sensor.node);
            node.addPinListener(sensor);
        }
    }


    public void checkStatus() {
        if (getRemoteNode().getProtocolStatus() != getStatus()) {
            getRemoteNode().setStatus(getStatus());
        }

        if (getStatus() == ProtocolStatus.Activating) {
            checkActivatingLoop();
        } else if (getStatus() == ProtocolStatus.Suspicius) {
            checkSuspiciousLoop();
        }

        try {
            getRemoteNode().checkStatus();
            unableToRead.set(0);
        } catch (Exception e) {
            unableToRead.addAndGet(1);
            checkSabotage();
            log.error("Unable to reach remote node: " + nodeName, e);
        }
    }

    private void checkActivatingLoop() {
        long changeStatusTime = this.changeStatusTime;
        changeStatusTime += alarmProperties.getInteger(AlarmProperty.ActivatingSeconds) * 1000;

        if (changeStatusTime < System.currentTimeMillis()) {
            changeStatus(ProtocolStatus.Activated);
        }
    }

    private void checkSuspiciousLoop() {
        long changeStatusTime = this.changeStatusTime;
        changeStatusTime += alarmProperties.getInteger(AlarmProperty.SuspiciousSeconds) * 1000;

        if (changeStatusTime < System.currentTimeMillis()) {
            String message = "Alarma en " + name + ": ";
            message += activeSensors.toString().replace("[", "").replace("]", "");
            notificationsManager.notify("Alarm", message);
            changeStatus(ProtocolStatus.Alarmed);
        }

    }

    public AlarmDefinition getAlarmDefinition() {
        return alarmDefinition;
    }

    public boolean getUnderSabotage() {
        return underSabotage.get() || ((alarmProperties != null) && (unableToRead.get() >= alarmProperties.getInteger(AlarmProperty.MaxUnreadUntilSabotage)));
    }


    private void checkSabotage() {
        if (unableToRead.get() == alarmProperties.getInteger(AlarmProperty.MaxUnreadUntilSabotage)) {
            setUnderSabotage(true);
        }
    }

    public ProtocolStatus getStatus() {
        return status;
    }

    public void setStatus(ProtocolStatus status) {
        this.status = status;
    }

    public void changeStatus(ProtocolStatus protocolStatus) {
        if (status != protocolStatus) {
            log.info("Area '{}' new status: {}", name, protocolStatus);
            status = protocolStatus;
            changeStatusTime = System.currentTimeMillis();
            alarmDefinition.save();

            getRemoteNode().setStatus(protocolStatus);

            for (Sensor sensor : sensors) {
                sensor.onAreaStatusChange(status);
            }
        }
    }

    public void setSafetySensor(Safety sensor) {
        notificationsManager.notify("Alarm", "Sensor de incendios activado: " + sensor.name);

        if (!getUnderSabotage()) {
            changeStatus(ProtocolStatus.Safety);
        }
    }

    public void setUnderSabotage(boolean status) {
        if ((getAlarmDefinition().isSabotageEnabled()) && (status)) {
            if (!underSabotage.get()) {
                underSabotage.set(true);
                notificationsManager.notify("Alarm", "The alarm is under sabotage: " + name);
            }
        } else {
            underSabotage.set(false);
            unableToRead.set(0);
        }
    }

    public RemoteNode getRemoteNode() {
        return (RemoteNode) alarmDefinition.getNode(nodeName);
    }

    @Override
    public void onStatusChange(ProtocolStatus newStatus) {
        log.info("Area '{}' new status: {}", name, newStatus);
        status = newStatus;
        changeStatusTime = System.currentTimeMillis();
        alarmDefinition.save();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Area{");
        sb.append("name='").append(name).append('\'');
        sb.append(", sensors=").append(sensors);
        sb.append(", status=").append(status);
        sb.append(", changeStatusTime=").append(changeStatusTime);
        sb.append('}');
        return sb.toString();
    }
}
