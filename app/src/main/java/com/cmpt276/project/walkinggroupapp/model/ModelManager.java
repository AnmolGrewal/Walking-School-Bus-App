package com.cmpt276.project.walkinggroupapp.model;

import android.content.Context;

import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import java.security.Policy;
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


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        proxy = ProxyBuilder.getProxy(apiKey, token);
    }


    // TODO: I think the callback param should return Void.
    public void register(Context context,
                         ProxyBuilder.SimpleCallback<Void> callback,
                         String name, String emailAddress, String password) {

        // TODO: do I need to login when register?
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(emailAddress);
        newUser.setPassword(password);

        Call<User> caller = proxy.createNewUser(newUser);
        ProxyBuilder.callProxy(context, caller, returnedUser -> {
            user = returnedUser;
            callback.callback(null);
        });

    }


    public void login(Context context,
                      ProxyBuilder.SimpleCallback<Void> callback,
                      String emailAddress, String password) {
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
//    }

//    public void updateUser() {
////        if (user.getId() != null) {
////            Call<User> caller = proxy.getUserById(user.getId());
////            ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
////        } else if (user.getEmail() != null){
////            Call<User> caller = proxy.getUserByEmail(user.getEmail());
////            ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
////        } else {
////        }
//
//        Call<User> caller = proxy.getUserByEmail(user.getEmail());
//        ProxyBuilder.callProxy(caller, returnedUser -> getUserResponse(returnedUser));
//
////        while (true) {
////            if (user.getId() == null) {
////                try {
////                    Thread.sleep(100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            } else {
////                break;
////            }
////        }
//
//    }



//    public User getUser() {
////        updateUser();
//        return user;
//    }


//    public long getUserId() {
//        return user.getId();
//    }


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

    public void addNewMonitorsUser(Context context,
                                   ProxyBuilder.SimpleCallback<List<User>> callback,
                                   long targetId) {
//        updateUser();
        User newUser = new User();
        newUser.setId(targetId);
        Call<List<User>> caller = proxy.addNewMonitorsUser(user.getId(), newUser);
//        ProxyBuilder.callProxy(context, caller, monitorsUsers -> getMonitorsUsersResponse(monitorsUsers));
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void addNewMonitoredByUser(Context context,
                                      ProxyBuilder.SimpleCallback<List<User>> callback,
                                      long targetId) {
//        updateUser();
        User newUser = new User();
        newUser.setId(targetId);
        Call<List<User>> caller = proxy.addNewMonitoredByUser(user.getId(), newUser);
//        ProxyBuilder.callProxy(context, caller, monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers));
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void removeMonitorsUser(Context context,
                                   ProxyBuilder.SimpleCallback<Void> callback,
                                   long targetId) {
//        User userToRemove = new User();
//        userToRemove.setId(targetId);
        Call<Void> caller = proxy.removeMonitorsUser(user.getId(), targetId);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void removeMonitoredByUser(Context context,
                                      ProxyBuilder.SimpleCallback<Void> callback,
                                      long targetId) {
//        User userToRemove = new User();
//        userToRemove.setId(targetId);
        Call<Void> caller = proxy.removeMonitoredByUser(user.getId(), targetId);
        ProxyBuilder.callProxy(context, caller, callback);
    }






//    private void getUserResponse(User returnedUser) {
////        user = returnedUser;
//
////        userId = returnedUser.getId();
////        user.setId(userId);
//
//        user.setId(returnedUser.getId());
//        user.setId(returnedUser.getId());
//        user.setName(returnedUser.getName());
//        user.setEmail(returnedUser.getEmail());
//        user.setMonitoredByUsers(returnedUser.getMonitoredByUsers());
//        user.setMonitorsUsers(returnedUser.getMonitorsUsers());
//        user.setWalkingGroups(returnedUser.getWalkingGroups());
//        user.setHref(returnedUser.getHref());
//    }

//    private void getMonitorsUsersResponse(List<User> monitorsUsers) {
//        user.setMonitorsUsers(monitorsUsers);
//    }
//
//    private void getMonitoredByUsersResponse(List<User> monitoredByUsers) {
//        user.setMonitoredByUsers(monitoredByUsers);
//    }

}
