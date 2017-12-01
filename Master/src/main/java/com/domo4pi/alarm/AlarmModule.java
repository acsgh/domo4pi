package com.domo4pi.alarm;

import com.domo4pi.application.Application;
import com.domo4pi.application.Module;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmModule implements Module {

    @Inject
    AlarmManager alarmManager;

    @Override
    public void configure() {
    }

    @Override
    public void start() {
        alarmManager.start();
    }

    @Override
    public void stop() {
        alarmManager.stop();
    }

    @Override
    public void configureInjection(Injector binder) {
        binder.bind(AlarmManager.class).singleton();
        binder.bind(AlarmDefinition.class).singleton(Application.getInstance().getAlarmDefinition());
    }
}
