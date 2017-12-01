package com.domo4pi.gsm;

class SMSRequest {
    public final String number;
    public final String message;

    SMSRequest(String number, String message) {
        this.number = number;
        this.message = message;
    }
}