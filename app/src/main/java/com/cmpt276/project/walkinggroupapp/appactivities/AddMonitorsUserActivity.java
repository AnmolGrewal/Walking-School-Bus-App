package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * Class for adding users monitored and users monitoring the current user
 * */
public class AddMonitorsUserActivity extends AppCompatActivity {


    private Button addBtn;

    private EditText editTextUserId;




    private ModelManager modelManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitors_user);


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
                String userIdString = editTextUserId.getText().toString();
                if(userIdString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter a User ID", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    long userId = Long.parseLong(userIdString);
                    ProxyBuilder.SimpleCallback<List<User>> callback = monitorsUsers -> addNewMonitorsUserResponse(monitorsUsers);
                    modelManager.addNewMonitorsUser(AddMonitorsUserActivity.this, callback, userId);
                }
            }
        });
    }

    private void addNewMonitorsUserResponse(List<User> monitorsUsers) {
        finish();
    }




    public static Intent makeIntent(Context context){
        //        intent.putExtra(INTENT_TOKEN, token);
        return new Intent(context, AddMonitorsUserActivity.class);
    }

}
