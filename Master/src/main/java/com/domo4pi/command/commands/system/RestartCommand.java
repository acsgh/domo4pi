package com.domo4pi.command.commands.system;

import com.domo4pi.command.Command;

import java.util.Arrays;
import java.util.List;

public class RestartCommand extends Command {


    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return ((command.equalsIgnoreCase("reiniciar")) || (command.equalsIgnoreCase("restart")));
    }

    @Override
    public String executeCommand(String command) {
        executeNativeCommand("reboot");
        return "OK";
    }
}
