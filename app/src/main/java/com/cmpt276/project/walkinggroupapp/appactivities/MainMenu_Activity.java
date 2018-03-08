package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import retrofit2.Call;

public class MainMenu_Activity extends AppCompatActivity {

    public static final String INTENT_TOKEN = "com.cmpt276.project.walkinggroupapp.intentToken";
    private Button addYouMonitorButton;
    private Button addMonitorYouButton;

    private ListView youMonitorList;
    private ListView monitorYouList;

    private WGServerProxy proxy;

    private User user_local;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        extractDataFromIntent();
        createUser();
        populateListView();
    }

    private void populateListView()
    {
        //Create a List of Items already done with listCollection

        ArrayAdapter<User> adapter = new myListAdapter();
        //Configure ListView
        monitorYouList = findViewById(R.id.jacky_monitoring_by_list);
        monitorYouList.setAdapter(adapter);
    }

    private class myListAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public myListAdapter() {
            super(MainMenu_Activity.this, R.layout.list_layout, user_local.getMonitoredByUsers());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }

            //Find the pot to work with
            User currentUser = user_local.getUserBy(position);

            //Fill the view
            ImageView imageView =itemView.findViewById(R.id.jacky_temp_pic);
            //Done to set icon
            imageView.setImageResource(R.drawable.temp_pic);

            //Name:
            TextView makeName =itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeWeight = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeWeight.setText(currentUser.getEmail());


            return itemView;
        }
    }

    private void response(User user) {
        Log.w("MyApp", "Server replied with user: " + user.toString());
        user_local = user;
    }

    private void createUser() {
        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);

        Call<User> caller = proxy.getUserByEmail("testuser1@test.com");
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, returnedUser -> response(returnedUser));
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

}
