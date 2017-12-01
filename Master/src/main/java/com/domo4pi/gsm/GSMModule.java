package com.domo4pi.gsm;

import com.domo4pi.application.Module;
import com.domo4pi.gsm.properties.GSMProperties;
import com.domo4pi.gsm.properties.GSMProperty;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;

public class GSMModule implements Module {

    @Inject
    GSMManager gsmManager;

    @Inject
    GSMProperties gsmProperties;

    @Inject
    SMSSenderTimer smsSenderTimer;


    @Override
    public void configure() {
    }

    @Override
    public void start() {
        gsmManager.start();

        if (gsmProperties.getBoolean(GSMProperty.SMSEnabled)) {
            smsSenderTimer.start();
        }
    }

    @Override
    public void stop() {
        if (gsmProperties.getBoolean(GSMProperty.SMSEnabled)) {
            smsSenderTimer.stop();
        }

        gsmManager.stop();
    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(GSMManager.class).singleton();
        injector.bind(SMSSenderTimer.class).singleton();
    }
}
