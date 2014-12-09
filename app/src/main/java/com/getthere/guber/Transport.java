package com.getthere.guber;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Manav on 12/8/14.
 */
abstract class Transport {

    private String cost;
    private int duration;
    private LatLng start;
    private LatLng dest;

    Transport(LatLng start, LatLng dest){
        this.start = start;
        this.dest = dest;
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
}
