package com.domo4pi;

import com.domo4pi.alarm.AlarmModule;
import com.domo4pi.core.CoreModule;
import com.domo4pi.application.Application;
import com.domo4pi.application.ApplicationDefinition;
import com.domo4pi.application.Module;
import com.domo4pi.core.CoreModuleMock;
import com.domo4pi.gsm.GSMModule;
import com.domo4pi.notification.NotificationModule;
import com.domo4pi.saldo.SaldoModule;
import com.domo4pi.telnet.TelnetModule;
import com.domo4pi.utils.ExceptionUtils;
import com.domo4pi.utils.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlarmMain {

    private static final Logger log = LoggerFactory.getLogger(AlarmMain.class);

    private static List<Class<? extends Module>> getApplicationModules() {
        List<Class<? extends Module>> result = new ArrayList<>();

//        result.add(CoreModule.class);
        result.add(CoreModuleMock.class);
        result.add(GSMModule.class);
        result.add(NotificationModule.class);
        result.add(SaldoModule.class);
        result.add(TelnetModule.class);
        result.add(AlarmModule.class);

        return result;
    }

    public static void main(String[] args) {
        try {
            File baseDir = new File(new File("Application.xml").getAbsolutePath()).getParentFile();

            if (args.length > 0) {
                baseDir = new File(args[0]);

                if ((!baseDir.exists()) || (!baseDir.isDirectory())) {
                    throw new IllegalArgumentException("The base folder: '" + args[0] + "' is not a valid directory or not exist");
                }
            }

            log.info("Base dir: {}", baseDir.getAbsolutePath());

            try {
                File file = new File(baseDir, "Application.xml");
                ApplicationDefinition applicationDefinition = XmlUtils.readFromFile(file, ApplicationDefinition.class, Application.DEFAULT_ENCODING);
                Application.setInstance(new Application(toModules(getApplicationModules()), applicationDefinition, baseDir));
            } catch (Exception e) {
                ExceptionUtils.throwRuntimeException(e);
            }

            Runtime.getRuntime().addShutdownHook(new Thread("Shutdown") {
                @Override
                public void run() {
                    Application.getInstance().stop();
                }

            });

            Application.getInstance().start();
        } catch (Throwable e) {
            log.error("Unable to start application", e);
            System.exit(1);
        }
    }

    private static List<Module> toModules(List<Class<? extends Module>> modules) {
        List<Module> result = new ArrayList<>();

        for (Class<? extends Module> moduleClass : modules) {
            try {
                result.add(moduleClass.newInstance());
            } catch (Exception e) {
                ExceptionUtils.throwRuntimeException(e);
            }
        }

        return result;
    }
}
