package com.getthere.guber;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kevinlau on 12/9/2014.
 */
public class Car2Go extends Transport {

    private LatLng location;

    Car2Go(LatLng start, LatLng dest){
        super(start,dest);
        //fetch location of closest car
        Car2GoFetchCarTask nearestCar = new Car2GoFetchCarTask(this);
        nearestCar.execute();
        //get walking time to car
        //
    }

    public void setLocation(LatLng location){
        this.location = location;
    }
    public LatLng getLocation() {
        return location;
    }

}
