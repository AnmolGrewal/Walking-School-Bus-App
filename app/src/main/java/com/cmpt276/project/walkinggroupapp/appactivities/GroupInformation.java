package com.cmpt276.project.walkinggroupapp.appactivities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.GpsLocation;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
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

    private ModelManager mModelManager;

    private LocationManager mLocationManager;

    private Button mStartUploadButton;
    private Button mStopUploadButton;

    private boolean mIsUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);

        mModelManager = ModelManager.getInstance();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setupLocationListener();

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

    private void setupLocationListener() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

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

    private void getLastKnownLocation() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        GpsLocation currentLocation = new GpsLocation(12.0,12.0,timestamp);

        Log.w(TAG, "getLocation()4");
        ProxyBuilder.SimpleCallback<GpsLocation> setLastKnownLocationCallback = serverPassedLocation ->setLastKnownLocationResponse(serverPassedLocation);
        mModelManager.setLastGpsLocation(GroupInformation.this, setLastKnownLocationCallback, mModelManager.getPrivateFieldUser().getId(), currentLocation );

    }


    private void setLastKnownLocationResponse(GpsLocation gpsLocation) {
        //double check here to make sure the returned gpsLocation  was the same as the one you passed to server
        Log.w(TAG, "SET LOCATION SUCCESS, Recieved: " + gpsLocation.getLat() + " " + gpsLocation.getLng() + " " + gpsLocation.getTimestamp().toString());

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
