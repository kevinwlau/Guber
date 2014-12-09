package com.getthere.guber;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Manav on 12/8/14.
 */
abstract class Transport {
    String cost;
    int duration;
    LatLng start;
    LatLng dest;

    Transport(LatLng start, LatLng dest){
        this.start = start;
        this.dest = dest;
    }

}
