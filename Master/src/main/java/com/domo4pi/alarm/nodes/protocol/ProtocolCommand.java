package com.domo4pi.alarm.nodes.protocol;

public enum ProtocolCommand {
    GetStatus((byte) 1, 3), SetStatus((byte) 2, 3);

    public static ProtocolCommand getFromCode(byte code) {
        ProtocolCommand result = null;
        for (ProtocolCommand protocolCommand : values()) {
            if (protocolCommand.code == code) {
                result = protocolCommand;
                break;
            }
        }
        return result;
    }

    public final byte code;
    public final int responseLength;

    ProtocolCommand(byte code, int responseLength) {
        this.code = code;
        this.responseLength = responseLength;
    }
}
