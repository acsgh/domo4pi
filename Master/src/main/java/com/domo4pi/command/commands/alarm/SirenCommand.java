package com.domo4pi.command.commands.alarm;

import com.domo4pi.alarm.AlarmManager;
import com.domo4pi.command.Command;
import com.domo4pi.command.exceptions.InvalidCommand;
import com.domo4pi.utils.inject.Inject;

import java.util.Arrays;
import java.util.List;

public class SirenCommand extends Command {

    private final AlarmManager alarmManager;

    @Inject
    public SirenCommand(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("siren") || command.toLowerCase().startsWith("sirena");
    }

    @Override
    public String executeCommand(String command) {
        String[] parts = command.split(" ");

        if (parts.length >= 2) {
            if ((parts[1].equalsIgnoreCase("enable")) || (parts[1].equalsIgnoreCase("activar"))) {
                alarmManager.setEnableSiren(true);
                return "Ok";
            } else if ((parts[1].equalsIgnoreCase("disable")) || (parts[1].equalsIgnoreCase("desactivar"))) {
                alarmManager.setEnableSiren(false);
                return "Ok";
            }
            throw new InvalidCommand("Invalid Option");
        }
        throw new InvalidCommand("We need more argument");
    }
}
