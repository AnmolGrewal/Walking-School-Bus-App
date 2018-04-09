package com.cmpt276.project.walkinggroupapp.appactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmpt276.project.walkinggroupapp.R;


/**
 * activity for viewing all unlocked avatar and also for setting the users current avatar
 */
public class ViewCollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection);
    }
}
