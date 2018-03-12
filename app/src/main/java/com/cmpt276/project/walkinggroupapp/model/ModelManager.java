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

//    private long userId = -1;





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

    public void login(Context context, ProxyBuilder.SimpleCallback<Void> callback, String emailAddress, String password) {
        user = new User();
        user.setEmail(emailAddress);
        user.setPassword(password);
        ProxyBuilder.setOnTokenReceiveCallback(token -> onReceiveToken(token));
        Call<Void> caller = proxy.login(user);
//        ProxyBuilder.callProxy(context, caller, callback);
        ProxyBuilder.callProxy(context, caller, returnNothing -> {
            Call<User> getUserCaller = proxy.getUserByEmail(user.getEmail());
            ProxyBuilder.callProxy(getUserCaller, returnedUser -> {
                user = returnedUser;
                callback.callback(null);
            });
        });
//        ProxyBuilder.callProxy(context, caller, returnedNothing -> loginResponse(returnedNothing));


    }




    private void onReceiveToken(String token) {
        proxy = ProxyBuilder.getProxy(apiKey, token);
    }

//    private void loginResponse(Void returnedNothing) {
//        // TODO: do something?
//    }

    public void updateUser() {
//        if (user.getId() != null) {
//            Call<User> caller = proxy.getUserById(user.getId());
//            ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
//        } else if (user.getEmail() != null){
//            Call<User> caller = proxy.getUserByEmail(user.getEmail());
//            ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
//        } else {
//            // TODO: throw exception
//        }

        Call<User> caller = proxy.getUserByEmail(user.getEmail());
        ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));

//        while (true) {
//            if (user.getId() == null) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                break;
//            }
//        }

    }



    public User getUser() {
//        updateUser();
        return user;
    }

    public void getMonitorsUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback) {
//        updateUser();
        Call<List<User>> caller = proxy.getMonitorsUsersById(user.getId());
        ProxyBuilder.callProxy(context, caller, callback);
//        ProxyBuilder.callProxy(context, caller, monitorsUsers -> getMonitorsUsersResponse(monitorsUsers));
//        return user.getMonitorsUsers();
    }

    public void getMonitoredByUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback) {
//        updateUser();
        Call<List<User>> caller = proxy.getMonitoredByUsersById(user.getId());
        ProxyBuilder.callProxy(context, caller, callback);
//        ProxyBuilder.callProxy(context, caller, monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers));
//        return user.getMonitoredByUsers();
    }

    public void addNewMonitorsUser(Context context, long idOfTarget) {
//        updateUser();
        User newUser = new User();
        newUser.setId(idOfTarget);
        Call<List<User>> caller = proxy.addNewMonitorsUser(user.getId(), newUser);
        ProxyBuilder.callProxy(context, caller, monitorsUsers -> getMonitorsUsersResponse(monitorsUsers));
    }

    public void addNewMonitoredByUser(Context context, long idOfTarget) {
//        updateUser();
        User newUser = new User();
        newUser.setId(idOfTarget);
        Call<List<User>> caller = proxy.addNewMonitoredByUser(user.getId(), newUser);
        ProxyBuilder.callProxy(context, caller, monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers));
    }






    private void getUserResponse(User returnedUser) {
//        user = returnedUser;

//        userId = returnedUser.getId();
//        user.setId(userId);

        user.setId(returnedUser.getId());
        user.setId(returnedUser.getId());
        user.setName(returnedUser.getName());
        user.setEmail(returnedUser.getEmail());
        user.setMonitoredByUsers(returnedUser.getMonitoredByUsers());
        user.setMonitorsUsers(returnedUser.getMonitorsUsers());
        user.setWalkingGroups(returnedUser.getWalkingGroups());
        user.setHref(returnedUser.getHref());
    }

    private void getMonitorsUsersResponse(List<User> monitorsUsers) {
        user.setMonitorsUsers(monitorsUsers);
    }

    private void getMonitoredByUsersResponse(List<User> monitoredByUsers) {
        user.setMonitoredByUsers(monitoredByUsers);
    }

}
