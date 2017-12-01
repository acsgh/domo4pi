package com.domo4pi.command.commands.alarm;

import com.domo4pi.alarm.AlarmDefinition;
import com.domo4pi.alarm.area.Area;
import com.domo4pi.alarm.nodes.protocol.ProtocolStatus;
import com.domo4pi.command.Command;
import com.domo4pi.command.exceptions.InvalidCommand;
import com.domo4pi.utils.inject.Inject;

import java.util.Arrays;
import java.util.List;

public class AlarmCommand extends Command {

    private final AlarmDefinition alarmDefinition;

    @Inject
    public AlarmCommand(AlarmDefinition alarmDefinition) {
        this.alarmDefinition = alarmDefinition;
    }

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return (command.toLowerCase().startsWith("alarm")) || (command.toLowerCase().startsWith("alarma"));
    }

    @Override
    public String executeCommand(String command) {
        String[] parts = command.split(" ");

        if (parts.length >= 2) {
            if ((parts[1].equalsIgnoreCase("enable")) || (parts[1].equalsIgnoreCase("activar"))) {
                Area area = getArea(parts);

                if (area == null) {
                    for (Area areaItem : alarmDefinition.areas) {
                        areaItem.setStatus(ProtocolStatus.Activated);
                    }
                } else {
                    area.setStatus(ProtocolStatus.Activated);
                }
                return alarmDefinition.getStatus();
            } else if ((parts[1].equalsIgnoreCase("disable")) || (parts[1].equalsIgnoreCase("desactivar"))) {
                Area area = getArea(parts);

                if (area == null) {
                    for (Area areaItem : alarmDefinition.areas) {
                        areaItem.setStatus(ProtocolStatus.Iddle);
                    }
                } else {
                    area.setStatus(ProtocolStatus.Iddle);
                }
                return alarmDefinition.getStatus();
            }
            throw new InvalidCommand("Invalid Option");
        }
        throw new InvalidCommand("Invalid Command");
    }

    private Area getArea(String[] parts) {
        String name = extractRest(parts);
        Area result = null;

        for (Area area : alarmDefinition.areas) {
            if (area.name.equalsIgnoreCase(name)) {
                result = area;
                break;
            }
        }
        return result;
    }

    private String extractRest(String[] parts) throws InvalidCommand {
        StringBuilder result = new StringBuilder();

        for (int i = 2; i < parts.length; i++) {
            if (i > 2) {
                result.append(" ");
            }
            result.append(parts[i]);
        }

        return result.toString();
    }
}
