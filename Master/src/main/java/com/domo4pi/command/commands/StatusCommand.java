package com.domo4pi.command.commands;

import com.domo4pi.alarm.AlarmDefinition;
import com.domo4pi.alarm.AlarmManager;
import com.domo4pi.application.Application;
import com.domo4pi.command.Command;
import com.domo4pi.saldo.SaldoChecker;
import com.domo4pi.utils.inject.Inject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class StatusCommand extends Command {

    private final NumberFormat formatter = new DecimalFormat("#0.00");

    private final SaldoChecker saldoChecker;
    private final AlarmManager alarmManager;
    private final AlarmDefinition alarmDefinition;

    @Inject
    public StatusCommand(SaldoChecker saldoChecker, AlarmManager alarmManager, AlarmDefinition alarmDefinition) {
        this.saldoChecker = saldoChecker;
        this.alarmManager = alarmManager;
        this.alarmDefinition = alarmDefinition;
    }

    @Override
    public List<String> getAuthorizedGroups() {
        return Arrays.asList("Admin", "Alarm");
    }

    @Override
    public boolean canProcess(String command) {
        return ((command.equalsIgnoreCase("status")) || (command.equalsIgnoreCase("estado")));
    }

    @Override
    public String executeCommand(String command) {
        StringBuilder response = new StringBuilder();
        response.append("Temp: ").append(getTemperature()).append("\r\n");
        response.append("Bal: ").append((saldoChecker.getSaldo() == null) ? "Unknown" : formatter.format(saldoChecker.getSaldo()) + "€").append("\r\n");
        response.append("Sab: ").append(alarmDefinition.isSabotageEnabled() ? "On" : "Off").append("\r\n");
        response.append("Alarm: ").append(alarmManager.getStatus()).append(" (").append(alarmDefinition.getStatus()).append(")");
        return response.toString();
    }

    private String getTemperature() {
        return executeNativeCommand("/opt/vc/bin/vcgencmd", "measure_temp").replace("temp=", "");
    }
}
