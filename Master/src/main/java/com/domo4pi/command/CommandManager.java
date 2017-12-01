package com.domo4pi.command;

import com.domo4pi.command.commands.StatusCommand;
import com.domo4pi.command.commands.alarm.*;
import com.domo4pi.command.commands.system.PowerOffCommand;
import com.domo4pi.command.commands.system.RestartCommand;
import com.domo4pi.command.commands.system.TemperatureCommand;
import com.domo4pi.command.exceptions.CommandNotFound;
import com.domo4pi.command.exceptions.UnauthorizedCommand;
import com.domo4pi.notification.NotificationsManager;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandManager {

    private final Logger log = LoggerFactory.getLogger(CommandManager.class);

    private final NotificationsManager notificationsManager;
    private final List<Command> commands;

    private List<Command> getCommands(Injector injector) {
        List<Command> commands = new ArrayList<>();

        commands.add(injector.getInstance(StatusCommand.class));
        commands.add(injector.getInstance(PowerOffCommand.class));
        commands.add(injector.getInstance(RestartCommand.class));
        commands.add(injector.getInstance(TemperatureCommand.class));

        commands.add(injector.getInstance(PowerOffAlarmCommand.class));
        commands.add(injector.getInstance(RestartAlarmCommand.class));
        commands.add(injector.getInstance(SabotageCommand.class));
        commands.add(injector.getInstance(SirenCommand.class));
        commands.add(injector.getInstance(NotificationCommand.class));
        commands.add(injector.getInstance(AlarmCommand.class));

        return commands;
    }

    @Inject
    public CommandManager(NotificationsManager notificationsManager, Injector injector) {
        this.notificationsManager = notificationsManager;
        this.commands = getCommands(injector);
    }


    public String executeCommand(String user, String commandString) {
        commandString = commandString.trim();

        log.info("Incoming command, user: {}, {}", user, commandString);

        for (Command command : commands) {
            if (command.canProcess(commandString)) {
                if (hasGrants(user, command.getAuthorizedGroups())) {
                    return command.executeCommand(commandString);
                } else {
                    log.error("Incoming command unauthorized");
                    throw new UnauthorizedCommand(commandString);
                }
            }
        }
        log.error("Incoming command not found");
        throw new CommandNotFound(commandString);
    }

    private boolean hasGrants(String user, List<String> authorizedGroups) {
        boolean result = false;
        if (user.equals("admin")) {
            result = true;
        } else {
            Set<String> userGroups = notificationsManager.getUserGroups(user);
            for (String authorizedGroup : authorizedGroups) {
                if (userGroups.contains(authorizedGroup)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}