package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    private Integer currentSelectedAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection);

        modelManager = modelManager.getInstance();

        setupImageButtonIDs();
        setupInitialImages();
        setTransparency();

        ProxyBuilder.SimpleCallback<User> getUserInformationCallBack = returnedUser -> getUserInformationCallBack(returnedUser);
        modelManager.getUserById(ViewCollectionActivity.this, getUserInformationCallBack, modelManager.getLocalUserId());

        setupOnImageClick();

    }

    private void selectedAvatar(User currentAvatar) {
        Toast avatarText = Toast.makeText(ViewCollectionActivity.this, "Avatar Changed", Toast.LENGTH_SHORT);
        avatarText.show();
    }

    private void setupOnImageClick() {
        dogIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTransparency();
                dogIcon.setAlpha(1f);
                currentSelectedAvatar = R.drawable.dog_icon;
                changeAvatarCall();
            }
        });

        catIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTransparency();
                catIcon.setAlpha(1f);
                currentSelectedAvatar = R.drawable.cat_icon;
                changeAvatarCall();
            }
        });

        dinosaurIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTransparency();
                dinosaurIcon.setAlpha(1f);
                currentSelectedAvatar = R.drawable.dinosaur_icon;
                changeAvatarCall();
            }
        });

        dolphinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTransparency();
                dolphinIcon.setAlpha(1f);
                currentSelectedAvatar = R.drawable.dolphin_icon;
                changeAvatarCall();
            }
        });

        penguinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTransparency();
                penguinIcon.setAlpha(1f);
                currentSelectedAvatar = R.drawable.penguin_icon;
                changeAvatarCall();
            }
        });
    }

    private void changeAvatarCall() {
        ProxyBuilder.SimpleCallback<User> selectedAvatar = currentAvatar -> selectedAvatar(currentAvatar);
        modelManager.changeAvatar(ViewCollectionActivity.this, selectedAvatar, currentSelectedAvatar);
    }

    private void setupInitialImages() {
        dogIcon.setVisibility(View.INVISIBLE);
        catIcon.setVisibility(View.INVISIBLE);
        dinosaurIcon.setVisibility(View.INVISIBLE);
        dolphinIcon.setVisibility(View.INVISIBLE);
        penguinIcon.setVisibility(View.INVISIBLE);
    }

    private void getUserInformationCallBack(User returnedUser) {
        currentUser = returnedUser;
        //Toast test = Toast.makeText(ViewCollectionActivity.this, "Derp + "+currentUser.getOwnedAvatars().toString(), Toast.LENGTH_SHORT);
        //test.show();
        setupImages();
    }

    private void setupImages() {
        if(currentUser.getOwnedAvatars().contains(R.drawable.dinosaur_icon))
        {
            dinosaurIcon.setVisibility(View.VISIBLE);
            dinosaurIcon.setAlpha(0.2f);
        } else {
            dinosaurIcon.setVisibility(View.INVISIBLE);
        }

        if(currentUser.getOwnedAvatars().contains(R.drawable.dog_icon))
        {
            dogIcon.setVisibility(View.VISIBLE);
            dogIcon.setAlpha(0.2f);
        } else {
            dogIcon.setVisibility(View.INVISIBLE);
        }

        if(currentUser.getOwnedAvatars().contains(R.drawable.cat_icon))
        {
            catIcon.setVisibility(View.VISIBLE);
            catIcon.setAlpha(0.2f);
        } else {
            catIcon.setVisibility(View.INVISIBLE);
        }

        if(currentUser.getOwnedAvatars().contains(R.drawable.dolphin_icon))
        {
            dolphinIcon.setVisibility(View.VISIBLE);
            dolphinIcon.setAlpha(0.2f);
        } else {
            dolphinIcon.setVisibility(View.INVISIBLE);
        }

        if(currentUser.getOwnedAvatars().contains(R.drawable.penguin_icon))
        {
            penguinIcon.setVisibility(View.VISIBLE);
            penguinIcon.setAlpha(0.2f);
        } else {
            penguinIcon.setVisibility(View.INVISIBLE);
        }


        /*Set Current Avatar*/
        if(R.drawable.dinosaur_icon == currentUser.getCurrentAvatar()) {
            setTransparency();
            dinosaurIcon.setAlpha(1f);
        } else if (R.drawable.dog_icon == currentUser.getCurrentAvatar()) {
            setTransparency();
            dogIcon.setAlpha(1f);
        } else if (R.drawable.cat_icon == currentUser.getCurrentAvatar()) {
            setTransparency();
            catIcon.setAlpha(1f);
        } else if (R.drawable.dolphin_icon == currentUser.getCurrentAvatar()) {
            setTransparency();
            dolphinIcon.setAlpha(1f);
        } else if (R.drawable.penguin_icon == currentUser.getCurrentAvatar()) {
            setTransparency();
            penguinIcon.setAlpha(1f);
        }
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
