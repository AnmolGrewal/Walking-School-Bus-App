package com.cmpt276.project.walkinggroupapp.appactivities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cmpt276.project.walkinggroupapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Simple test app to show a Google Map.
 * - If using the emulator, Create an Emulator from the API 26 image.
 *   (API27's doesn't/didn't support maps; nor will 24 or before I believe).
 * - Accessing Google Maps requires an API key: You can request one for free (and should!)
 *   see /res/values/google_maps_api.xml
 * - More notes at the end of this file.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("App", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> onMapReady");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}


/*
    Google API
    - RUN ON ANDROID API 26! (27 does not show maps, 24 does not include support).

        - Install "Google APIs Intel x86 Atom System Image"
        Tools -> Android -> SDK Manager
        Check "Show Package Details" (bottom right)
        Expand selected SDK level, check "Google APIs Intel x86 Atom System Image", install

        - Create AVD (Android Virtual Device; i.e., an emulator) targeting new "Google APIs Intel x86 Atom System Image"

        - In AVD, install "Fake GPS Location" app to fake locations
        AVD: Set as mock location app:
        - Phone Settings -> About
        - Keep taping build number to unlock developer options
        - Settings -> Developer Options -> Select mock location app
        Run app, click start; leave running.


        - Add dependencies:
        File -> Project Structure -> app (on left); Dependencies tab
        Add:
        com.google.android.gms:play-services-location:11.8.0
        com.google.android.gms:play-services-maps:11.8.0

        - App
        - Verify Play services in onResume()
        - Add location permissions



        ///////////////////////////////////////////////////////////////
        Maps
        ///////////////////////////////////////////////////////////////
        - Run on emulator at API 26
        - Get Google Maps API key:
        developers.google.com/maps/documentation/android/start
        * Don't create a new project, just add:
        Right click on package, New -> Activity -> Gallerey: select Google Maps Activity


        ERROR: Maps Android API: Google Maps Android API v2 only supports devices with OpenGL ES 2.0 and above
        - Check OpenGL Version via a hardware info app. If OpenGL ES 1.0, Run on API 26 (not 27!)

*/