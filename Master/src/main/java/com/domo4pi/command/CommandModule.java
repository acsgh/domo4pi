package com.domo4pi.command;

import com.domo4pi.application.Module;
import com.domo4pi.gsm.event.IncomingSMSEvent;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;

public class CommandModule implements Module {

    @Inject
    Dispatcher dispatcher;

    @Inject
    SMSToCommand smsToCommand;

    @Override
    public void configure() {
        dispatcher.addListener(IncomingSMSEvent.class, smsToCommand);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(CommandManager.class).singleton();
        injector.bind(SMSToCommand.class).singleton();
    }
}