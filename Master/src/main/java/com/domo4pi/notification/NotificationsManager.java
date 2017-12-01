package com.domo4pi.notification;

import com.domo4pi.core.AbstractManager;
import com.domo4pi.gsm.GSMManager;
import com.domo4pi.utils.inject.Inject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class NotificationsManager extends AbstractManager {

    private final GSMManager gsmManager;
    private final NotificationRegistry notificationsConfiguration;

    @Inject
    public NotificationsManager(GSMManager gsmManager, NotificationRegistry notificationsConfiguration) {
        this.gsmManager = gsmManager;
        this.notificationsConfiguration = notificationsConfiguration;
    }

    public void addRecipient(String group, String address) {
        Set<String> groupAddresses = getGroup(group, true);

        if (validNumber(address)) {
            groupAddresses.add(address);
            notificationsConfiguration.store();
        } else {
            throw new IllegalArgumentException("Invalid phone number: " + address);
        }
    }

    public void removeRecipient(String group, String address) {
        Set<String> groupAddresses = getGroup(group, false);

        if (groupAddresses != null) {
            groupAddresses.remove(address);
            notificationsConfiguration.store();
        } else {
            throw new IllegalArgumentException("Invalid phone number: " + address);
        }
    }

    public void notify(String group, String message) {
        Set<String> addresses = getGroup(group, false);

        if (addresses != null) {
            for (String address : addresses) {
                gsmManager.sendSMS(address, message);
            }
        } else {
            log.debug("Invalid group: {}", group);
            throw new IllegalArgumentException("Invalid group: " + group);
        }
    }


    public Set<String> getUserGroups(String user) {
        Set<String> result = new HashSet<>();
        Map<String, Set<String>> groups = notificationsConfiguration.addressByGroup;

        for (String group : groups.keySet()) {
            if (groups.get(group).contains(user)) {
                result.add(group);
            }
        }

        return result;
    }

    private boolean validNumber(String address) {
        return ((address != null) && (address.length() > 8));
    }

    private Set<String> getGroup(String group, boolean create) {
        Map<String, Set<String>> groups = notificationsConfiguration.addressByGroup;

        if ((!groups.containsKey(group)) && (create)) {
            groups.put(group, new TreeSet<String>());
        }

        return groups.get(group);
    }
}