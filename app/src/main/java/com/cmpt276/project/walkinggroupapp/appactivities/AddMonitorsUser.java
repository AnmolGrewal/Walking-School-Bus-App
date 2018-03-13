package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class AddMonitorsUser extends AppCompatActivity {

    private static final String PREFERENCE_EMAIL= "saved.email.key";
    public static final String INTENT_TOKEN = "com.cmpt276.project.walkinggroupapp.intentToken";

    private User userLocal;

    private String token;

    private WGServerProxy proxy;

    private Button addBtn;

    private EditText editTextUserId;




    private ModelManager modelManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitoring_user);

        //Extract data from intent
//        extractDataFromIntent();
        //Need to recreate the user
//        createUser();
        //Need to wire button


        modelManager = ModelManager.getInstance();


        setUpButton();
    }

    private void setUpButton()
    {
        addBtn = findViewById(R.id.jacky_add_user_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Once clicked we get data from user input, find user, if found add else tell user error
                editTextUserId = findViewById(R.id.jacky_add_user);
                String strId = editTextUserId.getText().toString();
                long userId = Long.parseLong(strId);
//                findUser(longUserId);
                ProxyBuilder.SimpleCallback<List<User>> callback = monitorsUsers -> addNewMonitorsUserResponse(monitorsUsers);
                modelManager.addNewMonitorsUser(AddMonitorsUser.this, callback, userId);
            }
        });
    }

    private void addNewMonitorsUserResponse(List<User> monitorsUsers) {
        finish();
    }

//    private void response(User user) {
//        Log.i("MyApp", "Server replied with user: " + user.toString() );
//        userLocal = user;
//    }

//    private void createUser() {
//        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);
//        String email = getSavedEmail();
//        Log.i("MyApp", "Email is: " + email);
//        Call<User> caller = proxy.getUserByEmail(email);                     //For now since the email is not being passed i will use a standard one
//        ProxyBuilder.callProxy(AddMonitorsUser.this, caller, returnedUser -> response(returnedUser));
//    }

//    private String getSavedEmail()
//    {
//        SharedPreferences saveEmail= getSharedPreferences("MyData", MODE_PRIVATE);
//        return saveEmail.getString(PREFERENCE_EMAIL, "0");
//    }


//    private void findUser(Long id)
//    {
//        Call<User> caller = proxy.getUserById(id);
//        ProxyBuilder.callProxy(AddMonitorsUser.this, caller, newUser -> waitNew(newUser));
//    }

//    private void waitNew(User user)
//    {
//        Log.i("MyApp", "    User: " + user.toString());
//        User tempUser = user;
//        Call<List<User>> caller = proxy.addNewMonitorsUser(userLocal.getId(),tempUser);                    //Since only the id is provided
//        ProxyBuilder.callProxy(AddMonitorsUser.this, caller, monitoringList -> AddUser(monitoringList));
//    }

//    private void AddUser(List <User> monitoringList)
//    {
//        Log.i("MyApp", "ADDED user");
//        for (User user : monitoringList) {
//            Log.w("MyApp", "    User: " + user.toString());
//        }
//        finish();
//    }


    public static Intent makeIntent(Context context){
        //        intent.putExtra(INTENT_TOKEN, token);
        return new Intent(context, AddMonitorsUser.class);
    }

//    private void extractDataFromIntent(){
//        Intent intent = getIntent();
//        token = intent.getStringExtra(INTENT_TOKEN);
//    }
}
