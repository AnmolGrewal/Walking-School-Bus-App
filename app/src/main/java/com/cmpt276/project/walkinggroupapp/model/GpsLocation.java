package com.cmpt276.project.walkinggroupapp.model;

import java.sql.Timestamp;

/**
 * Created by glang on 3/23/2018.
 */

public class GpsLocation {

    private double lat;
    private double lng;
    private Timestamp timestamp;


    public double getLat() {return lat;}

    public void setLat(double lat) {this.lat = lat;}

    public double getLng() {return lng;}

    public void setLng(double lng) {this.lng = lng;}

    public Timestamp getTimestamp() {return timestamp;}

    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}

}
