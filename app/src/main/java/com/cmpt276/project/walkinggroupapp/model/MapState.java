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



    //enum for a map state
    public enum CurrentStateEnum {
        IsSelectingStartLocation, IsSelectingEndLocation, IsParent, IsJoining, IsViewingAllChild, IsViewingAChild
    }



    /////Variables

    //variables for storing selected location for start and end destination when creating a group
    private LatLng mSelectedStartLocation;
    private LatLng mSelectedEndLocation;

    //variable for storing current map state
    private CurrentStateEnum mCurrentStateEnum;




    /////Getters and Setters
    public LatLng getSelectedStartLocation() { return mSelectedStartLocation; }
    public void setSelectedStartLocation(LatLng selectedStartLocation) { mSelectedStartLocation = selectedStartLocation; }

    public LatLng getSelectedEndLocation() { return mSelectedEndLocation; }
    public void setSelectedEndLocation(LatLng selectedEndLocation) { mSelectedEndLocation = selectedEndLocation; }

    public CurrentStateEnum getCurrentStateEnum() { return mCurrentStateEnum; }

    public void setCurrentStateEnum(CurrentStateEnum currentStateEnum) { mCurrentStateEnum = currentStateEnum; }




}
