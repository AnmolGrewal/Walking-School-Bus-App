package com.cmpt276.project.walkinggroupapp.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

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

}
