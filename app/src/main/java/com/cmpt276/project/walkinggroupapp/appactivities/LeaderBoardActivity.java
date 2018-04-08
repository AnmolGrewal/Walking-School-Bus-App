package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;

public class LeaderBoardActivity extends AppCompatActivity {

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        setupList();
    }

    private void setupList() {
        
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, LeaderBoardActivity.class);
    }
}
