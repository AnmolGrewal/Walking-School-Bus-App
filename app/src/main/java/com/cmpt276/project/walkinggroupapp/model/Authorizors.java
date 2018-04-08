package com.cmpt276.project.walkinggroupapp.model;

import java.util.List;

/**
 * Created by 21jac_000 on 2018-04-07.
 */

public class Authorizors {
    private List<User> parentOfUser;

    private User whoApprovedOrDenied;

    private String status;

    public List<User> getParentOfUser(){
        return parentOfUser;
    }

    public User getWhoApprovedOrDenied(){
        return whoApprovedOrDenied;
    }

    public String getStatus(){
        return status;
    }

    public void setParentOfUser(User parentOfUser){
        this.parentOfUser.add(parentOfUser);
    }

    public void setWhoApprovedOrDenied(User whoApprovedOrDenied){
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }

    public void setStatus(String status){
        this.status = status;
    }




}
