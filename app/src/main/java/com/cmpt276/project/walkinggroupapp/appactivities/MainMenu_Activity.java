package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class MainMenu_Activity extends AppCompatActivity {

    private static final String PREFERENCE_EMAIL = "saved.email.key";
    public static final String INTENT_TOKEN = "com.cmpt276.project.walkinggroupapp.intentToken";

    private Button btnAddNewMonitorsUser;
    private Button btnAddNewMonitoredByUser;

    private ListView monitorsUsersListView;
    private ListView monitoredByUsersListView;

    private List<User> monitorsUsers;
    private List<User> monitoredByUsers;


//    private int selectedPosition;
    private WGServerProxy proxy;
//    private User userLocal;
    private String token;




    private ModelManager modelManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        modelManager = ModelManager.getInstance();


//        // TODO: delete this.
//        while (modelManager.getUser().getId() == null) {
//            Log.w("Error", "!!!!!!!!!!!!!!!!");
//        }



        ProxyBuilder.SimpleCallback<List<User>> getMonitorsUsersCallback = monitorsUsers -> getMonitorsUsersResponse(monitorsUsers);
        modelManager.getMonitorsUsers(MainMenu_Activity.this, getMonitorsUsersCallback);

        ProxyBuilder.SimpleCallback<List<User>> getMonitoredByUsersCallback = monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers);
        modelManager.getMonitoredByUsers(MainMenu_Activity.this, getMonitoredByUsersCallback);


//        extractDataFromIntent();
//        createUser();
        setupAddNewMonitorsUserButton();
        setupAddNewMonitoredByUserButton();
//        registerMonitorsUsersOnItemClick();
//        registerMonitoredByUsersOnItemClick();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // TODO
//        createUser();
        ProxyBuilder.SimpleCallback<List<User>> getMonitorsUsersCallback = monitorsUsers -> getMonitorsUsersResponse(monitorsUsers);
        modelManager.getMonitorsUsers(MainMenu_Activity.this, getMonitorsUsersCallback);

        ProxyBuilder.SimpleCallback<List<User>> getMonitoredByUsersCallback = monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers);
        modelManager.getMonitoredByUsers(MainMenu_Activity.this, getMonitoredByUsersCallback);
    }

    private void setupAddNewMonitorsUserButton() {
        //register button
        btnAddNewMonitorsUser = findViewById(R.id.jacky_add_monitoring_button);
        btnAddNewMonitorsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddMonitorsUser.makeIntent(getApplicationContext(), token);
                startActivity(intent);
            }
        });
    }

    private void setupAddNewMonitoredByUserButton(){
        btnAddNewMonitoredByUser = findViewById(R.id.jacky_add_monitoring_by_button);
        btnAddNewMonitoredByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddMonitoredByUser.makeIntent(getApplicationContext(), token);
                startActivity(intent);
            }
        });
    }

//    private void createUser() {
//        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);
//        String email = getSavedEmail();
//        Call<User> caller = proxy.getUserByEmail(email);
//        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, returnedUser -> response(returnedUser));
//    }

//    private void response(User user) {
//        Log.i("MyApp", "Server replied with user: " + user.toString());
//        userLocal = user;
//
//        Call<List<User>> caller = proxy.getMonitorsUsersById(userLocal.getId());
//        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, monitorsUsers -> getMonitorsUsersResponse(monitorsUsers));
//
//        Call<List<User>> newCaller = proxy.getMonitoredByUsersById(userLocal.getId());
//        ProxyBuilder.callProxy(MainMenu_Activity.this, newCaller, monitoredByUsers -> getMonitoredByUsersResponse(monitoredByUsers));
//    }

    private void getMonitorsUsersResponse(List<User> monitorsUsers) {
        Log.i("MyApp","Inside update you");
        this.monitorsUsers = monitorsUsers;
        populateMonitorsUsersList();
        registerMonitorsUsersOnItemClick();
    }

    private void populateMonitorsUsersList() {
        ArrayAdapter<User> adapter = new monitorsUsersAdapter();
        //Configure ListView
        monitorsUsersListView = findViewById(R.id.jacky_monitoring_list);
        monitorsUsersListView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class monitorsUsersAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public monitorsUsersAdapter() {
            super(MainMenu_Activity.this, R.layout.list_layout, monitorsUsers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }
            //Find a user to add
            User currentUser = monitorsUsers.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(currentUser.getEmail());


            return itemView;
        }
    }

    private void getMonitoredByUsersResponse(List<User> monitoredByUsers) {
        Log.i("MyApp", "How many times CALLED???");
        this.monitoredByUsers = monitoredByUsers;
        populateMonitoredByUsersList();
        registerMonitoredByUsersOnItemClick();
    }

    private void populateMonitoredByUsersList() {
        ArrayAdapter<User> adapter = new monitoredByUsersAdapter();
        //Configure ListView
        monitoredByUsersListView = findViewById(R.id.jacky_monitoing_by_list);
        monitoredByUsersListView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class monitoredByUsersAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public monitoredByUsersAdapter() {
            super(MainMenu_Activity.this, R.layout.list_layout, monitoredByUsers);
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
            User currentUser = monitoredByUsers.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(currentUser.getEmail());

            return itemView;
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainMenu_Activity.class);
    }

//    public static Intent makeIntent(Context context, String token){
//        Intent intent = new Intent(context, MainMenu_Activity.class);
//        intent.putExtra(INTENT_TOKEN, token);
//        return intent;
//    }

//    private void extractDataFromIntent(){
//        Intent intent = getIntent();
//        token = intent.getStringExtra(INTENT_TOKEN);
//    }

//    private String getSavedEmail()
//    {
//        SharedPreferences saveEmail= getSharedPreferences("MyData", MODE_PRIVATE);
//        return saveEmail.getString(PREFERENCE_EMAIL, "0");
//    }

    private void registerMonitorsUsersOnItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_monitoring_list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id)
            {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
//                selectedPosition = position;
                PopupMenu popupMenu = new PopupMenu(MainMenu_Activity.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId())
                        {
                            case R.id.cancel:
//                                doCancel();
                                break;
                            case R.id.delete:
                                removeMonitorsUserByPosition(position);
                                break;
                        }
                        return true;
                    }

                });

                popupMenu.show();
                return true;
            }
        });
    }

    private void registerMonitoredByUsersOnItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_monitoing_by_list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id)
            {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
//                selectedPosition = position;
                PopupMenu popupMenu = new PopupMenu(MainMenu_Activity.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId())
                        {
                            case R.id.cancel:
//                                doCancel();
                                break;
                            case R.id.delete:
                                removeMonitoredByUserByPosition(position);
                                break;
                        }
                        return true;
                    }

                });

                popupMenu.show();
                return true;
            }
        });
    }


//    // TODO: not necessary.
//    private void doCancel()
//    {
//        //Do nothing XD
//    }

    private void removeMonitorsUserByPosition(int position)
    {
//        User tempUser = monitorsUsers.get(position);
//        Call<Void> caller = proxy.removeMonitorsUser(userLocal.getId(), tempUser.getId());
//        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, noResponse -> redrawMonitorUser(noResponse));
    }

    private void removeMonitoredByUserByPosition(int position)
    {
//        User tempUser = monitoredByUsers.get(position);
//        Call<Void> caller = proxy.removeMonitoredByUser(userLocal.getId(), tempUser.getId());
//        ProxyBuilder.callProxy(MainMenu_Activity.this, caller, noResponse -> redrawMonitorUser(noResponse));
    }

    private void redrawMonitorUser(Void nothing)
    {
        Log.i("MyApp", "Removed USER");
//        createUser();
    }

}
