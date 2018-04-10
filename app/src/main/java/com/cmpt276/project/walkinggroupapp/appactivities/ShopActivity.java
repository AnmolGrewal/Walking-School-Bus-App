package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private ModelManager modelManager;

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
        registerShopListViewItemClick();

    }


    private void populateAvatarList() {
        //get all the list of current avatars in drawable--set their price(all 500 for now)
        mAvatarList.add(new Avatar(R.drawable.dog_icon,100));
        mAvatarList.add(new Avatar(R.drawable.cat_icon,100));
        mAvatarList.add(new Avatar(R.drawable.dinosaur_icon,100));
        mAvatarList.add(new Avatar(R.drawable.dolphin_icon,500));
        mAvatarList.add(new Avatar(R.drawable.penguin_icon,500));


    }

    private void getNotOwnedAvatars() {
        List<Integer> userOwnedAvatars = mCurrentUser.getOwnedAvatars();

        boolean ownedAllAvatar = true;

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
                ownedAllAvatar = false;
                mNotOwnedAvatars.add(mAvatarList.get(i));
            }
        }

        if(ownedAllAvatar) {
            Toast.makeText(ShopActivity.this, "All Icons Already Owned", Toast.LENGTH_SHORT).show();
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
            //show the icon
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

    //when icons on the shop are clicked -- let user buy them
    private void registerShopListViewItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.gerry_Shop_ListView_shop);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                // allow user to buy the icon
                PopupMenu popupMenu = new PopupMenu(ShopActivity.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_shop, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId())
                        {
                            case R.id.gerry_Buy_popup_shop:
                                //buy icon then refresh the entire activity
                                //check first if user has enough points
                                if( mCurrentUser.getCurrentPoints() >= mNotOwnedAvatars.get(position).getPrice())
                                {
                                    //user has enough points
                                    addNewUserOwnedAvatar(mNotOwnedAvatars.get(position).getId(), mNotOwnedAvatars.get(position).getPrice());
                                }
                                else {
                                    Toast.makeText(ShopActivity.this, "Not Enough Points", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case R.id.gerry_Cancel_popup_shop:
                                //cancel clicked, do nothing
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();

            }
        });

    }




    private void addNewUserOwnedAvatar(int avatarId, int avatarPrice) {

        ProxyBuilder.SimpleCallback<User> addNewUserOwnedAvatarCallback = serverPassedUser -> addNewUserOwnedAvatarResponse(serverPassedUser);
        mModelManager.buyAvatar(ShopActivity.this,addNewUserOwnedAvatarCallback,avatarId,avatarPrice);
    }

    private void addNewUserOwnedAvatarResponse(User passedUser) {

        Toast.makeText(ShopActivity.this, "Icon bought", Toast.LENGTH_SHORT).show();
        restartActivity();
    }


    private void setupViewCollectionButton() {

        mViewCollectionButton = findViewById(R.id.gerry_ViewCollection_Button_shop);
        mViewCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to viewCollection Activity
                Intent intent = ViewCollectionActivity.makeIntent(ShopActivity.this);
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
    //Change Avatar
    @Override
    protected void onResume() {
        super.onResume();
        modelManager = ModelManager.getInstance();
        ProxyBuilder.SimpleCallback<User> getUserInformationCallBack = returnedUser -> getUserInformationCallBack(returnedUser);
        modelManager.getUserById(ShopActivity.this, getUserInformationCallBack, modelManager.getLocalUserId());
    }

    private void getUserInformationCallBack(User returnedUser) {
        mCurrentAvatarImageView = findViewById(R.id.gerry_Avatar_ImageView_shop);
        if(returnedUser.getCurrentAvatar() != null && returnedUser.getCurrentAvatar() != 0) {
            mCurrentAvatarImageView.setImageResource(returnedUser.getCurrentAvatar());
        } else {
            mCurrentAvatarImageView.setImageResource(R.drawable.temp_pic);
        }
    }
}
