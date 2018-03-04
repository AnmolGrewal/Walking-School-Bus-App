package com.cmpt276.project.walkinggroupapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class model a user account.
 */
public class User {

    private long id;

    private String name;
    private String emailAddress;

    // TODO: is password needed here?
    //private String password;

    // TODO: list of User or String (emailAddress);
    private List<User> monitoredByUsers;
    private List<User> monitorsUsers;

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

    public void addMonitoringUser(String emailAddress) {

    }

}
