package com.getthere.guber;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kevinlau on 12/9/2014.
 */
public class Car2Go extends Transport {

    private LatLng location;

    Car2Go(LatLng start, LatLng dest){
        super(start,dest);

    }

    public void setLocation(LatLng location){
        this.location = location;
    }

}
