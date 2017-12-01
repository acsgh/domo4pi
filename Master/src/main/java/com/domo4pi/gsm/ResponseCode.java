package com.domo4pi.gsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ResponseCode {
	Ok("OK"), NoDialTone("NO DIALTONE"), Busy("BUSY"), NoCarrier("NO CARRIER"), NoAnswer("NO ANSWER"), Error("ERROR"), Unknown("UNKNOWN");

	private static final Logger log = LoggerFactory.getLogger(ResponseCode.class);

	public static ResponseCode getFromText(String text) {
		ResponseCode code = Unknown;

		for (ResponseCode responseCode : values()) {
			if (responseCode.getText().equalsIgnoreCase(text)) {
				code = responseCode;
				break;
			}
		}

		if (code == Unknown) {
			log.trace("Unable to find the response code: '{}'", text);
		}

		return code;
	}

	private final String text;

	private ResponseCode(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}