package com.domo4pi.core.mock.serial;

import com.domo4pi.application.Application;
import com.domo4pi.core.serial.SerialManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SerialManagerMockController {

    @FXML
    private TextArea outputText;
    @FXML
    private TextField inputText;

    private final SerialManagerMock serialManagerMock;

    public SerialManagerMockController() {
        serialManagerMock = (SerialManagerMock) Application.getInstance(SerialManager.class);
        serialManagerMock.setSerialController(this);

    }

    public void initialize() {
        inputText.requestFocus();
        updateState();
    }

    @FXML
    private void sendCommand(ActionEvent event) {
        serialManagerMock.incomingData(inputText.getText());
        inputText.setText("");
        inputText.requestFocus();
    }

    public void updateState() {
        outputText.setText(serialManagerMock.getText());
    }
}
