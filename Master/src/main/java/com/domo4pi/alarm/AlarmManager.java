package com.domo4pi.alarm;

import com.domo4pi.alarm.area.Area;
import com.domo4pi.alarm.properties.AlarmProperties;
import com.domo4pi.alarm.properties.AlarmProperty;
import com.domo4pi.core.gpio.GPIOManager;
import com.domo4pi.notification.NotificationsManager;
import com.domo4pi.utils.Timer;
import com.domo4pi.utils.inject.Inject;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlarmManager extends Timer {

    private final AlarmProperties alarmProperties;
    private final GPIOManager gpioManager;
    private final NotificationsManager notificationsManager;
    private final AlarmDefinition alarmDefinition;

    private GpioPinDigitalOutput sirenPin;
    private GpioPinDigitalOutput sabotagePin;

    private AtomicLong sabotageReseted = new AtomicLong();

    @Inject
    public AlarmManager(AlarmProperties alarmProperties, GPIOManager gpioManager, NotificationsManager notificationsManager, AlarmDefinition alarmDefinition) {
        super("Alarm.Manager");
        this.alarmProperties = alarmProperties;
        this.gpioManager = gpioManager;
        this.notificationsManager = notificationsManager;
        this.alarmDefinition = alarmDefinition;
    }

    @Override
    public int getIntervalValue() {
        return alarmProperties.getInteger(AlarmProperty.SleepTimeMillis);
    }

    @Override
    public TimeUnit getIntervalUnits() {
        return TimeUnit.MILLISECONDS;
    }

    @Override
    public void start() {
        sirenPin = gpioManager.provisionDigitalOutputPin("Siren", alarmProperties.getInteger(AlarmProperty.SirenPin));
        sabotagePin = gpioManager.provisionDigitalOutputPin("Slaves Power", alarmProperties.getInteger(AlarmProperty.SabotagePin));


        alarmDefinition.load();
        updatePins();

        try {
            Thread.sleep(alarmProperties.getLong(AlarmProperty.RemoteTimeInitMillis));
        } catch (InterruptedException e) {
            log.error("Unable to start alarm manager", e);
        }

        for (Area area : alarmDefinition.areas) {
            area.start(alarmDefinition, alarmProperties, notificationsManager);
        }

        resetSabotage();

        super.start();
    }

    private void updatePins() {
        switch (getStatus()) {
            case Iddle:
            case Activated:
                setSirenPinState(PinState.LOW);
                setSabotagePinState(PinState.HIGH);
                break;
            case Alarmed:
            case Safety:
                setSirenPinState(PinState.HIGH);
                setSabotagePinState(PinState.HIGH);
                break;
            case Sabotage:
                setSirenPinState(PinState.HIGH);
                setSabotagePinState(PinState.LOW);
                break;
        }
    }

    private void setSirenPinState(PinState newState) {
        sirenPin.setState(isSirenEnabled() ? newState : PinState.LOW);
    }

    private void setSabotagePinState(PinState newState) {
        sabotagePin.setState(isSabotageEnabled() ? newState : PinState.HIGH);
    }

    public AlarmStatus getStatus() {
        AlarmStatus status = AlarmStatus.Iddle;

        for (Area area : alarmDefinition.areas) {
            AlarmStatus areaAlarmstatus = AlarmStatus.Iddle;

            if (area.getUnderSabotage()) {
                areaAlarmstatus = AlarmStatus.Sabotage;
            } else {
                switch (area.getStatus()) {
                    case Alarmed:
                        areaAlarmstatus = AlarmStatus.Alarmed;
                        break;
                    case Activated:
                        areaAlarmstatus = AlarmStatus.Activated;
                        break;
                    case Safety:
                        areaAlarmstatus = AlarmStatus.Safety;
                        break;
                }

            }

            if (areaAlarmstatus.ordinal() > status.ordinal()) {
                status = areaAlarmstatus;
            }
        }

        return status;
    }

    @Override
    public void stop() {
        super.stop();

        sirenPin.low();
        sabotagePin.low();
    }

    public void tick() {
        long remoteNodesWakeUp = sabotageReseted.get() + alarmProperties.getLong(AlarmProperty.RemoteTimeInitMillis);

        if ((getStatus() != AlarmStatus.Sabotage) && (System.currentTimeMillis() > remoteNodesWakeUp)) {
            for (Area area : alarmDefinition.areas) {
                area.checkStatus();
            }
        }

        updatePins();
    }

    public void setEnableSiren(boolean value) {
        alarmDefinition.getSirenEnabled().set(value);
        updatePins();
    }


    private boolean isSirenEnabled() {
        return alarmDefinition.getSirenEnabled().get();
    }

    public void setSabotageEnabled(boolean value) {
        alarmDefinition.getSabotageEnabled().set(value);
        updatePins();
    }

    public boolean isSabotageEnabled() {
        return alarmDefinition.getSabotageEnabled().get();
    }

    public void resetSabotage() {
        setSabotageEnabled(true);

        for (Area area : alarmDefinition.areas) {
            area.setUnderSabotage(false);
        }
        sabotageReseted.set(System.currentTimeMillis());
        updatePins();
    }
}