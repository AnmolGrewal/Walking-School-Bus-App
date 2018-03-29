package com.cmpt276.project.walkinggroupapp.model;


import com.google.android.gms.maps.model.LatLng;

/**
 *
 * Singleton class for managing the different state of the map and extracting location data from map
 */
public class MapState {

    //Singleton Support
    private static MapState instance = null;

    public static MapState getInstance() {
        if (instance == null) {
            instance = new MapState();
        }
        return instance;
    }


    //variables
    private boolean mIsSelectingStartLocation = false;
    private boolean mIsSelectingEndLocation = false;


    private LatLng mSelectedStartLocation;
    private LatLng mSelectedEndLocation;





    //Getters and Setters
    public boolean getIsSelectingStartLocation() { return mIsSelectingStartLocation; }

    public void setIsSelectingStartLocation(boolean selectingLocation) { mIsSelectingStartLocation = selectingLocation; }


    public boolean getIsSelectingEndLocation() { return mIsSelectingEndLocation; }

    public void setIsSelectingEndLocation(boolean selectingEndLocation) { mIsSelectingEndLocation = selectingEndLocation; }


    public LatLng getSelectedStartLocation() { return mSelectedStartLocation; }

    public void setSelectedStartLocation(LatLng selectedStartLocation) { mSelectedStartLocation = selectedStartLocation; }


    public LatLng getSelectedEndLocation() { return mSelectedEndLocation; }

    public void setSelectedEndLocation(LatLng selectedEndLocation) { mSelectedEndLocation = selectedEndLocation; }





}
