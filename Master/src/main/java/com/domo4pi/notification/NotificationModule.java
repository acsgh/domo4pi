package com.domo4pi.notification;

import com.domo4pi.application.Application;
import com.domo4pi.application.Module;
import com.domo4pi.utils.inject.Injector;
import com.domo4pi.utils.inject.providers.SingletonProvider;
import com.domo4pi.utils.xml.XmlUtils;

public class NotificationModule implements Module {

    @Override
    public void configure() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void configureInjection(Injector injector) {
        injector.bind(NotificationRegistry.class).provided(new SingletonProvider<NotificationRegistry>(NotificationRegistry.class) {
            @Override
            public NotificationRegistry getInstance(Injector injector) {
                NotificationRegistry notificationRegistry = new NotificationRegistry();
                try {
                    notificationRegistry = XmlUtils.readFromFile(NotificationRegistry.configurationFile, NotificationRegistry.class, Application.DEFAULT_ENCODING);
                } catch (Exception e) {
                    log.info("Unable to read notification registry: {}", e.getMessage());
                }
                return notificationRegistry;
            }
        });
    }
}