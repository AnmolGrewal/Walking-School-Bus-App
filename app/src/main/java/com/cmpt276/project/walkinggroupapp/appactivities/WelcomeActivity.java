package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.cmpt276.project.walkinggroupapp.R;


/**
 * Class for registering a new User
 *
 * ASSETS http://wyomingpublicmedia.org/post/jackson-school-program-encourages-walking-school Bus
 *http://diysolarpanelsv.com/clipart-horizontal-road.html Background
 *http://psql.me/post/20081828750/good-afternoon-tumblr-how-is-your-day Sun
 * */

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    int counter = 0;
    private ImageButton skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Animation lasts about 2seconds so 4seconds after
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 6s = 6000ms
                //Which is launch the Main Menu Activity
                if(counter == 0) {
                    goToLoginActivity();
                }
            }
        }, 4000);

        skipButton = findViewById(R.id.anmol_imageButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to Login Activity
                Log.i(TAG, "Skip Pressed");
                counter = 1;
                goToLoginActivity();
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = LoginActivity.makeIntent(getApplicationContext());
        startActivity(intent);
    }

}
