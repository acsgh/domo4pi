package com.domo4pi.command.commands.alarm;

import com.domo4pi.command.Command;
import com.domo4pi.command.exceptions.InvalidCommand;
import com.domo4pi.notification.NotificationsManager;
import com.domo4pi.utils.inject.Inject;

import java.util.Arrays;
import java.util.List;

public class NotificationCommand extends Command {

    private final NotificationsManager notificationsManager;

    @Inject
    public NotificationCommand(NotificationsManager notificationsManager) {
        this.notificationsManager = notificationsManager;
    }

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("notifications");
    }

    @Override
    public String executeCommand(String command) {
        String[] parts = command.split(" ");

        if (parts.length >= 3) {
            if (parts[1].equalsIgnoreCase("add")) {
                String group = parts[2];
                String number = extractRest(parts);

                notificationsManager.addRecipient(group, number);
                return "OK";
            } else if (parts[1].equalsIgnoreCase("del")) {
                String group = parts[2];
                String number = extractRest(parts);

                notificationsManager.removeRecipient(group, number);
                return "OK";
            } else if (parts[1].equalsIgnoreCase("send")) {
                String group = parts[2];
                String message = extractRest(parts);

                notificationsManager.notify(group, message);
                return "OK";
            }
            throw new InvalidCommand("Invalid Option");
        }
        throw new InvalidCommand("We need more argument");
    }

    private String extractRest(String[] parts) throws InvalidCommand {
        StringBuilder result = new StringBuilder();

        for (int i = 3; i < parts.length; i++) {
            if (i > 3) {
                result.append(" ");
            }
            result.append(parts[i]);
        }

        return result.toString();
    }
}
