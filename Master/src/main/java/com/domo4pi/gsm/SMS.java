package com.domo4pi.gsm;

import java.util.Date;

public class SMS {
	private final String number;
	private final String name;
	private final boolean readed;
	private final Date date;
	private final String message;

	public SMS(String number, String name, boolean readed, Date date, String message) {
		this.number = number;
		this.name = name;
		this.readed = readed;
		this.date = date;
		this.message = message;
	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public boolean isReaded() {
		return readed;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "SMS [number=" + number + ", name=" + name + ", readed=" + readed + ", date=" + date + ", message=" + message + "]";
	}

}
