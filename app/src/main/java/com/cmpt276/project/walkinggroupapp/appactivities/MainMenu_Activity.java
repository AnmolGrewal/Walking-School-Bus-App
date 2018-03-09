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
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class MainMenu_Activity extends AppCompatActivity {

    public static final String INTENT_TOKEN = "com.cmpt276.project.walkinggroupapp.intentToken";
    private Button addYouMonitorButton;
    private Button addMonitorYouButton;

    private ListView youMonitorList;
    private ListView monitorYouList;

    private WGServerProxy proxy;

    private User user_local;
    private User temp_user;
    private Boolean flag = false;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        extractDataFromIntent();
        Log.i("MyApp", "After extract");
        createUser();
        setupButton();
    }

    private void setupButton() {
        //register button
        addMonitorYouButton = findViewById(R.id.jacky_add_monitoring_you);
        addMonitorYouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To add user activity
                //Temp fix we add here but later in a different page
                long temp = 111;
                newOne(temp);
                Log.i("MyApp", "Data sent in is " + temp);

            }
        });
    }

    private void newOne(Long id)
    {
        Call<User> caller = proxy.getUserById(id);
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, newUser -> waitNew(newUser));
    }

    private void waitNew(User user)
    {
        Log.i("MyApp", "    User: " + user.toString());
        temp_user = user;
        Call<List<User>> caller = proxy.addNewMonitorsUser(user_local.getId(),temp_user);                    //Since only the id is provided
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, monitoringList -> AddUser(monitoringList));
    }

    private void AddUser(List <User> monitoringList)
    {
        Log.i("MyApp", "ADDED user");
        for (User user : monitoringList) {
            Log.w("MyApp", "    User: " + user.toString());
        }
        user_local.addUser(temp_user);
        populateListView();
    }

    private void populateListView()
    {
        //Create a List of Items already done with listCollection
        Log.i("MyApp", "Before ArrayAdapter");
        ArrayAdapter<User> adapter = new myListAdapter();
        Log.i("MyApp", "After ArrayAdapter");
        //Configure ListView
        monitorYouList = findViewById(R.id.jacky_monitoring_by_list);
        Log.i("MyApp", "After FindView");
        monitorYouList.setAdapter(adapter);
        Log.i("MyApp", "After afterSetAdapter");
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }
    
    private class myListAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public myListAdapter() {
            super(MainMenu_Activity.this, R.layout.list_layout, user_local.getMonitoredByUsers());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            Log.i("MyApp", "Inside getView");
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }

            //Find a user to add
            User currentUser = user_local.getUserBy(position);
            //Since Only Id is provided
            Call<User> caller = proxy.getUserById(currentUser.getId());                    //Since only the id is provided
            ProxyBuilder.callProxy(MainMenu_Activity.this, caller, returnedUser -> getUser(returnedUser));

            while(flag == false);
            flag = false;

            //Fill the view
            ImageView imageView =itemView.findViewById(R.id.jacky_temp_pic);
            //Done to set icon
            imageView.setImageResource(R.drawable.temp_pic);

            //Name:
            TextView makeName =itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(temp_user.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(temp_user.getEmail());


            return itemView;
        }
    }

    private void getUser(User user)
    {
        Log.i("MyApp", "Server replied with user: " + user.toString());
        temp_user = user;
        flag = true;
    }

    
    
    
    
    
    
    
    
    
    
    
    private void response(User user) {
        Log.i("MyApp", "Server replied with user: " + user.toString() );
        user_local = user;
        Log.i("MyApp", "Name:" + user_local.getName() + " Email: " + user_local.getEmail());                        //It is here because it is a critical section, we need the server response before making the list
        populateListView();

        Call<List<User>> caller = proxy.getUsers();
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, returnedUsers -> list(returnedUsers));
    }


    private void list(List<User> returnedUsers) {
        Log.w("MyApp", "All Users:");
        for (User user : returnedUsers) {
            Log.w("MyApp", "    User: " + user.toString());
        }
    }

    private void createUser() {
        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);
        Log.i("MyApp", "After getproxy" );

        Call<User> caller = proxy.getUserByEmail("1");                     //For now since the email is not being passed i will use a standard one
        Log.i("MyApp", "After Call" );
        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, returnedUser -> response(returnedUser));
        Log.i("MyApp", "After callProxy" );
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
