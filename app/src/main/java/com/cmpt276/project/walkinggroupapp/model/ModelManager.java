package com.cmpt276.project.walkinggroupapp.model;

import android.content.Context;

import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

/**
 * Created by zherenx on 2018-03-03.
 */

public class ModelManager {

    private static ModelManager instance = null;

    private String apiKey = null;

    private WGServerProxy proxy = null;

    private String token = null;

    private User user = null;




    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

//    private ModelManager() {
//
//    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        proxy = ProxyBuilder.getProxy(apiKey, token);
    }

    public void login(Context context, String emailAddress, String password) {
        user = new User();
        user.setEmail(emailAddress);
        user.setPassword(password);

        ProxyBuilder.setOnTokenReceiveCallback(token -> onReceiveToken(token));

        Call<Void> caller = proxy.login(user);
        ProxyBuilder.callProxy(context, caller, returnedNothing -> loginResponse(returnedNothing));
    }


//    private void buildProxy() {
//        proxy = ProxyBuilder.getProxy(apiKey, token);
//    }

    private void onReceiveToken(String token) {
        proxy = ProxyBuilder.getProxy(apiKey, token);
    }

    private void loginResponse(Void returnedNothing) {
        // TODO: do something?
    }





    public User getUser() {
        updateUser();
        return user;
    }

    public List<User> getMonitorsUsers(Context context) {
        updateUser();
        Call<List<User>> caller = proxy.getMonitorsUsersById(user.getId());
        ProxyBuilder.callProxy(context, caller, monitorsUsers -> getMonitorsUsersResponse(monitorsUsers));
        return user.getMonitorsUsers();
    }

    public List<User> getMonitoredByUsers(Context context) {
        updateUser();
        Call<List<User>> caller = proxy.getMonitoredByUsersById(user.getId());
        ProxyBuilder.callProxy(context, caller, monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers));
        return user.getMonitoredByUsers();
    }

    public void addNewMonitorsUser(Context context, long idOfTarget) {
        updateUser();
        User newUser = new User();
        newUser.setId(idOfTarget);
        Call<List<User>> caller = proxy.addNewMonitorsUser(user.getId(), newUser);
        ProxyBuilder.callProxy(context, caller, monitorsUsers -> getMonitorsUsersResponse(monitorsUsers));
    }

    public void addNewMonitoredByUser(Context context, long idOfTarget) {
        updateUser();
        User newUser = new User();
        newUser.setId(idOfTarget);
        Call<List<User>> caller = proxy.addNewMonitoredByUser(user.getId(), newUser);
        ProxyBuilder.callProxy(context, caller, monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers));
    }




    private void updateUser() {
        if (user.getId() != null) {
            Call<User> caller = proxy.getUserById(user.getId());
            ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
        } else if (user.getEmail() != null){
            Call<User> caller = proxy.getUserByEmail(user.getEmail());
            ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
        } else {
            // TODO: throw exception
        }

    }

    private void getUserResponse(User returnedUser) {
        user = returnedUser;
    }

    private void getMonitorsUsersResponse(List<User> monitorsUsers) {
        user.setMonitorsUsers(monitorsUsers);
    }

    private void getMonitoredByUsersResponse(List<User> monitoredByUsers) {
        user.setMonitoredByUsers(monitoredByUsers);
    }

}
