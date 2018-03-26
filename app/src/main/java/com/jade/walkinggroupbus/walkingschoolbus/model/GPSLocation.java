package com.jade.walkinggroupbus.walkingschoolbus.model;


import java.util.Date;

public class GPSLocation {

    Double lat;
    Double lng;
    Date timestamp;

    public GPSLocation() {
        lat = null;
        lng = null;
        timestamp = null;
    }

    public Double[] getLocation() {
        Double[] Location = {lat,lng};
        return Location;
    }

    public void setLocation(Double[] newLocation) {
        if (newLocation.length >= 2) {
            lat = newLocation[0];
            lng = newLocation[1];
        }
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double newLat) {
        lat = newLat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double newLng) {
        lng = newLng;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date newTimestamp) {
        timestamp = newTimestamp;
    }
}
