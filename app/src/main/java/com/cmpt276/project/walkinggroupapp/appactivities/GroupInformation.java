package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.GpsLocation;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

public class GroupInformation extends AppCompatActivity  {
    private static final String TAG = "GroupInfoActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private ModelManager mModelManager;

    private Button mStartUploadButton;
    private Button mStopUploadButton;

    private GpsLocation mObtainedServerLocation;

    private boolean mIsUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);

        mModelManager = ModelManager.getInstance();

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
            }
        });

        mStopUploadButton = findViewById(R.id.gerry_Stop_Upload_Button_info);
        mStopUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop uploading location
                mIsUpload = false;

            }
        });
    }


    private void getLastKnownLocation() {
        GpsLocation currentLocation = new GpsLocation(); //= new GpsLocation(lat,lng,timestamp);

        Log.w(TAG, "getLocation()4");
        ProxyBuilder.SimpleCallback<GpsLocation> setLastKnownLocationCallback = serverPassedLocation ->setLastKnownLocationResponse(serverPassedLocation);
        mModelManager.setLastGpsLocation(GroupInformation.this, setLastKnownLocationCallback, mModelManager.getPrivateFieldUser().getId(), currentLocation );

    }


    private void setLastKnownLocationResponse(GpsLocation gpsLocation) {
        //can double check here to make sure the returned gpsLocation  was the same as the one you passed to server


        Log.w(TAG, "SET LOCATION SUCCESS");

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

}
