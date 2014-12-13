package com.getthere.guber;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class Drive extends Transport {

    Drive(LatLng start, LatLng dest, ListAdapter adapter){
        super(start, dest, "Driving");
        super.setIntent(new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + start.latitude + ","
                        + start.longitude + "&daddr=" + dest.latitude + "," + dest.longitude
                        + "&dirflg=driving")));
        GoogleMapsFetchTimeTask driveTask = new GoogleMapsFetchTimeTask(this, adapter);
        driveTask.execute("driving");
    }

    @Override
    public String getCost() { return "$0"; }

}
