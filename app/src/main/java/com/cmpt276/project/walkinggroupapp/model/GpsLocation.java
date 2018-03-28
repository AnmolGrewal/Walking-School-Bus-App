package com.cmpt276.project.walkinggroupapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;

/**
 * Created by glang on 3/23/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GpsLocation {

    private Double lat;
    private Double lng;
    private Timestamp timestamp;

    //Default constructor--needed to prevent a certain server error
    public GpsLocation() {

    }

    public GpsLocation(Double lat, Double lng, Timestamp timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }


    public Double getLat() {return lat;}

    public void setLat(Double lat) {this.lat = lat;}

    public Double getLng() {return lng;}

    public void setLng(Double lng) {this.lng = lng;}

    public Timestamp getTimestamp() {return timestamp;}

    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}

}
