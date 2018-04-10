package com.cmpt276.project.walkinggroupapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 21jac_000 on 2018-04-07.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission {
    private Long id;

    private String action;
    private String status;
    private String message;

    private User userA;
    private User userB;

    private WalkingGroup groupG;

    private List<Authorizors> authorizors = new ArrayList<>();

    public Long getId(){
        return id;
    }

    public String getAction(){
        return action;
    }

    public String getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public User getUserA(){
        return userA;
    }

    public User getUserB(){
        return userB;
    }

    public WalkingGroup getGroupG(){
        return groupG;
    }

    public List<Authorizors> getAuthorizors(){
        return authorizors;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setAction(String action){
        this.action = action;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setUserA(User userA){
        this.userA = userA;
    }

    public void setUserB(User userB){
        this.userB = userB;
    }

    public void setGroupG(WalkingGroup groupG){
        this.groupG = groupG;
    }

    public void setAuthorizors(List<Authorizors> authorizors){
        this.authorizors = authorizors;
    }

}
