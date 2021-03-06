package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
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
import com.cmpt276.project.walkinggroupapp.fragments.sendMessageFragment;
import com.cmpt276.project.walkinggroupapp.model.MapState;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/******
 *  Notice the complex List Adapter is based off from https://www.youtube.com/watch?v=WRANgDgM2Zg
 *  a video provided by the prof for assignment 2
 *  Code is adapted from Jacky.T  Assignment 2
 */

public class ViewGroupActivity extends AppCompatActivity {

    private List<WalkingGroup> memberOfGroups = new ArrayList<>();
    private List<WalkingGroup> leadsGroups = new ArrayList<>();

    private ListView groupsYouAreMemberOfListView;
    private ListView groupsYouAreLeadingListView;

    private Button createBtn;
    private Button joinBtn;

    private ModelManager modelManager;
    private MapState mapState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_view_group);

        modelManager = ModelManager.getInstance();

        mapState = MapState.getInstance();


        ProxyBuilder.SimpleCallback<List<Long>> getIdsOfGroupsYouAreLeadingCallback = groupIdsList -> getIdsOfGroupsYouAreLeadingResponse(groupIdsList);
        modelManager.getIdsOfGroupsYouAreLeading(ViewGroupActivity.this, getIdsOfGroupsYouAreLeadingCallback);

        ProxyBuilder.SimpleCallback<List<Long>> getIdsOfGroupsYouAreMemberOfCallback = groupIdsList -> getIdsOfGroupsYouAreMemberOfResponse(groupIdsList);
        modelManager.getIdsOfGroupsYouAreMemberOf(ViewGroupActivity.this, getIdsOfGroupsYouAreMemberOfCallback);

//        populateLeaderList();
//        populateMemberList();

//        registerLeaderListClick();
//        registerMemberListClick();

        setupCreateButton();
        setupJoinButton();

    }

    private void getIdsOfGroupsYouAreLeadingResponse(List<Long> groupIdsList) {
        for (Long groupId: groupIdsList) {
            ProxyBuilder.SimpleCallback<WalkingGroup> callback = returnedGroup -> getLeadsGroupResponse(returnedGroup);
            modelManager.getWalkingGroupById(ViewGroupActivity.this, callback, groupId);
        }
    }

    private void getLeadsGroupResponse(WalkingGroup returnedGroup) {
        for (WalkingGroup group: leadsGroups) {
            if (Objects.equals(group.getId(), returnedGroup.getId())) {
                return;
            }
        }
        leadsGroups.add(returnedGroup);
        populateLeaderList();
        registerLeaderListClick();
    }

    private void getIdsOfGroupsYouAreMemberOfResponse(List<Long> groupIdsList) {
        memberOfGroups.clear();
        for (Long groupId: groupIdsList) {
            ProxyBuilder.SimpleCallback<WalkingGroup> callback = returnedGroup -> getMemberOfGroupResponse(returnedGroup);
            modelManager.getWalkingGroupById(ViewGroupActivity.this, callback, groupId);
        }
    }

    private void getMemberOfGroupResponse(WalkingGroup returnedGroup) {
        for (WalkingGroup group: memberOfGroups) {
            if (Objects.equals(group.getId(), returnedGroup.getId())) {
                return;
            }
        }
        memberOfGroups.add(returnedGroup);
        populateMemberList();
        registerMemberListClick();
    }

    @Override
    public void onResume() {
        super.onResume();

        ProxyBuilder.SimpleCallback<List<Long>> getIdsOfGroupsYouAreLeadingCallback = groupIdsList -> getIdsOfGroupsYouAreLeadingResponse(groupIdsList);
        modelManager.getIdsOfGroupsYouAreLeading(ViewGroupActivity.this, getIdsOfGroupsYouAreLeadingCallback);

        ProxyBuilder.SimpleCallback<List<Long>> getIdsOfGroupsYouAreMemberOfCallback = groupIdsList -> getIdsOfGroupsYouAreMemberOfResponse(groupIdsList);
        modelManager.getIdsOfGroupsYouAreMemberOf(ViewGroupActivity.this, getIdsOfGroupsYouAreMemberOfCallback);
    }

    private void setupCreateButton() {
        createBtn = findViewById(R.id.jacky_create_group_button);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateGroupActivity.makeIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void setupJoinButton(){
        joinBtn = findViewById(R.id.jacky_join_group_button);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Parent not forcing child to join groups
                //set properties to let map know what to do
                //set to let map know what to do
                MapState.CurrentStateEnum currentState = MapState.CurrentStateEnum.IsJoining;
                mapState.setCurrentStateEnum(currentState);

                //go to map activity
                Intent intentMap = new Intent(ViewGroupActivity.this, MapActivity.class);
                startActivity(intentMap);

            }
        });
    }


    private void populateMemberList() {
        ArrayAdapter<WalkingGroup> adapter = new ViewGroupActivity.memberListAdapter();
        //Configure ListView
        groupsYouAreMemberOfListView = findViewById(R.id.jacky_edit_user_member_list);
        groupsYouAreMemberOfListView.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class memberListAdapter extends ArrayAdapter<WalkingGroup> {                                                 //Code for complexList based from Brian Frasers video
        public memberListAdapter() {
            super(ViewGroupActivity.this, R.layout.group_layout, memberOfGroups);
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
            WalkingGroup currentGroup = memberOfGroups.get(position);

            //Group Description
            TextView makeName = itemView.findViewById(R.id.jacky_group_description_dynamic);
            makeName.setText(currentGroup.getGroupDescription());


            return itemView;
        }
    }

    private void populateLeaderList() {
        ArrayAdapter<WalkingGroup> adapter = new ViewGroupActivity.LeaderAdapter();
        //Configure ListView
        groupsYouAreLeadingListView = findViewById(R.id.jacky_leader_list);
        groupsYouAreLeadingListView.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(), "Done Populating List", Toast.LENGTH_LONG).show();
    }

    private class LeaderAdapter extends ArrayAdapter<WalkingGroup> {                                                 //Code for complexList based from Brian Frasers video
        public LeaderAdapter() {
            super(ViewGroupActivity.this, R.layout.group_layout, leadsGroups);
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
            WalkingGroup currentGroup = leadsGroups.get(position);

            //Group Description
            TextView makeName = itemView.findViewById(R.id.jacky_group_description_dynamic);
            makeName.setText(currentGroup.getGroupDescription());

            return itemView;
        }
    }

    private void registerMemberListClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_edit_user_member_list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
//                selectedPosition = position;
                PopupMenu popupMenu = new PopupMenu(ViewGroupActivity.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_member_group, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.cancel:
//                                doCancel();
                                break;
                            case R.id.leaveGroup:
                                leaveGroupYouAreMemberOf(position);
                                break;
                        }
                        return true;
                    }

                });

                popupMenu.show();
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the Id of the group that is clicked
                long groupId = memberOfGroups.get(position).getId();

                // go to GroupInfo Activity
                Intent intent = GroupInformationActivity.makeIntent(ViewGroupActivity.this, groupId);
                startActivity(intent);
            }
        });
    }

    private void registerLeaderListClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_leader_list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
//                selectedPosition = position;
                PopupMenu popupMenu = new PopupMenu(ViewGroupActivity.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_lead_group_options, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                // TODO: delete the group here.
                                break;
                            case R.id.jacky_send_message_to_group:
                                doSend(position);
                                break;
                        }
                        return true;
                    }

                });

                popupMenu.show();
                return true;
            }

        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the Id of the group that is clicked
                long groupId = leadsGroups.get(position).getId();

                // go to GroupInfo Activity
                Intent intent = GroupInformationActivity.makeIntent(ViewGroupActivity.this, groupId);
                startActivity(intent);
            }
        });

    }

    private void leaveGroupYouAreMemberOf(int position){
        //Remove user
        long groupId = memberOfGroups.get(position).getId();

        ProxyBuilder.SimpleCallback<List<User>> callback = returnedMembersList -> leaveGroupResponse(returnedMembersList);
        modelManager.leaveGroup(ViewGroupActivity.this, callback, groupId);
    }

    private void leaveGroupResponse(List<User> returnedMembersList) {
        memberOfGroups.clear();
        ProxyBuilder.SimpleCallback<List<Long>> getIdsOfGroupsYouAreMemberOfCallback = groupIdsList -> getIdsOfGroupsYouAreMemberOfResponse(groupIdsList);
        modelManager.getIdsOfGroupsYouAreMemberOf(ViewGroupActivity.this, getIdsOfGroupsYouAreMemberOfCallback);
    }

    private void doSend(int position){
        WalkingGroup sendGroup = leadsGroups.get(position);
        FragmentManager manager = getSupportFragmentManager();
        sendMessageFragment dialog = new sendMessageFragment();

        // Supply index input as an argument.
        Bundle variables = new Bundle();
        variables.putBoolean("isToGroup", true);
        variables.putLong("GroupIdToSendMessage", sendGroup.getId());

        dialog.setArguments(variables);
        dialog.show(manager, "SendView");
    }


    public static Intent makeIntent(Context context){
        return new Intent(context, ViewGroupActivity.class);
    }
}
