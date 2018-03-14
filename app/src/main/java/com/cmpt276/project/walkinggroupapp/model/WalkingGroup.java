package com.cmpt276.project.walkinggroupapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * This class model a walking group.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalkingGroup {

    private Long id;

    private String groupDescription;

    private double[] routeLatArray;
    private double[] routeLngArray;

    private User leader;

    private List<User> memberUsers = new ArrayList<>();

    private String href;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public double[] getRouteLatArray() {
        return routeLatArray;
    }

    public void setRouteLatArray(double[] routeLatArray) {
        this.routeLatArray = routeLatArray;
    }

    public double[] getRouteLngArray() {
        return routeLngArray;
    }

    public void setRouteLngArray(double[] routeLngArray) {
        this.routeLngArray = routeLngArray;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMemberUsers() {
        return memberUsers;
    }

    public void setMemberUsers(List<User> memberUsers) {
        this.memberUsers = memberUsers;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
