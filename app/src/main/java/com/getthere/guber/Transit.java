package com.getthere.guber;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aznerik on 12/8/2014.
 */
public class Transit extends Transport{

    Transit(LatLng start, LatLng dest, ListAdapter adapter){
        super(start,dest, "Public Transit");
        super.setIntent(new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + start.latitude + ","
                        + start.longitude + "&daddr=" + dest.latitude + "," + dest.longitude
                        + "&dirflg=transit")));
        GoogleMapsFetchTimeTask timeTask = new GoogleMapsFetchTimeTask(this, adapter);
        timeTask.execute("transit");
    }

    @Override
    public String getCost(){
        return "$0";
    }

}
