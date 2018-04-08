package com.cmpt276.project.walkinggroupapp.model;

/**
 * Created by 21jac_000 on 2018-04-07.
 */

public class Permission {
    private Long id;

    private String action;
    private String status;
    private String message;

    private User userA;
    private User userB;

    private WalkingGroup groupG;

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

}
