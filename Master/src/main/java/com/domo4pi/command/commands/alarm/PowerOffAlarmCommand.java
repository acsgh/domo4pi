package com.domo4pi.command.commands.alarm;

import com.domo4pi.command.Command;

import java.util.Arrays;
import java.util.List;

public class PowerOffAlarmCommand extends Command {

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return ((command.equalsIgnoreCase("poweroff alarm")) || (command.equalsIgnoreCase("apagar alarma")));
    }

    @Override
    public String executeCommand(String command) {
        executeNativeCommand("service", "domo4pi", " stop");
        return "OK";
    }
}
