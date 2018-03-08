package com.cmpt276.project.walkinggroupapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zherenx on 2018-03-03.
 */
public class WalkingGroup {

    private Long id;

    private String groupDescription;

    private long[] routeLatArray;
    private long[] routeLngArray;

    private User leader;

    private List<User> memberUser = new ArrayList<>();

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

    public long[] getRouteLatArray() {
        return routeLatArray;
    }

    public void setRouteLatArray(long[] routeLatArray) {
        this.routeLatArray = routeLatArray;
    }

    public long[] getRouteLngArray() {
        return routeLngArray;
    }

    public void setRouteLngArray(long[] routeLngArray) {
        this.routeLngArray = routeLngArray;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMemberUser() {
        return memberUser;
    }

    public void setMemberUser(List<User> memberUser) {
        this.memberUser = memberUser;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
