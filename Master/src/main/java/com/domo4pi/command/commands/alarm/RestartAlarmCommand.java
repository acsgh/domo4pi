package com.domo4pi.command.commands.alarm;

import com.domo4pi.command.Command;

import java.util.Arrays;
import java.util.List;

public class RestartAlarmCommand extends Command {


    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return ((command.equalsIgnoreCase("reiniciar alarma")) || (command.equalsIgnoreCase("restart alarm")));
    }

    @Override
    public String executeCommand(String command) {
        executeNativeCommand("service", "domo4pi", "restart");
        return "OK";
    }
}
