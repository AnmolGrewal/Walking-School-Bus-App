package com.cmpt276.project.walkinggroupapp.appactivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.GpsLocation;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Code for obtaining current location based on: https://www.youtube.com/watch?v=QNb_3QKSmMk
 *
 * Activity for viewing Information about a group and also uploading current location to server
 */
public class GroupInformationActivity extends AppCompatActivity {
    private static final String TAG = "GroupInfoActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String GROUP_ID_INTENT_KEY = "group_id_intent_key";
    private static  final float DESTINATION_DISTANCE_TOLERANCE = 40;// distance in meters to destination to  consider destination as reached


    private ListView mMemberOfGroupListView;

    private List<User> mMemberOfGroup = new ArrayList<>();

    private ModelManager mModelManager;

    private TextView mGroupDescription;

    private LocationManager mLocationManager;

    private Button mStartUploadButton;
    private Button mStopUploadButton;

    private boolean mIsUpload;

    private Double mLat;
    private Double mLng;

    private double mGroupUploadingLat;
    private double mGroupUploadingLng;

    private boolean mIsStoppingInTenMinutes = false;

    private long mCurrentGroupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);

        extractDataFromIntent();

        mModelManager = ModelManager.getInstance();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setupLocationListener();

        //get the destination of the User currently uploading location data
        getGroupUploadingDestination();

        setupButtons();

        ProxyBuilder.SimpleCallback<WalkingGroup> getDetailsOfWalkingGroupCallback = walkingGroup -> getDetailsOfWalkingGroupResponse(walkingGroup);
        mModelManager.getWalkingGroupById(GroupInformationActivity.this, getDetailsOfWalkingGroupCallback, mCurrentGroupId);

        ProxyBuilder.SimpleCallback<List<User>> getIdsOfUserInGroupCallback = UserIdsList -> getIdsOfUserInGroupResponse(UserIdsList);
        mModelManager.getAllMemberUsersByGroupId(GroupInformationActivity.this, getIdsOfUserInGroupCallback, mCurrentGroupId);

    }

    private void setupButtons() {

        mStartUploadButton = findViewById(R.id.gerry_Upload_Location_Button_info);
        mStartUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start uploading user current location
                mIsUpload = true;
                getLastKnownLocation();
                Toast.makeText(GroupInformationActivity.this, "Started Uploading", Toast.LENGTH_SHORT).show();
            }
        });

        mStopUploadButton = findViewById(R.id.gerry_Stop_Upload_Button_info);
        mStopUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop uploading location
                mIsUpload = false;
                Toast.makeText(GroupInformationActivity.this, "Upload Stopped", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void setupLocationListener() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLat = location.getLatitude();
                mLng = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //let user go to settings to enable gps if it is disabled
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };


        //request for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        //get location update every 10 seconds
        mLocationManager.requestLocationUpdates("gps", 10000, 0, locationListener);

    }

    //handle the results of permission request pop-up
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                //if accept button clicked
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //finish this activity and reload it
                    finish();
                    startActivity(getIntent());
                }
                //deny button clicked
                else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void getLastKnownLocation() {
        if(mIsUpload) {
            Log.w(TAG, "getLocation()1");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            GpsLocation currentLocation;

            if (mLat != null && mLng != null) {
                currentLocation = new GpsLocation(mLat, mLng, timestamp);
            } else {
                currentLocation = new GpsLocation(0.0, 0.0, timestamp);
            }
            Log.w(TAG, "getLocation()2");
            ProxyBuilder.SimpleCallback<GpsLocation> setLastKnownLocationCallback = serverPassedLocation -> setLastKnownLocationResponse(serverPassedLocation);
            mModelManager.setLastGpsLocation(GroupInformationActivity.this, setLastKnownLocationCallback, mModelManager.getPrivateFieldUser().getId(), currentLocation);
            Log.w(TAG, "getLocation()3");
        }

    }

    private void setLastKnownLocationResponse(GpsLocation gpsLocation) {
        //double check here to make sure the returned gpsLocation  was the same as the one you passed to server
        Log.w(TAG, "SET LOCATION SUCCESS, Received: " + gpsLocation.getLat() + " " + gpsLocation.getLng() + " " + gpsLocation.getTimestamp().toString());

        //current location
        Location currentLocation = new Location("currentLocation");
        currentLocation.setLatitude(mGroupUploadingLat);
        currentLocation.setLongitude(mGroupUploadingLng);

        //new location
        Location newLocation = new Location("newLocation");
        newLocation.setLatitude(gpsLocation.getLat());
        newLocation.setLongitude(gpsLocation.getLng());

        //check if destination is reached -> stop uploading location data
        if(currentLocation.distanceTo(newLocation) <= DESTINATION_DISTANCE_TOLERANCE ) {
            stopUploadingInTenMinutes();

            //So only 1 stop command is done once destination is reached
            mIsStoppingInTenMinutes = true;
        }


        //wait 30 seconds and request for current location again
        Handler handler = new Handler();
        int delay = 30000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something after 30 sec
                getLastKnownLocation();
            }
        }, delay);

    }



    private void getGroupUploadingDestination() {
        ProxyBuilder.SimpleCallback<WalkingGroup> getGroupUploadingDestinationCallBack = serverPassedWalkingGroup-> getGroupUploadingDestinationResponse(serverPassedWalkingGroup);
        mModelManager.getWalkingGroupById(GroupInformationActivity.this, getGroupUploadingDestinationCallBack, mCurrentGroupId);
    }

    private void getGroupUploadingDestinationResponse(WalkingGroup walkingGroup) {

       mGroupUploadingLat = walkingGroup.getRouteLatArray()[1];
       mGroupUploadingLng = walkingGroup.getRouteLngArray()[1];

        Log.w(TAG, "GET GROUP DESTINATION SUCCESS, Received: " + mGroupUploadingLat + " " + mGroupUploadingLng);


    }


    private void stopUploadingInTenMinutes() {
        Log.w(TAG, "Destination Reached, stopping upload after 10 minutes");
        Toast.makeText(GroupInformationActivity.this, "Destination Reached, stopping upload after 10 minutes", Toast.LENGTH_SHORT).show();
        //wait 10 minutes then stop uploading location data
        Handler handler = new Handler();
        int delay = 600000; //milliseconds,

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something after 10 minutes
                mIsUpload = false;
                Log.w(TAG, "Stop Upload After 10 Minutes");
            }
        }, delay);

    }

    private void populateMemberOfGroupsList() {
        ArrayAdapter<User> adapter = new GroupInformationActivity.memberOfGroupAdapter();

        //Configure ListView
        mMemberOfGroupListView = findViewById(R.id.jacky_user_of_group);
        mMemberOfGroupListView.setAdapter(adapter);
    }

    private class memberOfGroupAdapter extends ArrayAdapter<User> {                                                 //Code for complexList based from Brian Frasers video
        public memberOfGroupAdapter() {
            super(GroupInformationActivity.this, R.layout.list_layout, mMemberOfGroup);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_layout, parent, false);
            }
            //Find a user to add
            User currentUser = mMemberOfGroup.get(position);

            //Name:
            TextView makeName = itemView.findViewById(R.id.jacky_user_name_dynamic);
            makeName.setText(currentUser.getName());

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_user_email_dynamic);
            makeEmail.setText(currentUser.getEmail());

            return itemView;
        }
    }

    private void registerViewUserOnItemClick()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_user_of_group);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to EditProfileActivity Activity
                Log.i("MyApp", "Id being send it is: " + mMemberOfGroup.get(position).getId());
                Intent intent = ViewProfileActivity.makeIntent(GroupInformationActivity.this, mMemberOfGroup.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void getIdsOfUserInGroupResponse(List<User> listOfIds){
        for(User user: listOfIds){
            ProxyBuilder.SimpleCallback<User> getUserCallback = userDetail -> getUserDetail(userDetail);
            mModelManager.getUserById(GroupInformationActivity.this, getUserCallback, user.getId());
        }
    }


    private void getUserDetail(User user){
        Log.i("MyApp" , "User id is: " + user.getId());
        mMemberOfGroup.add(user);
        populateMemberOfGroupsList();
        registerViewUserOnItemClick();
    }

    private void getDetailsOfWalkingGroupResponse(WalkingGroup walkingGroup){
        mGroupDescription = findViewById(R.id.jacky_group_description_info);
        mGroupDescription.setText(walkingGroup.getGroupDescription());
    }


    //For creating intents outside of this Activity
    public static Intent makeIntent(Context context, long groupId){
        Intent intent = new Intent(context, GroupInformationActivity.class);
        intent.putExtra(GROUP_ID_INTENT_KEY, groupId);
        return intent;
    }

    //for extracting intent extras from ViewGroup Activity
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        mCurrentGroupId = intent.getLongExtra(GROUP_ID_INTENT_KEY, 0);

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mIsUpload = false;
        Toast.makeText(GroupInformationActivity.this, "Upload Stopped", Toast.LENGTH_SHORT).show();
        finish();
    }

}
