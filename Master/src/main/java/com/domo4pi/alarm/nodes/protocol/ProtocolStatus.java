package com.domo4pi.alarm.nodes.protocol;

public enum ProtocolStatus {
    Iddle((byte) 1), Activating((byte) 2), Activated((byte) 3), Suspicius((byte) 4), Alarmed((byte) 5), Safety((byte) 6);

    public static ProtocolStatus getFromCode(byte code) {
        ProtocolStatus result = null;
        for (ProtocolStatus protocolStatus : values()) {
            if (protocolStatus.code == code) {
                result = protocolStatus;
                break;
            }
        }
        return result;
    }

    public final byte code;

    ProtocolStatus(Byte code) {
        this.code = code;
    }
}
