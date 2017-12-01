package com.domo4pi.gsm.event;

import com.domo4pi.utils.dispatcher.DispatcherEvent;

public class IncomingServiceMessageEvent implements DispatcherEvent {
    private final String message;

    public IncomingServiceMessageEvent(String callerNumber) {
        this.message = callerNumber;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IncomingServiceMessageEvent [message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }
}
