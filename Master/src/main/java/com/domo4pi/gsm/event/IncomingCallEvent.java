package com.domo4pi.gsm.event;

public class IncomingCallEvent {
    private final String callerNumber;

    public IncomingCallEvent(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IncomingCallEvent [callerNumber=");
        builder.append(callerNumber);
        builder.append("]");
        return builder.toString();
    }
}
