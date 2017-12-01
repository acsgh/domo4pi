package com.domo4pi.telnet;

import com.domo4pi.application.Module;
import com.domo4pi.utils.ExceptionUtils;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;

public class TelnetModule implements Module {

    @Inject
    TelnetServer telnetServer;

    @Override
    public void configure() {
    }

    @Override
    public void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    telnetServer.start();
                } catch (Exception e) {
                    ExceptionUtils.throwRuntimeException(e);
                }
            }
        }.start();
    }

    @Override
    public void stop() {
        telnetServer.stop();
    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(TelnetServer.class).singleton();
    }
}