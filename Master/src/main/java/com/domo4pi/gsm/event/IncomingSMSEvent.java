package com.domo4pi.gsm.event;


import com.domo4pi.gsm.SMS;
import com.domo4pi.utils.dispatcher.DispatcherEvent;

public class IncomingSMSEvent implements DispatcherEvent{
    private final SMS sms;

    public IncomingSMSEvent(SMS sms) {
        this.sms = sms;
    }

    public SMS getSms() {
        return sms;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IncomingSMSEvent [sms=");
        builder.append(sms);
        builder.append("]");
        return builder.toString();
    }
}
