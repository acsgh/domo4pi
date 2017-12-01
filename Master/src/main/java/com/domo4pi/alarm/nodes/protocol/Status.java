package com.domo4pi.alarm.nodes.protocol;

public class Status {
    public ProtocolStatus currentStatus = ProtocolStatus.Iddle;
    public byte[] sensors = {0, 0};
}
