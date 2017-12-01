package com.domo4pi.core.serial;

public interface SerialManager {

    public void flush() throws IllegalStateException;

    public char read() throws IllegalStateException;

    public void write(char data) throws IllegalStateException;

    public void write(char[] data) throws IllegalStateException;

    public void write(byte data) throws IllegalStateException;

    public void write(byte[] data) throws IllegalStateException;

    public void write(String data) throws IllegalStateException;

    public void writeln(String data) throws IllegalStateException;

    public void write(String data, String... args) throws IllegalStateException;

    public void writeln(String data, String... args) throws IllegalStateException;

    public int availableBytes() throws IllegalStateException;

    public void start();

    public void stop();

}
