package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.fragments.viewPastPermissionFragment;
import com.cmpt276.project.walkinggroupapp.fragments.viewPermissionFragment;
import com.cmpt276.project.walkinggroupapp.model.Authorizors;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.Permission;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.ArrayList;
import java.util.List;

public class CheckPermissionActivity extends AppCompatActivity {

    private ListView permissionListView;
    private ListView pastPermissionListView;

    private List<Permission> permissionList = new ArrayList<>();
    private List<Permission> pastPermissionList = new ArrayList<>();

    private ModelManager modelManager;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);

        modelManager = ModelManager.getInstance();

        ProxyBuilder.SimpleCallback<List<Permission>> permissionCallback = permissionList -> getPendingPermissionList(permissionList);
        modelManager.getPendingPermissionForUser(CheckPermissionActivity.this, permissionCallback);

        ProxyBuilder.SimpleCallback<List<Permission>> pastPermissionCallback = pastPermissionList -> getPastPermissionList(pastPermissionList);
        modelManager.getPastPermissionForUser(CheckPermissionActivity.this, pastPermissionCallback);

        //setTimer();
    }

    private void populatePermissionList() {
        ArrayAdapter<Permission> adapter = new CheckPermissionActivity.permissionAdapter();
        //Configure ListView
        permissionListView = findViewById(R.id.jacky_permission_list);
        permissionListView.setAdapter(adapter);
    }

    private class permissionAdapter extends ArrayAdapter<Permission> {
        public permissionAdapter() {
            super(CheckPermissionActivity.this, R.layout.permission_list_layout, permissionList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Find a permission to add
            Permission permission = permissionList.get(position);

            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.permission_list_layout, parent, false);
            }

            TextView setMessage = itemView.findViewById(R.id.jacky_permission_message);
            setMessage.setText(permission.getMessage());

            TextView setStatus = itemView.findViewById(R.id.jacky_status_message);
            setStatus.setText(permission.getStatus());


            return itemView;
        }
    }

    private void registerPendingPermission()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_permission_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Permission permission = permissionList.get(position);

                fragmentToDisplayPendingPermission(permission);
            }
        });
    }

    private void populatePastPermissionList() {
        ArrayAdapter<Permission> adapter = new CheckPermissionActivity.pastPermissionAdapter();
        //Configure ListView
        pastPermissionListView = findViewById(R.id.jacky_past_permissions);
        pastPermissionListView.setAdapter(adapter);

    }

    private class pastPermissionAdapter extends ArrayAdapter<Permission> {
        public pastPermissionAdapter() {
            super(CheckPermissionActivity.this, R.layout.permission_list_layout, pastPermissionList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Find a permission to add
            Permission permission = pastPermissionList.get(position);

            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.permission_list_layout, parent, false);
            }

            TextView setMessage = itemView.findViewById(R.id.jacky_permission_message);
            setMessage.setText(permission.getMessage());

            TextView setStatus = itemView.findViewById(R.id.jacky_status_message);
            setStatus.setText(permission.getStatus());


            return itemView;
        }
    }

    private void registerPastPermissionItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_past_permissions);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Permission permission = pastPermissionList.get(position);

                Intent intent = ViewPermissionActivity.makeIntent(CheckPermissionActivity.this, permission.getId());
                startActivity(intent);
            }
        });
    }


    private void fragmentToDisplayPendingPermission(Permission permission)
    {
        FragmentManager manager = getSupportFragmentManager();
        viewPermissionFragment dialog = new viewPermissionFragment();

        // Supply index input as an argument.
        Bundle variables = new Bundle();
        variables.putString("MessagePermissionForUser", permission.getMessage());
        variables.putLong("PermissionID", permission.getId());

        dialog.setArguments(variables);
        dialog.show(manager, "MyApp");


    }

    private void getPendingPermissionList(List<Permission> permissions){
        permissionList.clear();
        permissionList = permissions;
        Log.i("MyApp", "The size is: " + permissionList.size());
        //setTimer();
        populatePermissionList();
        registerPendingPermission();
    }

    private void getPastPermissionList(List<Permission> permissions){
        pastPermissionList.clear();
        pastPermissionList = permissions;
        Log.i("MyApp", "The past list is: " + pastPermissionList.size());
        //setTimer();
        populatePastPermissionList();
        registerPastPermissionItemClick();
    }

    private void setTimer(){
        Log.i("MyApp", "In SetTimer");
        //wait 15 seconds and request for new messages -> from garry's code
        int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                ProxyBuilder.SimpleCallback<List<Permission>> permissionCallback = permissionList -> getPendingPermissionList(permissionList);
                modelManager.getPendingPermissionForUser(CheckPermissionActivity.this, permissionCallback);

                ProxyBuilder.SimpleCallback<List<Permission>> pastPermissionCallback = pastPermissionList -> getPastPermissionList(pastPermissionList);
                modelManager.getPastPermissionForUser(CheckPermissionActivity.this, pastPermissionCallback);


            }
        }, delay);

    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CheckPermissionActivity.class);
    }

}
