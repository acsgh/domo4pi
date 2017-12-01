package com.domo4pi.core.mock;

import com.domo4pi.alarm.nodes.Node;
import com.domo4pi.alarm.nodes.RemoteNode;
import com.domo4pi.core.mock.node.RemoteNodeMockController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockApp extends Application implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(MockApp.class);

    @Override
    public void start(Stage gpioStage) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Parent gpioRoot = FXMLLoader.load(contextClassLoader.getResource("GPIO.fxml"));
        gpioStage.setTitle("GPIO");
        gpioStage.setScene(new Scene(gpioRoot, 600, 400));
        gpioStage.setResizable(false);
        gpioStage.show();

        Stage serialStage = new Stage();
        Parent serialRoot = FXMLLoader.load(contextClassLoader.getResource("Serial.fxml"));
        serialStage.setTitle("Serial");
        serialStage.setScene(new Scene(serialRoot, 500, 200));
        serialStage.setResizable(false);
        serialStage.show();

        for (Node node : com.domo4pi.application.Application.getInstance().getAlarmDefinition().nodes) {
            if (node instanceof RemoteNode) {
                Stage remoteStage = new Stage();
                FXMLLoader loader = new FXMLLoader(contextClassLoader.getResource("Remote.fxml"));
                Parent remoteRoot = (Parent)loader.load();
                remoteStage.setTitle(node.name);
                remoteStage.setScene(new Scene(remoteRoot, 250, 300));
                remoteStage.setResizable(false);
                remoteStage.show();

                RemoteNodeMockController controller = loader.getController();
                controller.setRemoteNode(node.name);

            }
        }
    }


    @Override
    public void run() {
        launch();
    }

    private static final MockApp instance = new MockApp();
    private static final Thread thread = new Thread(instance, "GUI");

    public static MockApp getInstance() {
        return instance;
    }

    public static void startGUI() {
        thread.start();
    }

    public static void stopGUI() {
        try {
            getInstance().stop();
        } catch (Exception e) {
            log.error("Unable to stop GUI", e);
        }
    }
}
