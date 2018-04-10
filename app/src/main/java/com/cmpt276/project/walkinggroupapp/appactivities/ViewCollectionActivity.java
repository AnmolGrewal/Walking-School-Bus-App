package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

public class ViewCollectionActivity extends AppCompatActivity {

    private ImageButton dogIcon;
    private ImageButton catIcon;
    private ImageButton dinosaurIcon;
    private ImageButton dolphinIcon;
    private ImageButton penguinIcon;

    private ModelManager modelManager;

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection);

        modelManager = modelManager.getInstance();

        setupImageButtonIDs();
        setTransparency();

        ProxyBuilder.SimpleCallback<User> getUserInformationCallBack = returnedUser -> getUserInformationCallBack(returnedUser);
        modelManager.getUserById(ViewCollectionActivity.this, getUserInformationCallBack, modelManager.getLocalUserId());
    }

    private void getUserInformationCallBack(User returnedUser) {
        currentUser = returnedUser;
        //Toast test = Toast.makeText(ViewCollectionActivity.this, "Derp + "+currentUser.getOwnedAvatars().toString(), Toast.LENGTH_SHORT);
        //test.show();
    }

    private void setTransparency() {
        dogIcon.setAlpha(0.2f);
        catIcon.setAlpha(0.2f);
        dinosaurIcon.setAlpha(0.2f);
        dolphinIcon.setAlpha(0.2f);
        penguinIcon.setAlpha(0.2f);
    }

    private void setupImageButtonIDs() {
        dogIcon = findViewById(R.id.anmol_dogIcon);
        catIcon = findViewById(R.id.anmol_catIcon);
        dinosaurIcon = findViewById(R.id.anmol_dinosaurIcon);
        dolphinIcon = findViewById(R.id.anmol_dolphinIcon);
        penguinIcon = findViewById(R.id.anmol_penguinIcon);
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, ViewCollectionActivity.class);
    }
}
