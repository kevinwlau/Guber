package com.getthere.guber;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aznerik on 12/8/2014.
 */
public class Walk extends Transport {

    Walk(LatLng start, LatLng dest, ListAdapter adapter){
        super(start, dest, "Walking");
        super.setIntent(new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + start.latitude + ","
                        + start.longitude + "&daddr=" + dest.latitude + "," + dest.longitude
                        + "&dirflg=walking")));
        GoogleMapsFetchTimeTask timeTask = new GoogleMapsFetchTimeTask(this, adapter);
        timeTask.execute("walking");
    }

    @Override
    public String getCost() { return "$0"; }

}
