package com.domo4pi.command.exceptions;

public class InvalidCommand extends RuntimeException {
    public InvalidCommand(String message) {
        super(message);
    }
}
