package com.domo4pi.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Timer implements Runnable {

    private static final int CYCLE_TIME_MILLIS = 100;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private AtomicBoolean mustStop = new AtomicBoolean(false);

    protected final Thread thread;

    public Timer(String name) {
        this.thread = new Thread(this, name);
    }

    public String getName() {
        return thread.getName();
    }

    public abstract int getIntervalValue();

    public abstract TimeUnit getIntervalUnits();

    public abstract void tick();

    @Override
    public final void run() {
        log.debug("Start timer: {}", getName());
        while (!mustStop.get()) {
            log.debug("Tick timer: {}", getName());
            tick();
            sleep();
        }
        log.debug("Stop timer: {}", getName());
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        mustStop.set(true);
    }

    private void sleep() {
        if (!mustStop.get()) {
            try {
                long sleepTime = TimeUnit.MILLISECONDS.convert(getIntervalValue(), getIntervalUnits());
                long iterations = sleepTime / CYCLE_TIME_MILLIS; // 100ms
                long index = 0;

                while (index++ < iterations) {
                    Thread.sleep(CYCLE_TIME_MILLIS);
                }
            } catch (InterruptedException e) {
                log.warn("Unable to sleep", e);
            }
        }
    }
}
