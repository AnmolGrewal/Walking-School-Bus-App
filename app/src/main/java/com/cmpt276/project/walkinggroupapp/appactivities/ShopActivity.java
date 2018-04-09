package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

/**
 *
 * Activity for buying avatar for the user using points earned from walking with group
 */
public class ShopActivity extends AppCompatActivity {


    private ModelManager mModelManager;
    private User mCurrentUser;

    private Button mViewCollectionButton;
    private ListView mShopListview;
    private TextView mNameTextView;
    private TextView mLevelTextView;
    private TextView mCurrentPointsTextView;
    private ProgressBar mLevelProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mModelManager = ModelManager.getInstance();

        //get the updated user from server first before setting up buttons/text views etc.
        getUpdatedUser();

    }



    private void getUpdatedUser() {

        long currentUserId = mModelManager.getLocalUserId();

        //get current user
        ProxyBuilder.SimpleCallback<User> getUserCallback = serverPassedUser -> getUpdatedUserResponse(serverPassedUser);
        mModelManager.getUserById(ShopActivity.this, getUserCallback, currentUserId);
    }

    private void getUpdatedUserResponse(User passedUser) {
        mCurrentUser = passedUser;

        //setup views/buttons after updated user obtained
        setupShopListView();
        setupViewCollectionButton();
        setupNameTextView();
        setupCurrentPointsTextView();
        setupLevelTextViewAndProgress();
    }





    private void setupViewCollectionButton() {

        mViewCollectionButton = findViewById(R.id.gerry_ViewCollection_Button_shop);
        mViewCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to viewCollection Activity
                Intent intent = new Intent(ShopActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

    }


    private void setupShopListView() {

    }


    private void setupNameTextView() {
        mNameTextView = findViewById(R.id.gerry_Name_TextView_shop);
        mNameTextView.setText(mCurrentUser.getName());
    }


    private void setupCurrentPointsTextView() {
        mCurrentPointsTextView = findViewById(R.id.gerry_CurrentPoints_TextView_shop);
        String currentPointsString = "Current Points: " + mCurrentUser.getCurrentPoints().toString();
        mCurrentPointsTextView.setText(currentPointsString);
    }


    private void setupLevelTextViewAndProgress() {
        mLevelTextView = findViewById(R.id.gerry_Level_TextView_shop);
        mLevelProgressBar= findViewById(R.id.gerry_Level_ProgressBar_shop);


        int totalPoints = mCurrentUser.getTotalPointsEarned();

        double currentLevel = totalPoints / 500;
        int displayCurrentLevel =  (int)currentLevel + 1;
        String levelString = "Level " + displayCurrentLevel;
        mLevelTextView.setText(levelString);
        double levelBarDisplay = currentLevel % 500;
        int percentageLevel = (int)(levelBarDisplay % 100);
        mLevelProgressBar.setProgress(percentageLevel);

    }


    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }


}
