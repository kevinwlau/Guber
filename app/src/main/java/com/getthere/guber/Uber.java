package com.getthere.guber;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Manav on 12/8/14.
 */
public class Uber extends Transport {

    int timeToArrive;

    Uber(LatLng start, LatLng dest){
        super(start, dest);
        UberFetchCostTask costTask = new UberFetchCostTask(this);
        UberFetchTimeTask timeTask = new UberFetchTimeTask(this);
        GoogleMapsFetchTimeTask driveTimeTask = new GoogleMapsFetchTimeTask(this);
        costTask.execute();
        timeTask.execute();
        driveTimeTask.execute("driving");
    }

    public String getCost() {
        return cost;
    }

    public int getDuration() {
        return duration + timeToArrive;
    }

}
