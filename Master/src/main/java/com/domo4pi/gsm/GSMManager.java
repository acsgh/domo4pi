package com.domo4pi.gsm;

import com.domo4pi.core.AbstractManager;
import com.domo4pi.application.Application;
import com.domo4pi.core.gpio.GPIOManager;
import com.domo4pi.core.serial.IncomingDataEvent;
import com.domo4pi.core.serial.SerialManager;
import com.domo4pi.gsm.event.IncomingSMSEvent;
import com.domo4pi.gsm.event.IncomingServiceMessageEvent;
import com.domo4pi.gsm.properties.GSMProperties;
import com.domo4pi.gsm.properties.GSMProperty;
import com.domo4pi.utils.CheckUtils;
import com.domo4pi.utils.dispatcher.Dispatcher;
import com.domo4pi.utils.dispatcher.DispatcherEvent;
import com.domo4pi.utils.dispatcher.DispatcherListener;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.selector.Selector;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class GSMManager extends AbstractManager implements DispatcherListener {

    private static final String LINE_SEPARATOR = "\r\n";

    public ReentrantLock checkStatusLock = new ReentrantLock();
    public ReentrantLock executeCommandLock = new ReentrantLock();

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean ready = new AtomicBoolean(false);

    private StringBuilder lineBuffer = new StringBuilder();
    private GSMCommandResponse commandResponse = null;

    private GpioPinDigitalOutput gsmPowerPin;
    private GpioPinDigitalInput gsmPowerCheckPin;

    private final Dispatcher dispatcher;
    private final GSMProperties gsmProperties;
    private final GPIOManager gpioManager;
    private final SerialManager serialManager;

    @Inject
    public GSMManager(Dispatcher dispatcher, GSMProperties gsmProperties, GPIOManager gpioManager, SerialManager serialManager) {
        this.dispatcher = dispatcher;
        this.gsmProperties = gsmProperties;
        this.gpioManager = gpioManager;
        this.serialManager = serialManager;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                gsmPowerPin = gpioManager.provisionDigitalOutputPin("GSM Toggle Power", gsmProperties.getGPIOPin(GSMProperty.PowerPin));
                gsmPowerCheckPin = gpioManager.provisionDigitalInputPin("GSM Power", gsmProperties.getGPIOPin(GSMProperty.PowerCheckPin));

                restartGSMBoard();

                dispatcher.addListener(IncomingDataEvent.class, GSMManager.this);

                executeCommand("AT");
                started.set(true);
            }
        }.start();
    }

    public void stop() {
        if (gsmPowerCheckPin.isHigh()) {
            toggleGSMStatus();
        }
    }

    @Override
    public void processEvent(DispatcherEvent rawEvent) {
        if (rawEvent instanceof IncomingDataEvent) {
            IncomingDataEvent event = (IncomingDataEvent) rawEvent;
            incomingData(event.getData());
        } else {
            log.warn("Not supported event received: {}", rawEvent.getClass().getName());
        }
    }

    public boolean isGSMBoardReady() {
        if (started.get()) {
            if (!ready.get()) {
                checkStatusLock.lock();
                try {
                    if (!ready.get()) {
                        GSMCommandResponse result = executeCommand("AT+COPS?");
                        ready.set(!result.getResponseLines().get(0).equals("+COPS: 0"));
                    }
                } finally {
                    checkStatusLock.unlock();
                }
            }
        }
        return ready.get();
    }


    public void sendSMS(String number, String message) {
        CheckUtils.checkString("number", number);
        CheckUtils.checkString("text", message);

        log.info("Request send sms to: '{}' --> '{}'", number, message);

        Application.getInstance(SMSSenderTimer.class).addSMS(new SMSRequest(number, message));
    }

    public GSMCommandResponse makeCall(String number) {
        return executeCommand("ATD" + number + ";");
    }

    public GSMCommandResponse acceptCall() {
        return executeCommand("ATA");
    }

    public GSMCommandResponse rejectCall() {
        return executeCommand("ATH");
    }

    public GSMCommandResponse executeCommand(String command) {
        executeCommandLock.lock();
        try {
            return executeCommandInner(command);
        } finally {
            executeCommandLock.unlock();
        }
    }

    private void restartGSMBoard() {
        if (gsmPowerCheckPin.isHigh()) {
            toggleGSMStatus();
        }
        toggleGSMStatus();
    }

    private void toggleGSMStatus() {
        gsmPowerPin.low();
        sleep(1000);
        gsmPowerPin.high();
        sleep(2000);
        gsmPowerPin.low();
        sleep(3000);
    }

    private void incomingData(String data) {
//        log.debug("Incoming serial data: '{}'", data);
        lineBuffer.append(data);

        while (lineBuffer.indexOf(LINE_SEPARATOR) > -1) {
            String line = lineBuffer.substring(0, lineBuffer.indexOf(LINE_SEPARATOR));

            if ((line.length() > 0) && (!smsSendLines(line))) {
                if (commandResponse != null) {
                    ResponseCode responseCode = ResponseCode.getFromText(line);

                    if (responseCode != ResponseCode.Unknown) {
                        commandResponse.setResponseCode(responseCode);
                    } else {
                        commandResponse.appendLine(line);
                    }
                } else {
                    processUnknownLine(line);
                }
            }

            lineBuffer = lineBuffer.replace(0, line.length() + LINE_SEPARATOR.length(), "");
        }
    }

    private boolean smsSendLines(String line) {
        line = line.trim();
        return (line.equals(">")) || (line.startsWith("+CMGS:"));
    }

    private void processUnknownLine(final String line) {
        log.info("New unknown line received from GSM Board: '{}'", line);

        if (line.startsWith("+CMTI: \"SM\",")) {
            new Thread("SMS Reader") {
                public void run() {
                    int index = Integer.parseInt(line.replace("+CMTI: \"SM\",", "").trim());
                    log.debug("New SMS with index: {}", index);

                    SMS sms = readSMS(index);

                    log.info("SMS({}): {}", index, sms);

                    dispatcher.dispatchEvent(new IncomingSMSEvent(sms));

                    executeCommand("AT+CMGD=" + index);
                }
            }.start();
        } else if (line.startsWith("+CUSD: ")) {
            new Thread("Service Message Reader") {
                public void run() {
                    int start = line.indexOf("\"");
                    int end = line.lastIndexOf("\"");

                    if ((start > -1) && (end > -1)) {
                        String message = line.substring(start + 1, end);
                        log.debug("New service message: {}", message);
                        dispatcher.dispatchEvent(new IncomingServiceMessageEvent(message));
                    }
                }
            }.start();
        }
    }

    private SMS readSMS(int index) {
        GSMCommandResponse response = executeCommand("AT+CMGR=" + index);

        SMS sms = null;

        if (response.getResponseCode() == ResponseCode.Ok) {
            List<String> lines = response.getResponseLines();

            if (lines.size() == 2) {
                String header = lines.get(0);

                Selector selector = new Selector(new StringBuilder(header));

                selector.selectText("\"", "\"");
                boolean readed = "READ".equalsIgnoreCase(selector.extractTextToEnd(" "));
                selector.deselectText();

                String number = selector.extractText("\"", "\"").replace("+34", "");

                String name = selector.extractText("\"", "\"");

                String dateString = selector.extractText("\"", "\"");

                SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd,HH:mm:ssX");

                Date date = null;

                try {
                    date = formatter.parse(dateString);
                } catch (ParseException e) {
                    log.debug("Unable to parse the date: " + dateString, e);
                }

                String message = lines.get(1);

                sms = new SMS(number, name, readed, date, message);
            }

        }

        return sms;
    }

    private GSMCommandResponse executeCommandInner(String command) {
        if (commandResponse != null) {
            throw new IllegalStateException("Inside a command...");
        }
        log.debug("Executing command: '{}'", command);
        try {

            commandResponse = new GSMCommandResponse(command);

            serialManager.writeln(command);

            while (commandResponse.getResponseCode() == null) {
                sleep(200);
            }

            log.debug("Executing command done: {}", commandResponse);
            return commandResponse;
        } finally {
            commandResponse = null;
        }
    }

    void sendSMS(SMSRequest request) {
        log.info("Sending sms to: '{}' --> '{}'", request.number, request.message);
        executeCommandLock.lock();
        try {
            GSMCommandResponse response = executeCommandInner("AT+CMGF=1");
            sleep(200);

            if (response.getResponseCode() == ResponseCode.Ok) {
                byte[] bytes = request.message.getBytes("US-ASCII");
                List<byte[]> parts = splitSMS(bytes);
                log.trace("Send SMS in {} parts", parts.size());

                int partIndex = 0;
                for (byte[] part : parts) {
                    log.trace("Partial bytes: {}", part.length);
                    try {
                        commandResponse = new GSMCommandResponse("SMS Part: " + partIndex);
                        serialManager.write("AT+CMGS=\"" + request.number + "\"\r");
                        sleep(200);
                        serialManager.write(part);
                        sleep(200);
                        serialManager.write((byte) 0x1A);

                        while (commandResponse.getResponseCode() == null) {
                            sleep(200);
                        }

                        log.trace("Sending SMS part {}: {}", partIndex, commandResponse);
                    } finally {
                        commandResponse = null;
                    }
                    partIndex++;
                }
            }

        } catch (Exception e) {
            log.error("Unable to send sms", e);
        } finally {
            executeCommandLock.unlock();
        }
    }

    private List<byte[]> splitSMS(byte[] message) {
        List<byte[]> parts = new ArrayList<byte[]>();

        boolean multipart = false;//message.length > 140;
        int maxPartLength = multipart ? 135 : 140;

        int totalParts = 1;

        if (multipart) {
            totalParts = message.length / maxPartLength;

            if (message.length % maxPartLength != 0) {
                totalParts++;
            }
        }

        int i = 0;
        while (i < message.length) {
            int left = message.length - i;
            int partLength = Math.min(left, maxPartLength);

            byte[] part;
            if (multipart) {
                part = new byte[partLength + 5];

                part[0] = (byte) 0x00;
                part[1] = (byte) 0x03;
                part[2] = (byte) 0xA4;
                part[3] = (byte) totalParts;
                part[4] = (byte) (parts.size() + 1);
            } else {
                part = new byte[partLength];

            }

            System.arraycopy(message, i, part, multipart ? 5 : 0, partLength);

            i += partLength;
            parts.add(part);
        }

        return parts;
    }
}
