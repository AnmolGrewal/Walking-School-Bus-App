package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.Avatar;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Activity for buying avatar for the user using points earned from walking with group
 */
public class ShopActivity extends AppCompatActivity {


    private ModelManager mModelManager;
    private User mCurrentUser;

    private List<Avatar> mAvatarList = new ArrayList<Avatar>();
    private List<Avatar> mNotOwnedAvatars = new ArrayList<Avatar>();

    private Button mViewCollectionButton;
    private ListView mShopListview;
    private TextView mNameTextView;
    private TextView mLevelTextView;
    private TextView mCurrentPointsTextView;
    private ProgressBar mLevelProgressBar;
    private ImageView mCurrentAvatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mModelManager = ModelManager.getInstance();

        //get the updated user from server first before setting up buttons/list views etc.
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
        setupViewCollectionButton();
        setupNameTextView();
        setupCurrentPointsTextView();
        setupLevelTextViewAndProgress();
        setupCurrentAvatarImageView();

        populateAvatarList();

        getNotOwnedAvatars();

        populateAvatarShopListView();

    }


    private void populateAvatarList() {
        //get all the list of current avatars in drawable--set their price(all 500 for now)
        mAvatarList.add(new Avatar(R.drawable.dog_icon,500));
        mAvatarList.add(new Avatar(R.drawable.cat_icon,500));
        mAvatarList.add(new Avatar(R.drawable.dinosaur_icon,500));
        mAvatarList.add(new Avatar(R.drawable.dolphin_icon,500));
        mAvatarList.add(new Avatar(R.drawable.penguin_icon,500));


    }

    private void getNotOwnedAvatars() {
        List<Integer> userOwnedAvatars = mCurrentUser.getOwnedAvatars();

        for(int i = 0; i < mAvatarList.size(); i++) {
            boolean matchFound = false;
            for(int j = 0; j < userOwnedAvatars.size(); j++){
                //compare to find an avatar that is still not owned by user
                if(mAvatarList.get(i).getId() == userOwnedAvatars.get(j)) {
                    matchFound = true;
                }
            }

            //no match found, ith avatar from AvatarList is still not owned by user
            if(!matchFound) {
                mNotOwnedAvatars.add(mAvatarList.get(i));
            }
        }
    }

    private void populateAvatarShopListView () {
        ArrayAdapter<Avatar> adapter = new ShopActivity.MyListAdapter();

        ListView list = findViewById(R.id.gerry_Shop_ListView_shop);
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<Avatar> {
        public MyListAdapter() {
            super(ShopActivity.this, R.layout.shop_layout, mNotOwnedAvatars);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view
            View itemView = convertView;
            if(itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.shop_layout, parent, false);
            }

            //find avatar to work with
            Avatar currentAvatar = mNotOwnedAvatars.get(position);

            //fill the view
            ImageView imageView = itemView.findViewById(R.id.gerry_Avatar_ImageView_shop_layout);
            int id = currentAvatar.getId();
            imageView.setImageResource(id);


            //show the price
            TextView textView = itemView.findViewById(R.id.gerry_Price_TextView_shop_layout);
            String priceString = "Price: " + currentAvatar.getPrice();
            textView.setText(priceString);

            return itemView;
        }

    }









    private void setupViewCollectionButton() {

        mViewCollectionButton = findViewById(R.id.gerry_ViewCollection_Button_shop);
        mViewCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to viewCollection Activity
                Intent intent = new Intent(ShopActivity.this, ViewCollectionActivity.class);
                startActivity(intent);
            }
        });

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

        //show the users current level
        //each level is 500 points
        int currentLevelInt = totalPoints / 500;   // java "/" user integer division which throw away remainder
        String levelString = "Level " + currentLevelInt;
        mLevelTextView.setText(levelString);

        //show the percentage progress in the progress bar
        int remainderCurrentLevelInt = totalPoints % 500;
        Double remainderCurrentLevelDouble = (double)(remainderCurrentLevelInt);
        Double progressPercent = (remainderCurrentLevelDouble/500) * 100;
        mLevelProgressBar.setProgress(progressPercent.intValue());

    }


    private void setupCurrentAvatarImageView() {
        mCurrentAvatarImageView = findViewById(R.id.gerry_Avatar_ImageView_shop);

        //if user has default
        if(mCurrentUser.getCurrentAvatar() == 0) {
            mCurrentAvatarImageView.setImageResource(R.drawable.temp_pic);
        }
        else {
            mCurrentAvatarImageView.setImageResource(mCurrentUser.getCurrentAvatar());
        }


    }


    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }


}
