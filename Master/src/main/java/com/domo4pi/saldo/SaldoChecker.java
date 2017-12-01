package com.domo4pi.saldo;

import com.domo4pi.gsm.GSMManager;
import com.domo4pi.gsm.event.IncomingServiceMessageEvent;
import com.domo4pi.notification.NotificationsManager;
import com.domo4pi.saldo.properties.SaldoProperties;
import com.domo4pi.saldo.properties.SaldoProperty;
import com.domo4pi.utils.Timer;
import com.domo4pi.utils.dispatcher.DispatcherEvent;
import com.domo4pi.utils.dispatcher.DispatcherListener;
import com.domo4pi.utils.inject.Inject;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SaldoChecker extends Timer implements DispatcherListener {

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;

    private final GSMManager gsmManager;

    private final SaldoProperties saldoProperties;

    private final NotificationsManager notificationsManager;

    @Inject
    public SaldoChecker(GSMManager gsmManager, SaldoProperties saldoProperties, NotificationsManager notificationsManager) {
        super("Saldo.Checker");

        this.gsmManager = gsmManager;
        this.saldoProperties = saldoProperties;
        this.notificationsManager = notificationsManager;
    }

    private Double saldo;
    private long lastSent = 0; // Always send the first time

    public Double getSaldo() {
        return saldo;
    }

    @Override
    public int getIntervalValue() {
        return 30;
    }

    @Override
    public TimeUnit getIntervalUnits() {
        return TimeUnit.MINUTES;
    }

    @Override
    public void processEvent(DispatcherEvent rawEvent) {
        if (rawEvent instanceof IncomingServiceMessageEvent) {
            IncomingServiceMessageEvent event = (IncomingServiceMessageEvent) rawEvent;

            Scanner scanner = new Scanner(event.getMessage());

            boolean checkNext = false;
            double value = 0.0;
            while (scanner.hasNext()) {
                if (scanner.hasNextDouble()) {
                    value = scanner.nextDouble();
                    checkNext = true;
                } else {
                    String word = scanner.next();
                    if ((checkNext) && (word.contains("euro"))) {
                        saldo = value;
                        gsmManager.executeCommand("AT+CMGD=1,4");
                        log.info("Current saldo is {} euros", value);
                        checkSaldo();
                    }
                }
            }
        } else {
            log.warn("Not supported event received: {}", rawEvent.getClass().getName());
        }
    }

    @Override
    public void tick() {
        if (gsmManager.isGSMBoardReady()) {
            log.info("Checking current saldo");
            gsmManager.executeCommand("AT+CUSD=1,\"*111#\"");
        }
    }

    private void checkSaldo() {
        int threshold = saldoProperties.getInteger(SaldoProperty.SaldoAlarmThreshold);

        if (saldo <= threshold) {
            long timeSpend = System.currentTimeMillis() - lastSent;
            if (timeSpend > ONE_DAY) {
                notificationsManager.notify("Saldo", "El saldo actual es de " + saldo + " euros, por favor recarga");
                lastSent = System.currentTimeMillis();
            }
        } else {
            lastSent = 0;
        }
    }
}