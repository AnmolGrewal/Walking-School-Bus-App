package com.cmpt276.project.walkinggroupapp.appactivities;

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

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.List;

public class ParentDashboardActivity extends AppCompatActivity {

    private Button mViewMapButton;
    private ListView mChildrenListView;

    private List<User> mChildrenList;

    private ModelManager mModelManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        mModelManager = ModelManager.getInstance();

        //get the list of all children
        ProxyBuilder.SimpleCallback<List<User>> getMonitorsUsersCallback = serverChildrenList -> getChildrenListResponse(serverChildrenList);
        mModelManager.getMonitorsUsers(ParentDashboardActivity.this, getMonitorsUsersCallback);


        setupViewMapButton();
    }


    private void setupViewMapButton() {
        mViewMapButton = findViewById(R.id.gerry_ViewMap_Button_parent_dashboard);
        mViewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set property to false to let map know what to do
                mModelManager.getPrivateFieldUser().setIsViewingChild(true);

                //go to map activity
                Intent intent = new Intent(ParentDashboardActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });
    }

    private void getChildrenListResponse(List<User> monitorsUsers) {
        this.mChildrenList = monitorsUsers;
        populateChildrenList();
        registerChildrenListOnItemClick();
    }


    private void populateChildrenList() {
        ArrayAdapter<User> adapter = new childrenListAdapter();

        //Configure ListView
        mChildrenListView = findViewById(R.id.gerry_Children_List_parent_dashboard);
        mChildrenListView.setAdapter(adapter);
    }


    private class childrenListAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public childrenListAdapter() {
            super(ParentDashboardActivity.this, R.layout.list_layout, mChildrenList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }
            //Find a user to add
            User currentUser = mChildrenList.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(currentUser.getEmail());

            return itemView;
        }
    }

    private void registerChildrenListOnItemClick() {
        final ListView list = findViewById(R.id.gerry_Children_List_parent_dashboard);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the Id of the group that is clicked
                long UserId = mChildrenList.get(position).getId();

                // go to map activity-show the clicked user location

            }
        });


    }
}
