package com.domo4pi.gsm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GSMCommandResponse {
	private final String command;
	private final List<String> responseLines = new ArrayList<String>();
	private ResponseCode responseCode;

	public GSMCommandResponse(String command) {
		this.command = command;
	}

	String getCommand() {
		return command;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public List<String> getResponseLines() {
		return Collections.unmodifiableList(responseLines);
	}

	void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	void appendLine(String line) {
		responseLines.add(line);
	}

	@Override
	public String toString() {
		return "CommandResponse [command=" + command + ", responseLines=" + responseLines + ", responseCode=" + responseCode + "]";
	}
}