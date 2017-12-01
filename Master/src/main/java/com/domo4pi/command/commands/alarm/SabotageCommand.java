package com.domo4pi.command.commands.alarm;

import com.domo4pi.alarm.AlarmManager;
import com.domo4pi.command.Command;
import com.domo4pi.command.exceptions.InvalidCommand;
import com.domo4pi.utils.inject.Inject;

import java.util.Arrays;
import java.util.List;

public class SabotageCommand extends Command {

    private final AlarmManager alarmManager;

    @Inject
    public SabotageCommand(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("sabotage");
    }

    @Override
    public String executeCommand(String command) {
        String[] parts = command.split(" ");

        if (parts.length >= 2) {
            if (parts[1].equalsIgnoreCase("enable")) {
                if (alarmManager.isSabotageEnabled()) {
                    return "Already enabled";
                } else {
                    alarmManager.setSabotageEnabled(true);
                    return "OK";
                }
            } else if (parts[1].equalsIgnoreCase("disable")) {
                if (alarmManager.isSabotageEnabled()) {
                    alarmManager.setSabotageEnabled(false);
                    return "OK";
                } else {
                    return "Already disabled";
                }
            } else if (parts[1].equalsIgnoreCase("reset")) {
                alarmManager.resetSabotage();
                return "OK";
            }
            throw new InvalidCommand("Invalid Option");
        }
        throw new InvalidCommand("We need more argument");
    }
}
