

package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static java.lang.Long.parseLong;


/**
 * Map based on tutorials :
 * https://www.raywenderlich.com/144066/introduction-google-maps-api-android
 * https://www.youtube.com/playlist?list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt
 * and files provided by Professor Brian Fraser
 * *Activity for joining and viewing walking groups in google map
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {
    private static final String TAG = "MapActivity";
    private static final int DEFAULT_ZOOM = 13;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    public static final String USER_ID_FORCE_CHILD = "user_id_force_child";
    public static final String USER_ID_VIEW_CHILD = "user_id_view_child";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;

    private Button mJoinGroupButton;

    private ModelManager mModelManager;
    private List<WalkingGroup> mWalkingGroups;
    private long mClickedGroupId;

    private User mCurrentUser;

    private long mChildUserIdToForce;
    private long mChildUserIdToView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //ask for permission
        askPermission();

        mModelManager = ModelManager.getInstance();

        extractDataFromIntent();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializes google api client if its null
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        mJoinGroupButton = findViewById(R.id.gerry_Join_Group_Button_map);

        //createLocationRequest();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("App", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> onMapReady");
        mMap = googleMap;

        //add zoom buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //allows clicking on marker to show its title
        mMap.setOnMarkerClickListener(this);

        //allow multi line statements on title and snippet of markers
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });



        //test location of all child
        if(mModelManager.getPrivateFieldUser().isViewingAllChild()) {
            placeChildLocationMarker(new LatLng(37.28, -122.17));
            placeChildLocationMarker(new LatLng(37.3, -122.17));
            placeChildLocationMarker(new LatLng(37.28, -122.2));
        }

        //test location of a child
        if(mModelManager.getPrivateFieldUser().isViewingAChild()) {
            placeChildLocationMarker(new LatLng(37.30, -122.19));
        }


        //parent forcing child to join group or User wanting to join group
        if(mModelManager.getPrivateFieldUser().isParent() || mModelManager.getPrivateFieldUser().isJoining()) {


            //Get the existing walking groups in server
            populateMapWithGroups();


            //Create Listeners for the markers
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {


                    // if it is not the current location marker--has to be after mCurrentLocation has been initialized
                    if ( (!(marker.getSnippet().equals("Current Location"))) && (!(marker.getSnippet().equals("Child Current Location"))) ) {

                        //get the Id of marker in long format
                        String stringID = String.valueOf(marker.getTag());
                        mClickedGroupId = parseLong(stringID);


                        //show Join button
                        mJoinGroupButton.setVisibility(View.VISIBLE);


                    } else {
                        //hide Join Button
                        mJoinGroupButton.setVisibility(View.INVISIBLE);
                    }


                    return false;
                }
            });

            //create map listener
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    //place marker
                    //placeWalkingGroupMarker(latLng);

                    //hide Join Button
                    mJoinGroupButton.setVisibility(View.INVISIBLE);

                }
            });


            //create on camera move listener
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    //hide Join Button
                    mJoinGroupButton.setVisibility(View.INVISIBLE);
                }
            });


            //Join Button Listener
            mJoinGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //"Child" wanting to join group by him/herself
                    if (mModelManager.getPrivateFieldUser().isJoining()) {
                        //Add the group to users list of walking group
                        mCurrentUser = mModelManager.getPrivateFieldUser();

                        //get the walking group which is associated with marker--set it to mClickedGroup
                        //then add user to be part of that group
                        ProxyBuilder.SimpleCallback<List<User>> addUserToGroupCallback = serverPassedWalkingGroup -> addUserToClickedGroupResponse(serverPassedWalkingGroup);
                        mModelManager.addUserToGroup(MapActivity.this, addUserToGroupCallback, mClickedGroupId, mCurrentUser.getId());
                    }
                    //"Parent" requesting for his/her child
                    else if (mModelManager.getPrivateFieldUser().isParent()){
                        //set mCurrentUser to be the User with the UserId passed by the parent
                        ProxyBuilder.SimpleCallback<User> getUserByIdCallback = serverPassedUser -> getUserByIdResponse(serverPassedUser);
                        mModelManager.getUserById(MapActivity.this, getUserByIdCallback, mChildUserIdToForce);


                    }

                }
            });

        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //ask for permission, get current location and zoom to it
        setUpMap();



        if (mLocationUpdateState) {
            //startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (null != mLastLocation) {
            //placeCurrentLocationMarker(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Initiates a background connection of the client to Google Play services.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Closes the connection to Google Play services if the client is not null and is connected
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }


    //start the update request if it has a RESULT_OK result for a REQUEST_CHECK_SETTINGS request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                mLocationUpdateState = true;
                //startLocationUpdates();
            }
        }
    }

    //stop location update request
    @Override
    protected void onPause() {
        super.onPause();
       //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    //to restart the location update request.
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mLocationUpdateState) {
            //startLocationUpdates();
        }
    }



    private void askPermission() {
        //Ask for USer Permission if USer has no ACCESS_FINE_LOCATION Permission
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);


        }

    }


    //handle the results of permissions
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

    private void setUpMap() {

        //Ask for USer Permission if USer has no ACCESS_FINE_LOCATION Permission
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        //get Users last known location and zoom to it
        mMap.setMyLocationEnabled(true);
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            //determines the availability of location data on the device
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //gives you the most recent location currently available
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                placeCurrentLocationMarker(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
            }
        }
    }


    //place a marker with non default icon
    private void placeWalkingGroupMarker(WalkingGroup walkingGroup) {
        //extract location of group
        double latitude = walkingGroup.getRouteLatArray()[0];
        double longitude = walkingGroup.getRouteLngArray()[0];
        LatLng location = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions().position(location)
                                                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_location));
        String titleStr = getAddressOfMarker(location);
        markerOptions.title(titleStr);
        markerOptions.snippet(walkingGroup.getGroupDescription());
        Marker theMarker = mMap.addMarker(markerOptions);

        //set the tag of marker to be the groups id--use this to differentiate marker during marker click event
        theMarker.setTag(walkingGroup.getId());
    }

    //place a marker with default icon--Users current location
    private void placeCurrentLocationMarker(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        String titleStr = getAddressOfMarker(location);
        markerOptions.title(titleStr);
        markerOptions.snippet("Current Location");

        mMap.addMarker(markerOptions);
    }

    //place marker for each of the child current walking group location
    private void placeChildLocationMarker(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        String titleStr = getAddressOfMarker(location);
        markerOptions.title(titleStr);
        markerOptions.snippet("Child Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        Marker theMarker = mMap.addMarker(markerOptions);
        //set the tag of marker to be the user id--use this to differentiate marker during marker click event
        theMarker.setTag("test");
    }



    //set the title of marker to be the address it is located
    private String getAddressOfMarker(LatLng latLng ) {
        //Creates a geoCoder object to turn a latitude and longitude coordinate into an address and vice versa
        Geocoder geocoder = new Geocoder( this );
        String addressText = "";
        List<Address> addresses = null;
        Address address = null;
        try {
            //Asks the geoCoder to get the address from the location passed to the method.
            addresses = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 );
            //If the response contains any address, then append it to a string and return.
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
                }
            }
        } catch (IOException e ) {
        }
        return addressText;
    }


    protected void startLocationUpdates() {
        //In startLocationUpdates(), if the ACCESS_FINE_LOCATION permission has not been granted, request it now and return.
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        //If there is permission, request for location updates.
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                this);
    }

    //You create an instance of LocationRequest, add it to an instance of LocationSettingsRequest.
    //Builder and retrieve and handle any changes to be made based on the current state of the user’s location settings.
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        //specifies the rate at which your app will like to receive updates.
        mLocationRequest.setInterval(10000);
        //specifies the fastest rate at which the app can handle updates.
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    //A SUCCESS status means all is well and you can go ahead and initiate a location request.
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationUpdateState = true;
                        startLocationUpdates();
                        break;
                    //A RESOLUTION_REQUIRED status means the location settings have some issues which can be fixed.
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    // A SETTINGS_CHANGE_UNAVAILABLE status means the location settings have some issues that you can’t fix.
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void getWalkingGroupsResponse(List<WalkingGroup> passedWalkingGroups) {

        this.mWalkingGroups = passedWalkingGroups;

        //Populate the map with all the markers
        for(int i = 0; i < mWalkingGroups.size(); i++ ) {
            WalkingGroup ithGroup = mWalkingGroups.get(i);
            placeWalkingGroupMarker(ithGroup);
        }
    }

    private void addUserToClickedGroupResponse(List<User> passedGroup) {
        //already added user to the group

        //go back to previous activity
        finish();
    }


    private void getUserByIdResponse(User passedUser) {
        mCurrentUser = passedUser;

        //add the group to the child
        //get the walking group which is associated with marker--set it to mClickedGroup
        //then add user to be part of that group
        ProxyBuilder.SimpleCallback<List<User>> addUserToGroupCallback = serverPassedWalkingGroup -> addUserToClickedGroupResponse(serverPassedWalkingGroup);
        mModelManager.addUserToGroup(MapActivity.this, addUserToGroupCallback, mClickedGroupId, mCurrentUser.getId());
    }


    private void populateMapWithGroups () {
        ProxyBuilder.SimpleCallback<List<WalkingGroup>>  getWalkingGroupsCallback = serverPassedWalkingGroups -> getWalkingGroupsResponse(serverPassedWalkingGroups);
        mModelManager.getAllWalkingGroups(MapActivity.this, getWalkingGroupsCallback);
    }










    //For creating intents outside of this Activity
    public static Intent makeIntentForceChild(Context context, long editUserId){
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(USER_ID_FORCE_CHILD, editUserId);
        return intent;
    }

    public static Intent makeIntentViewChild(Context context, long editUserId){
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(USER_ID_VIEW_CHILD, editUserId);
        return intent;
    }




    //for extracting intent extras made from other Activities--only do this when parent forcing child
    private void extractDataFromIntent() {

        if(mModelManager.getPrivateFieldUser().isParent()) {

            Intent intent = getIntent();
            mChildUserIdToForce = intent.getLongExtra(USER_ID_FORCE_CHILD, 0);
        }
        else if(mModelManager.getPrivateFieldUser().isViewingAChild()) {

            Intent intent = getIntent();
            mChildUserIdToView = intent.getLongExtra(USER_ID_VIEW_CHILD, 0);

        }

    }




}


