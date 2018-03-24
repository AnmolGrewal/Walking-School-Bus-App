package com.cmpt276.project.walkinggroupapp.model;

import java.sql.Timestamp;

/**
 * Created by glang on 3/23/2018.
 */

public class GpsLocation {

    private Double lat;
    private Double lng;
    private Timestamp timestamp;


    public Double getLat() {return lat;}

    public void setLat(Double lat) {this.lat = lat;}

    public Double getLng() {return lng;}

    public void setLng(Double lng) {this.lng = lng;}

    public Timestamp getTimestamp() {return timestamp;}

    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}

}
