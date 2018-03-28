package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;

public class ParentDashboard extends AppCompatActivity {

    private Button mViewMapButton;
    private Button editProfileUser;
    private ModelManager mModelManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        mModelManager = ModelManager.getInstance();

        setupViewMapButton();
        setupEditUser();
    }

    private void setupEditUser() {
        editProfileUser = findViewById(R.id.anmol_editOwnProfile);
        editProfileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditOwnProfile.makeIntent(ParentDashboard.this);
                startActivity(intent);
            }
        });
    }


    private void setupViewMapButton() {
        mViewMapButton = findViewById(R.id.gerry_ViewMap_Button_parent_dashboard);
        mViewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set properties to let map know what to do
                User user =  mModelManager.getPrivateFieldUser();
                user.setIsViewingAChild(false);
                user.setIsJoining(false);
                user.setIsViewingAllChild(true);
                user.setIsParent(false);

                //go to map activity
                Intent intent = new Intent(ParentDashboard.this, MapActivity.class);
                startActivity(intent);

            }
        });
    }
}
