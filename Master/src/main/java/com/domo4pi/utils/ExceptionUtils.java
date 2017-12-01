package com.domo4pi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public final class ExceptionUtils {

    private final static Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    private ExceptionUtils() {

    }

    public static void throwRuntimeException(Throwable e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new RuntimeException(e);
        }
    }

    public static String getExceptionStackTrace(Exception e, String charset) {
        String stacktrace = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            PrintStream writer = new PrintStream(output, true, charset);
            e.printStackTrace(writer);
            stacktrace = new String(output.toByteArray(), charset);
        } catch (UnsupportedEncodingException e1) {
            log.error("Unable to get the stacktrace", e1);
        }

        return stacktrace;
    }
}
