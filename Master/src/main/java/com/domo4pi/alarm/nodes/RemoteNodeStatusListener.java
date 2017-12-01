package com.domo4pi.alarm.nodes;

import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;

public interface RemoteNodeStatusListener {
    public void onStatusChange(ProtocolStatus newStatus);
}
