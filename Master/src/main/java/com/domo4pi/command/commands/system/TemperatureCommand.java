package com.domo4pi.command.commands.system;

import com.domo4pi.command.Command;

import java.util.Arrays;
import java.util.List;

public class TemperatureCommand extends Command {


    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return ((command.equalsIgnoreCase("temp")) || (command.equalsIgnoreCase("temperature")));
    }

    @Override
    public String executeCommand(String command) {
        return executeNativeCommand("/opt/vc/bin/vcgencmd", "measure_temp").replace("temp=", "");
    }
}
