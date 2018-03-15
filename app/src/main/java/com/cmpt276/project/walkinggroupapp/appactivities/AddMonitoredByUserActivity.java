package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.List;



/**
 * Class for viewing users monitored and users monitoring the current user
 * */

public class AddMonitoredByUserActivity extends AppCompatActivity {


    private Button addBtn;

    private EditText editTextUserId;



    private ModelManager modelManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitored_by_user);

        Log.i("MyApp", "INSIDE MONITOREDBY");



        modelManager = ModelManager.getInstance();




        setUpButton();
    }

    private void setUpButton()
    {
        Log.i("MyApp", "Inside set btn");
        addBtn = findViewById(R.id.jacky_add_by_user_button);
        Log.i("MyApp", "After find btton");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextUserId = findViewById(R.id.jacky_add_by_user);
                String userIdString = editTextUserId.getText().toString();
                if(userIdString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter a User ID", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    long userId = Long.parseLong(userIdString);

                    ProxyBuilder.SimpleCallback<List<User>> callback = monitoredByUsers -> addNewMonitoredByUserResponse(monitoredByUsers);
                    modelManager.addNewMonitoredByUser(AddMonitoredByUserActivity.this, callback, userId);
                }
            }
        });
    }

    private void addNewMonitoredByUserResponse(List<User> monitoredByUsers) {
        finish();
    }




    public static Intent makeIntent(Context context){
        //        intent.putExtra(INTENT_TOKEN, token);
        return new Intent(context, AddMonitoredByUserActivity.class);
    }

}
