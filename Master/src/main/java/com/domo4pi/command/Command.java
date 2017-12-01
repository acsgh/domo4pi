package com.domo4pi.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class Command {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract List<String> getAuthorizedGroups();

    public abstract boolean canProcess(String command);

    public abstract String executeCommand(String command);

    protected String executeNativeCommand(String... command) {
        StringBuilder output = new StringBuilder();
        log.info("Executing native command: '{}'", Arrays.toString(command));
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            Scanner scan = new Scanner(process.getInputStream());

            while (scan.hasNextLine()) {
                if (output.length() != 0) {
                    output.append("\n");
                }
                output.append(scan.nextLine());
            }

        } catch (Exception e) {
            log.error("Unable to execute command: '" + command + "'", e);
        }
        return output.toString();
    }

}
