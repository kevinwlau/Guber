package com.getthere.guber;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Manav on 12/8/14.
 */
public class Uber extends Transport {

    private int timeToArrive;

    Uber(LatLng start, LatLng dest){
        super(start, dest);
        UberFetchCostTask costTask = new UberFetchCostTask(this);
        UberFetchTimeTask timeTask = new UberFetchTimeTask(this);
        GoogleMapsFetchTimeTask driveTimeTask = new GoogleMapsFetchTimeTask(this);
        costTask.execute();
        timeTask.execute();
        driveTimeTask.execute("driving");
    }

    public int getDuration() {
        return this.getDuration() + timeToArrive;
    }
    public int getTimeToArrive() {
        return timeToArrive;
    }
    public void setTimeToArrive(int timeToArrive) {
        this.timeToArrive = timeToArrive;
    }

}
