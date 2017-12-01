package com.domo4pi.command;

import com.domo4pi.gsm.GSMManager;
import com.domo4pi.gsm.event.IncomingSMSEvent;
import com.domo4pi.utils.dispatcher.DispatcherEvent;
import com.domo4pi.utils.dispatcher.DispatcherListener;
import com.domo4pi.utils.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMSToCommand implements DispatcherListener {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    CommandManager commandManager;

    @Inject
    GSMManager gsmManager;

    @Override
    public void processEvent(DispatcherEvent rawEvent) {
        if (rawEvent instanceof IncomingSMSEvent) {
            IncomingSMSEvent event = (IncomingSMSEvent) rawEvent;

            String number = event.getSms().getNumber();
            String message = event.getSms().getMessage();
            String response = commandManager.executeCommand(number, message);
            gsmManager.sendSMS(number, response);
        } else {
            log.warn("Not supported event received: {}", rawEvent.getClass().getName());
        }
    }
}