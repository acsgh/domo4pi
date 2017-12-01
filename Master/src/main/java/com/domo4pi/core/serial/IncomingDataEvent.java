package com.domo4pi.core.serial;

import com.domo4pi.utils.dispatcher.DispatcherEvent;

public class IncomingDataEvent implements DispatcherEvent{
	private final String data;

	public IncomingDataEvent(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
}
