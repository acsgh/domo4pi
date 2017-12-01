package com.domo4pi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractManager {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Unable to sleep", e);
        }
    }
}
