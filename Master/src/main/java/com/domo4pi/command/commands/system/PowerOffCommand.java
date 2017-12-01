package com.domo4pi.command.commands.system;

import com.domo4pi.command.Command;

import java.util.Arrays;
import java.util.List;

public class PowerOffCommand extends Command {

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return ((command.equalsIgnoreCase("poweroff")) || (command.equalsIgnoreCase("apagar")));
    }

    @Override
    public String executeCommand(String command) {
        executeNativeCommand("poweroff");
        return "OK";
    }
}
