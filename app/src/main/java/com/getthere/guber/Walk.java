package com.getthere.guber;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aznerik on 12/8/2014.
 */
public class Walk extends Transport {

    String timeToArrive;

    Walk(LatLng start, LatLng dest){
        super(start, dest);
        WalkDurationAPITask timeTask = new WalkDurationAPITask(this);
        timeTask.execute();
    }

    public String getCost() { return "0"; }

    public int getDuration() { return duration; }
}
