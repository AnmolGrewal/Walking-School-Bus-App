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
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;

import java.util.ArrayList;
import java.util.List;

public class AddOrViewGroup extends AppCompatActivity {

    private List<WalkingGroup> memberList = new ArrayList<>();
    private List<WalkingGroup> leaderList = new ArrayList<>();

    private ListView memberListView;
    private ListView leaderListView;

    private Button createBtn;

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_view_group);

        modelManager = ModelManager.getInstance();

        WalkingGroup tempGroup = new WalkingGroup();
        tempGroup.setGroupDescription("Hello This is test group");

        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);
        memberList.add(tempGroup);

        WalkingGroup currentGroup = memberList.get(0);
        Log.i("MyApp", "The group description is: " + currentGroup.getGroupDescription());
        leaderList.add(tempGroup);

        populateMemberList();
        populateLeaderList();

        //TODO
        //ProxyBuilder.SimpleCallback<List<WalkingGroup>> getMonitorsUsersCallback = monitorsUsers -> getMonitorsUsersResponse(monitorsUsers);
        //modelManager.getMonitorsUsers(AddOrViewGroup.this, getMonitorsUsersCallback);

        setUpCreateButton();
        registerLeaderListOnItemClick();
        registerMemberListOnItemClick();

    }

    @Override
    public void onResume() {
        super.onResume();

        //TODO
        //REDRAW
    }

    private void setUpCreateButton() {
        createBtn = findViewById(R.id.jacky_create_group_button);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateGroup.makeIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }


    private void populateMemberList() {
        ArrayAdapter<WalkingGroup> adapter = new AddOrViewGroup.memberListAdapter();
        //Configure ListView
        memberListView = findViewById(R.id.jacky_edit_user_member_list);
        memberListView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class memberListAdapter extends ArrayAdapter<WalkingGroup> {                                                 //Code for complexList based from Brian Frasers video
        public memberListAdapter() {
            super(AddOrViewGroup.this, R.layout.group_layout, memberList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.group_layout, parent, false);
            }
            //Find a group to add
            Log.i("MyApp", "Inside Monitoring By");
            WalkingGroup currentGroup = memberList.get(position);

            //Group Description
            TextView makeName = itemView.findViewById(R.id.jacky_group_description_dynamic);
            makeName.setText(currentGroup.getGroupDescription());


            return itemView;
        }
    }

    private void populateLeaderList() {
        ArrayAdapter<WalkingGroup> adapter = new AddOrViewGroup.LeaderAdapter();
        //Configure ListView
        leaderListView = findViewById(R.id.jacky_leader_list);
        leaderListView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class LeaderAdapter extends ArrayAdapter<WalkingGroup> {                                                 //Code for complexList based from Brian Frasers video
        public LeaderAdapter() {
            super(AddOrViewGroup.this, R.layout.group_layout, leaderList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.group_layout, parent, false);
            }
            //Find a group to add
            Log.i("MyApp", "Inside Monitoring By");
            WalkingGroup currentGroup = leaderList.get(position);

            //Group Description
            TextView makeName = itemView.findViewById(R.id.jacky_group_description_dynamic);
            makeName.setText(currentGroup.getGroupDescription());

            return itemView;
        }
    }

    private void registerMemberListOnItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_edit_user_member_list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
//                selectedPosition = position;
                PopupMenu popupMenu = new PopupMenu(AddOrViewGroup.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.cancel:
//                                doCancel();
                                break;
                            case R.id.delete:
                                removeUser(position);
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

    private void registerLeaderListOnItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_leader_list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
//                selectedPosition = position;
                PopupMenu popupMenu = new PopupMenu(AddOrViewGroup.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.cancel:
//                                doCancel();
                                break;
                            case R.id.delete:
                                removeUser(position);
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

    private void removeUser(int position){
        //Remove user
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddOrViewGroup.class);
    }
}
