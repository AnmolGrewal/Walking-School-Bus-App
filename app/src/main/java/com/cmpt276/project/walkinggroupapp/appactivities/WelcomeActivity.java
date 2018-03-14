package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cmpt276.project.walkinggroupapp.R;
//ASSETS http://wyomingpublicmedia.org/post/jackson-school-program-encourages-walking-school Bus
//http://diysolarpanelsv.com/clipart-horizontal-road.html Background
//http://psql.me/post/20081828750/good-afternoon-tumblr-how-is-your-day Sun
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "UserClicks";
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
                    mainMenu();
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
                mainMenu();
            }
        });
    }

    private void mainMenu() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    //Makes it if you press back button on activity screen it exits app Source: AS3
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.i("DATA", "Hit Actionbar Back Button");
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
