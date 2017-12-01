package com.domo4pi.gsm;

import com.domo4pi.utils.Timer;
import com.domo4pi.utils.inject.Inject;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class SMSSenderTimer extends Timer {

    @Inject
    private GSMManager gsmManager;

    public SMSSenderTimer() {
        super("GSM.SMSSender");
    }

    private final LinkedList<SMSRequest> pendingSMS = new LinkedList<>();

    @Override
    public int getIntervalValue() {
        return 2;
    }

    @Override
    public TimeUnit getIntervalUnits() {
        return TimeUnit.SECONDS;
    }

    void addSMS(SMSRequest smsRequest) {
        pendingSMS.add(smsRequest);
    }

    @Override
    public void tick() {
        if (gsmManager.isGSMBoardReady()) {
            SMSRequest request;

            while ((request = pendingSMS.poll()) != null) {
                gsmManager.sendSMS(request);
            }
        }
    }
}