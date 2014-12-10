package com.getthere.guber;

import com.google.android.gms.maps.model.LatLng;

public class Drive extends Transport {

    Drive(LatLng start, LatLng dest){
        super(start, dest, "Driving");
        GoogleMapsFetchTimeTask driveTask = new GoogleMapsFetchTimeTask(this);
        driveTask.execute("driving");
    }

    public String getCost() { return "0"; }

}
