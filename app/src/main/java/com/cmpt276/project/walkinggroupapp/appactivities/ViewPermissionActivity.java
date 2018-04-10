package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.Authorizors;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.Permission;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.ArrayList;
import java.util.List;

public class ViewPermissionActivity extends AppCompatActivity {
    public static final String PERMISSION_ID = "UserID";

    private long permissionId;

    private ModelManager modelManager;

    private Permission permission;
    private List<Authorizors> authorizorsList = new ArrayList<>();
    private List<User> authorizedUser = new ArrayList<>();

    private TextView statusText;
    private TextView descriptionText;

    private ListView authorizedListView;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_permission);

        modelManager = ModelManager.getInstance();

        getExtra();

        ProxyBuilder.SimpleCallback<Permission> getPermissionByIdCallback = returnPermission -> setupViews(returnPermission);
        modelManager.getPermissionById(ViewPermissionActivity.this, getPermissionByIdCallback, permissionId);
    }

    private void getExtra() {
        Intent intent = getIntent();
        permissionId = intent.getLongExtra(PERMISSION_ID, -1);
    }

    private void setupViews(Permission returnPermission) {
        permission = returnPermission;
        authorizorsList.clear();
        authorizedUser.clear();
        authorizorsList = permission.getAuthorizors();
        setupText();

        for(Authorizors authorizors: authorizorsList){
            ProxyBuilder.SimpleCallback<User> getUserByIdCallback = returnedUser -> getUserData(returnedUser);
            modelManager.getUserById(ViewPermissionActivity.this, getUserByIdCallback, authorizors.getWhoApprovedOrDenied().getId());
        }
    }

    private void getUserData(User user){
        authorizedUser.add(user);
        count++;
        int j = 0;
        long compareA;
        long compareB;
        if(count == authorizorsList.size()){
            count = 0;
            for(int i = 0; i < authorizorsList.size(); i++){
                compareA = authorizorsList.get(i).getWhoApprovedOrDenied().getId();
                compareB = authorizedUser.get(j).getId();
                while(compareA != compareB){
                    j++;
                    compareB = authorizedUser.get(j).getId();
                }
                authorizorsList.get(i).setWhoApprovedOrDenied(authorizedUser.get(j));
                j=0;
            }
            populateAuthorizedList();
        }
    }

    private void setupText(){
        statusText = findViewById(R.id.justin_txtStatus2);
        statusText.setText(permission.getStatus());

        descriptionText = findViewById(R.id.justin_txtMessage2);
        descriptionText.setText(permission.getMessage());

    }

    private void populateAuthorizedList() {
        ArrayAdapter<Authorizors> adapter = new ViewPermissionActivity.authorizedUserAdapter();
        //Configure ListView
        authorizedListView = findViewById(R.id.jacky_authorized_list);
        authorizedListView.setAdapter(adapter);

    }

    private class authorizedUserAdapter extends ArrayAdapter<Authorizors> {
        public authorizedUserAdapter() {
            super(ViewPermissionActivity.this, R.layout.view_past_permission_layout, authorizorsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Find a permission to add
            Authorizors authorizors = authorizorsList.get(position);

            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.view_past_permission_layout, parent, false);
            }

            Log.i("MyApp", "The User Id we have is " + authorizors.getWhoApprovedOrDenied().getId());

            TextView setEmail = itemView.findViewById(R.id.jacky_permission_email);
            setEmail.setText(authorizors.getWhoApprovedOrDenied().getEmail());

            TextView setName = itemView.findViewById(R.id.jacky_dynamic_name_permission);
            setName.setText(authorizors.getWhoApprovedOrDenied().getName());

            TextView setUserId = itemView.findViewById(R.id.jacky_userid_dynamic_permissions);
            setUserId.setText(Long.toString(authorizors.getWhoApprovedOrDenied().getId()));

            TextView setStatusGiven = itemView.findViewById(R.id.jacky_user_permission_status);
            setStatusGiven.setText(authorizors.getStatus());

            return itemView;
        }
    }

    public static Intent makeIntent(Context context, Long userId){
        Intent intent = new Intent(context, ViewPermissionActivity.class);
        intent.putExtra(PERMISSION_ID, userId);
        return intent;
    }
}
