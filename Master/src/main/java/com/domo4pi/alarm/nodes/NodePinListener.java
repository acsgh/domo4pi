package com.domo4pi.alarm.nodes;

public interface NodePinListener {
    public String getName();

    public int getPinNumber();

    public void onStatusChange(boolean newState);
}
