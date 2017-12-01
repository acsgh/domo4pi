package com.domo4pi.saldo;

import com.domo4pi.application.Module;
import com.domo4pi.gsm.event.IncomingServiceMessageEvent;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;

public class SaldoModule implements Module {

    @Inject
    Dispatcher dispatcher;

    @Inject
    SaldoChecker saldoChecker;

    @Override
    public void configure() {
        dispatcher.addListener(IncomingServiceMessageEvent.class, saldoChecker);
    }

    @Override
    public void start() {
        saldoChecker.start();
    }

    @Override
    public void stop() {
        saldoChecker.stop();
    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(SaldoChecker.class).singleton();
    }
}