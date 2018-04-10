package com.cmpt276.project.walkinggroupapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 21jac_000 on 2018-04-07.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Authorizors {

    private List<User> users = new ArrayList<>();

    private User whoApprovedOrDenied;

    private String status;

    public List<User> getUsers(){
        return users;
    }

    public User getWhoApprovedOrDenied(){
        return whoApprovedOrDenied;
    }

    public String getStatus(){
        return status;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    public void setWhoApprovedOrDenied(User whoApprovedOrDenied){
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }

    public void setStatus(String status){
        this.status = status;
    }




}
