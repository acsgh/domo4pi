package com.domo4pi.application;

import com.domo4pi.alarm.AlarmDefinition;
import com.domo4pi.utils.ExceptionUtils;
import com.domo4pi.utils.StopWatch;
import com.domo4pi.utils.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class Application {
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static Application instance;

    public static <T> T getInstance(Class<T> objectClass) {
        return getInstance().getObjectInstance(objectClass);
    }

    public synchronized static Application getInstance() {
        return instance;
    }

    public static void setInstance(Application instance) {
        Application.instance = instance;
    }

    private final List<Module> modules;
    private final ApplicationDefinition applicationDefinition;
    private final File baseDir;

    private Injector injector;

    public Application(List<Module> modules, ApplicationDefinition applicationDefinition, File baseDir) {
        this.modules = modules;
        this.applicationDefinition = applicationDefinition;
        this.baseDir = baseDir;
    }

    public <T> T getObjectInstance(Class<T> objectClass) {
        return injector.getInstance(objectClass);
    }

    public void start() {
        StopWatch stopWatch = new StopWatch().start();
        log.info(getName() + ": Starting...");

        injector = new Injector();
        for (Module module : modules) {
            module.configureInjection(injector);
        }

        for (Module module : modules) {
            log.info("Configuring module: {}", module.getClass().getName());
            StopWatch stopWatchModule = new StopWatch().start();
            try {
                injector.injectMembers(module);
                module.configure();
            } catch (Exception e) {
                log.error("Unable to configureInjection the module: " + module.getClass().getName());
                ExceptionUtils.throwRuntimeException(e);
            }
            stopWatchModule.printElapseTime("Configured " + module.getClass().getName(), log, StopWatch.LogLevel.INFO);
        }

        for (Module module : modules) {
            log.info("Starting module: {}", module.getClass().getName());
            StopWatch stopWatchModule = new StopWatch().start();
            try {
                module.start();
            } catch (Exception e) {
                log.error("Unable to start the module: " + module.getClass().getName());
                ExceptionUtils.throwRuntimeException(e);
            }
            stopWatchModule.printElapseTime("Started " + module.getClass().getName(), log, StopWatch.LogLevel.INFO);
        }
        stopWatch.printElapseTime(getName() + ": Started", log, StopWatch.LogLevel.INFO);
    }

    public void stop() {
        StopWatch stopWatch = new StopWatch().start();
        log.info(getName() + ": Stopping...");

        for (int i = (modules.size() - 1); i >= 0; i--) {
            Module module = modules.get(i);
            StopWatch stopWatchModule = new StopWatch().start();

            log.debug("Stopping module: {}", module.getClass().getName());
            try {
                module.stop();
            } catch (Exception e) {
                log.error("Unable to stop the module: " + module.getClass().getName(), e);
                ExceptionUtils.throwRuntimeException(e);
            }
            stopWatchModule.printElapseTime("Stopped module " + module.getClass().getName(), log, StopWatch.LogLevel.INFO);
        }

        stopWatch.printElapseTime(getName() + ": Stopped", log, StopWatch.LogLevel.INFO);
    }

    public String getName() {
        return applicationDefinition.name;
    }

    public Injector getInjector() {
        return injector;
    }

    public Properties getProperties() {
        return applicationDefinition.properties;
    }

    public AlarmDefinition getAlarmDefinition() {
        return applicationDefinition.alarmDefinition;
    }

    public File getBaseDir() {
        return baseDir;
    }
}
