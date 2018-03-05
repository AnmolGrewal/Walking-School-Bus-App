package com.cmpt276.project.walkinggroupapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class model a user account.
 */
public class User {

    // TODO: type of id -> int, long, unsigned int?
    private int id;

    private String name;
    private String emailAddress;

    // TODO: is password needed here?
    //private String password;

    // TODO: list of User, or String (emailAddress), a user reference (very likely);
    private List<UserReference> monitoredByUsers;
    private List<UserReference> monitorsUsers;

    private List<Group> walkingGroups;

    // TODO: need a new name;
    private String href;


//    public User(String name, String emailAddress, String password) {
//        this.name = name;
//        this.emailAddress = emailAddress;
//
//        this.password = password;
//
//        monitorsUsers = new ArrayList<>();
//        monitoredByUsers = new ArrayList<>();
//    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void addNewMonitoredByUser(UserReference userReference) {
        monitoredByUsers.add(userReference);
    }

    public void addNewMonitorsUser(UserReference userReference) {
        monitorsUsers.add(userReference);
    }

    public void removeUserFromMonitoredList(int id) throws UserReferenceNotFoundException {
        for (int i = 0; i < monitoredByUsers.size(); i++) {
            if (monitoredByUsers.get(i).equals(id)) {
                monitoredByUsers.remove(i);
                return;
            }
        }
        throw new UserReferenceNotFoundException(
                "Internal problem occurs, target user reference not found in monitoredByUsers list");
    }

    public void removeUserFromMonitoringList() throws UserReferenceNotFoundException {
        for (int i = 0; i < monitorsUsers.size(); i++) {
            if (monitorsUsers.get(i).equals(id)) {
                monitorsUsers.remove(i);
                return;
            }
        }
        throw new UserReferenceNotFoundException(
                "Internal problem occurs, target user reference not found in monitorsUsers list");
    }

    public boolean hasUserInMonitoredList() {
        return false;
    }

    public boolean hasUserInMonitoringList() {
        return false;
    }




}
