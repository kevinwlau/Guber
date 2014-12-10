package com.getthere.guber;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aznerik on 12/8/2014.
 */
public class Transit extends Transport{

    Transit(LatLng start, LatLng dest){
        super(start,dest, "Public Transit");
        GoogleMapsFetchTimeTask timeTask = new GoogleMapsFetchTimeTask(this);
        timeTask.execute("transit");
    }

}
