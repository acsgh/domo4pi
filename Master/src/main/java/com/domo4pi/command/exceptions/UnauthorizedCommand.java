package com.domo4pi.command.exceptions;

public class UnauthorizedCommand extends RuntimeException {
    public UnauthorizedCommand(String message) {
        super(message);
    }
}
