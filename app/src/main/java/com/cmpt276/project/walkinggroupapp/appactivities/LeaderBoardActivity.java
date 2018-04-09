package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    private List<User> allUsers;
    private ListView leaderboardList;

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        setupList();
    }

    private void setupList() {
        modelManager = ModelManager.getInstance();
        ProxyBuilder.SimpleCallback<List<User>> getSortedListCallback = listLeaderboard -> getSortedPoints(listLeaderboard);
        modelManager.getAllUsers(LeaderBoardActivity.this, getSortedListCallback);
    }

    private void getSortedPoints(List<User> listLeaderboard) {
        allUsers = listLeaderboard;
        //Based on Brian Fraser's Video for List
        populateLeaderboardList();
    }

    private void populateLeaderboardList() {
        ArrayAdapter<User> adapter = new leaderboardList();
        //Configure ListView
        leaderboardList = findViewById(R.id.anmol_listDisplay);
        leaderboardList.setAdapter(adapter);
    }

    private class leaderboardList extends ArrayAdapter<User> {
        public leaderboardList() {
            super(LeaderBoardActivity.this, R.layout.leaderboard_layout, allUsers);
            }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.leaderboard_layout, parent, false);
            }
            //Find a user to add
            User currentUser = allUsers.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Points
            TextView setEmailToPoints = itemView.findViewById(R.id.anmol_totalPointsUser);
            setEmailToPoints.setText(""+currentUser.getTotalPointsEarned());

            //Icon
            ImageView setImage = itemView.findViewById(R.id.jacky_user_icon);
            if(position == 0){
                setImage.setImageResource(R.drawable.goldcup);
            }else if(position == 1){
                setImage.setImageResource(R.drawable.silvercup);
            }else if(position == 2){
                setImage.setImageResource(R.drawable.bronzecup);
            }else {
                setImage.setImageResource(R.drawable.dolphin_icon);
                setImage.setVisibility(View.INVISIBLE);
            }
            return itemView;
        }
        }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, LeaderBoardActivity.class);
    }
}
