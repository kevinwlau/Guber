package com.getthere.guber;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Manav on 12/8/14.
 */
public class Uber extends Transport {

    private int timeToArrive;
    private String surge;

    Uber(LatLng start, LatLng dest){
        super(start, dest, "Uber");
        super.setIntent(new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("uber://?action=setPickup&product_id=91901472-f30d-4614-8ba7-9fcc937cebf5&pickup[latitude]="
                        + start.latitude + "&pickup[longitude]=" + start.longitude + "&dropoff[latitude]="
                        + dest.latitude + "&dropoff[longitude]=" + dest.longitude)));
        UberFetchCostTask costTask = new UberFetchCostTask(this);
        UberFetchTimeTask timeTask = new UberFetchTimeTask(this);
        GoogleMapsFetchTimeTask driveTimeTask = new GoogleMapsFetchTimeTask(this);
        costTask.execute();
        timeTask.execute();
        driveTimeTask.execute("driving");
    }

    @Override
    public int getDuration() {
        return super.getDuration() + timeToArrive;
    }
    public int getTimeToArrive() {
        return timeToArrive;
    }
    public void setTimeToArrive(int timeToArrive) {
        this.timeToArrive = timeToArrive;
    }

    public String getSurge() {
        return surge;
    }

    public void setSurge(String surge) {
        this.surge = surge;
    }

}
