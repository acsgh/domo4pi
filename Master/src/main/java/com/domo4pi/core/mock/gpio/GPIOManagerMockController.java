package com.domo4pi.core.mock.gpio;

import com.domo4pi.application.Application;
import com.domo4pi.core.gpio.GPIOManager;
import com.pi4j.io.gpio.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.util.HashMap;
import java.util.Map;

public class GPIOManagerMockController {


    @FXML
    private CheckBox pin0;
    @FXML
    private CheckBox pin1;
    @FXML
    private CheckBox pin2;
    @FXML
    private CheckBox pin3;
    @FXML
    private CheckBox pin4;
    @FXML
    private CheckBox pin5;
    @FXML
    private CheckBox pin6;
    @FXML
    private CheckBox pin7;
    @FXML
    private CheckBox pin8;
    @FXML
    private CheckBox pin9;
    @FXML
    private CheckBox pin10;
    @FXML
    private CheckBox pin11;
    @FXML
    private CheckBox pin12;
    @FXML
    private CheckBox pin13;

    private final GPIOManagerMock gpioManagerMock;
    private final Map<Pin, CheckBox> checkBoxPin = new HashMap<>();

    public GPIOManagerMockController() {
        gpioManagerMock = (GPIOManagerMock) Application.getInstance(GPIOManager.class);
        gpioManagerMock.setGuiController(this);

    }

    public void initialize() {
        initCheckboxes();

        for (Pin pinKey : checkBoxPin.keySet()) {
            CheckBox checkBox = checkBoxPin.get(pinKey);
            checkBox.setSelected(false);
            checkBox.setVisible(false);
            checkBox.setStyle("-fx-opacity: 1");

            checkBox.selectedProperty().addListener(new CheckBoxChangeListener(pinKey));
        }

        updateState();
    }

    private void initCheckboxes() {
        checkBoxPin.put(RaspiPin.GPIO_00, pin0);
        checkBoxPin.put(RaspiPin.GPIO_01, pin1);
        checkBoxPin.put(RaspiPin.GPIO_02, pin2);
        checkBoxPin.put(RaspiPin.GPIO_03, pin3);
        checkBoxPin.put(RaspiPin.GPIO_04, pin4);
        checkBoxPin.put(RaspiPin.GPIO_05, pin5);
        checkBoxPin.put(RaspiPin.GPIO_06, pin6);
        checkBoxPin.put(RaspiPin.GPIO_07, pin7);
        checkBoxPin.put(RaspiPin.GPIO_08, pin8);
        checkBoxPin.put(RaspiPin.GPIO_09, pin9);
        checkBoxPin.put(RaspiPin.GPIO_10, pin10);
        checkBoxPin.put(RaspiPin.GPIO_11, pin11);
        checkBoxPin.put(RaspiPin.GPIO_12, pin12);
        checkBoxPin.put(RaspiPin.GPIO_13, pin13);
    }

    public void updateState() {
        Map<Pin, GpioPinDigital> pins = gpioManagerMock.getPins();

        for (Pin pinKey : checkBoxPin.keySet()) {
            GpioPinDigital pin = pins.get(pinKey);
            CheckBox checkBox = checkBoxPin.get(pinKey);

            if (pin == null) {
                checkBox.setSelected(false);
                checkBox.setVisible(false);
            } else {
                checkBox.setVisible(true);
                checkBox.setSelected(pin.isHigh());
                boolean output = pin instanceof GpioPinDigitalOutput;
                checkBox.setText(pinKey.getAddress() + " (" + (output ? "Out" : "In") + ") " + " - " + pin.getName());
                checkBox.setDisable(output);
            }
        }
    }

    private class CheckBoxChangeListener implements ChangeListener<Boolean> {
        private final Pin pinKey;

        public CheckBoxChangeListener(Pin pinKey) {
            this.pinKey = pinKey;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (oldValue != newValue) {
                GpioPinDigital pin = gpioManagerMock.getPins().get(pinKey);

                if ((pin != null) && (pin instanceof InputPin)) {
                    InputPin inputPin = (InputPin) pin;

                    inputPin.setState(newValue ? PinState.HIGH : PinState.LOW);
                }
            }
        }
    }
}
