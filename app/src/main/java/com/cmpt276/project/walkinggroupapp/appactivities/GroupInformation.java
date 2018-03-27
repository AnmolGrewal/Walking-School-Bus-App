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
import android.widget.Button;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.GpsLocation;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.sql.Timestamp;


/**
 * Code for obtaining current location based on: https://www.youtube.com/watch?v=QNb_3QKSmMk
 *
 * Activity for viewing Information about a group and also uploading current location to server
 */
public class GroupInformation extends AppCompatActivity {
    private static final String TAG = "GroupInfoActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String GROUP_ID_INTENT_KEY = "group_id_intent_key";

    private ModelManager mModelManager;

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

    }

    private void setupButtons() {

        mStartUploadButton = findViewById(R.id.gerry_Upload_Location_Button_info);
        mStartUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start uploading user current location
                mIsUpload = true;
                getLastKnownLocation();
                Toast.makeText(GroupInformation.this, "Started Uploading", Toast.LENGTH_SHORT).show();
            }
        });

        mStopUploadButton = findViewById(R.id.gerry_Stop_Upload_Button_info);
        mStopUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop uploading location
                mIsUpload = false;
                Toast.makeText(GroupInformation.this, "Upload Stopped", Toast.LENGTH_SHORT).show();

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
            mModelManager.setLastGpsLocation(GroupInformation.this, setLastKnownLocationCallback, mModelManager.getPrivateFieldUser().getId(), currentLocation);
            Log.w(TAG, "getLocation()3");
        }

    }

    private void setLastKnownLocationResponse(GpsLocation gpsLocation) {
        //double check here to make sure the returned gpsLocation  was the same as the one you passed to server
        Log.w(TAG, "SET LOCATION SUCCESS, Received: " + gpsLocation.getLat() + " " + gpsLocation.getLng() + " " + gpsLocation.getTimestamp().toString());

        //check if destination is reached -> stop uploading location data
        if( (Math.abs(gpsLocation.getLat() - mGroupUploadingLat) <= 0.01) && (Math.abs(gpsLocation.getLng() - mGroupUploadingLng) <= 0.01) && (!mIsStoppingInTenMinutes) ) {
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
        mModelManager.getWalkingGroupById(GroupInformation.this, getGroupUploadingDestinationCallBack, mCurrentGroupId);
    }

    private void getGroupUploadingDestinationResponse(WalkingGroup walkingGroup) {

       mGroupUploadingLat = walkingGroup.getRouteLatArray()[1];
       mGroupUploadingLng = walkingGroup.getRouteLngArray()[1];

        Log.w(TAG, "GET GROUP DESTINATION SUCCESS, Received: " + mGroupUploadingLat + " " + mGroupUploadingLng);


    }


    private void stopUploadingInTenMinutes() {
        Log.w(TAG, "Destination Reached, stopping upload after 10 minutes");
        Toast.makeText(GroupInformation.this, "Destination Reached, stopping upload after 10 minutes", Toast.LENGTH_SHORT).show();
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



    //For creating intents outside of this Activity
    public static Intent makeIntent(Context context, long groupId){
        Intent intent = new Intent(context, GroupInformation.class);
        intent.putExtra(GROUP_ID_INTENT_KEY, groupId);
        return intent;
    }

    //for extracting intent extras from ViewGroup Activity
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        mCurrentGroupId = intent.getLongExtra(GROUP_ID_INTENT_KEY, 0);

    }



}
