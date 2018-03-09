package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class MainMenu_Activity extends AppCompatActivity {

    private static final String PREFERENCE_EMAIL = "saved.email.key";
    public static final String INTENT_TOKEN = "com.cmpt276.project.walkinggroupapp.intentToken";

    private Button youMonitorBtn;
    private Button monitorsYouBtn;

    private ListView youMonitorList;
    private ListView monitorsYouList;

    private List<User> youMonitor_tempList;
    private List<User> monitorBy_tempList;

    private WGServerProxy proxy;


    private User user_local;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        extractDataFromIntent();
        createUser();
        setupButton();
    }

    private void setupButton() {
        //register button
        youMonitorBtn = findViewById(R.id.jacky_add_monitoring_button);
        youMonitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddMonitoringUser.createAddIntent(getApplicationContext(), token);
                startActivity(intent);
            }
        });

        monitorsYouBtn = findViewById(R.id.jacky_add_monitoring_by_button);
        monitorsYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add intent
            }
        });
    }

    private void createUser() {
        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);
        String email = getSavedEmail();
        Call<User> caller = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(User user) {
        Log.i("MyApp", "Server replied with user: " + user.toString());
        user_local = user;

        Call<List<User>> caller = proxy.getMonitorsUsersById(user_local.getId());
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, monitorUserList -> updateYouMonitor(monitorUserList));

        Call<List<User>> newCaller = proxy.getMonitoredByUsersById(user_local.getId());
        ProxyBuilder.callProxy(MainMenu_Activity.this, newCaller, monitorByUserList -> updateMonitorBy(monitorByUserList));
    }

    private void updateYouMonitor(List<User> monitorUserList) {
        Log.i("MyApp","Inside update you");
        youMonitor_tempList = monitorUserList;
        populateMonitorUser();
    }

    private void populateMonitorUser() {
        ArrayAdapter<User> adapter = new monitorUserAdapter();
        //Configure ListView
        youMonitorList = findViewById(R.id.jacky_monitoring_list);
        youMonitorList.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class monitorUserAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public monitorUserAdapter() {
            super(MainMenu_Activity.this, R.layout.list_layout, user_local.getMonitorsUsers());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }
            //Find a user to add
            User currentUser = youMonitor_tempList.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(currentUser.getEmail());


            return itemView;
        }
    }

    private void updateMonitorBy(List<User> monitorByUserList) {
        Log.i("MyApp", "How many times CALLED???");
        monitorBy_tempList = monitorByUserList;
        populateMonitorByUser();
    }

    private void populateMonitorByUser() {
        ArrayAdapter<User> adapter = new monitorByUserAdapter();
        //Configure ListView
        monitorsYouList = findViewById(R.id.jacky_monitoing_by_list);
        monitorsYouList.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class monitorByUserAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public monitorByUserAdapter() {
            super(MainMenu_Activity.this, R.layout.list_layout, user_local.getMonitorByUsers());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }
            //Find a user to add
            Log.i("MyApp", "Inside Monitoring By");
            User currentUser = monitorBy_tempList.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(currentUser.getEmail());

            return itemView;
        }
    }

    public static Intent makeIntnet(Context context, String token){
        Intent intent = new Intent(context, MainMenu_Activity.class);
        intent.putExtra(INTENT_TOKEN, token);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        token = intent.getStringExtra(INTENT_TOKEN);
    }

    private String getSavedEmail()
    {
        SharedPreferences saveEmail= getSharedPreferences("MyData", MODE_PRIVATE);
        return saveEmail.getString(PREFERENCE_EMAIL, "0");
    }

}
