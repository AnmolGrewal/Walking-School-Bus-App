package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
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
import com.cmpt276.project.walkinggroupapp.fragments.viewMessageFragment;
import com.cmpt276.project.walkinggroupapp.fragments.viewPermissionFragment;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.Permission;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.ArrayList;
import java.util.List;

public class CheckPermissionActivity extends AppCompatActivity {

    ListView permissionListView;
    List<Permission> permissionList = new ArrayList<>();
    ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);

        modelManager = ModelManager.getInstance();

        ProxyBuilder.SimpleCallback<List<Permission>> permissionCallback = PermissionList -> getPermissionList(PermissionList);
        modelManager.getPendingPermissionForUser(CheckPermissionActivity.this, permissionCallback);
    }

    private void populateMessageList() {
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


            return itemView;
        }
    }

    private void registerReadMessage()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_permission_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Permission permission = permissionList.get(position);

                respondToPermissions(permission);
            }
        });
    }

    private void respondToPermissions(Permission permission)
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

    private void getPermissionList(List<Permission> permissions){
        permissionList.clear();
        permissionList = permissions;
        Log.i("MyApp", "The size is: " + permissionList.size());
        populateMessageList();
        registerReadMessage();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CheckPermissionActivity.class);
    }
}
