package com.getthere.guber;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Manav on 12/8/14.
 */
abstract class Transport {

    private String type;
    private String cost;
    private int distance;
    private int duration;
    private LatLng start;
    private LatLng dest;
    private Intent intent;

    Transport(LatLng start, LatLng dest, String type){
        this.start = start;
        this.dest = dest;
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getDuration() {
        return duration;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

}
