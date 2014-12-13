package com.getthere.guber;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by kevinlau on 12/9/2014.
 */
public class Car2Go extends Transport {

    private final double minuteRate = 0.41;
    private final double hourRate = 14.99;

    private LatLng carLocation;
    private Transport walkSegment;
    private Transport driveSegment;

    Car2Go(LatLng start, LatLng dest, Context context, ListAdapter adapter){
        super(start, dest, "Car2Go");


        //Create and save deep linking Intent
        Intent intent;
        PackageManager manager = context.getPackageManager();
        try {
            intent = manager.getLaunchIntentForPackage("com.car2go");
            if (intent == null)
                throw new PackageManager.NameNotFoundException();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            super.setIntent(intent);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("ERROR: ", "NameNotFoundException");
        }

        //fetch location of closest car
        Car2GoFetchCarTask nearestCar = new Car2GoFetchCarTask(this, adapter);
        nearestCar.execute();
    }

    public void setCarLocation(LatLng location){
        this.carLocation = location;
    }
    public LatLng getCarLocation() {
        return carLocation;
    }

    @Override
    public int getDuration(){
        int duration = 0;
        ArrayList<String> details = new ArrayList<String>();

        if(walkSegment!=null) {
            duration += walkSegment.getDuration();
            details.add(String.format("Walking time: " + Transport.formatTime(walkSegment.getDuration())) );
            details.add(String.format("Walking distance: " + Transport.formatDistance(walkSegment.getDistance()) ));
        }
        if(driveSegment!=null) {
            duration += driveSegment.getDuration();
            details.add(String.format("Driving time: " + Transport.formatTime(driveSegment.getDuration())) );
            details.add(String.format("Driving distance: " + Transport.formatDistance(driveSegment.getDistance())));
        }
        setDetails(details);
        return duration;
    }

    @Override
    public String getCost(){

        int duration = 0;
        double cost = 0;
        if(walkSegment!=null)
            duration += walkSegment.getDuration();
        if(driveSegment!=null)
            duration += driveSegment.getDuration();
        super.setDuration(duration);

        int remainingMinutes = (duration%3600)/60;
        int hours = duration/3600;
        cost += Math.min(remainingMinutes*minuteRate, hourRate);
        cost += hours*14.99;
        return String.format("$%.2f",cost);
    }

    @Override
    public int getDistance(){
        int distance = 0;
        if(walkSegment!=null) {
            distance += walkSegment.getDistance();
        }
        if(driveSegment!=null) {
            distance += driveSegment.getDistance();
            addDetail(String.format("Distance to destination: Distance: $%.2f mi", driveSegment.getDistance()/1609));
        }
        return distance;
    }

    public Transport getDriveSegment() {
        return driveSegment;
    }

    public void setDriveSegment(Transport driveSegment) {
        this.driveSegment = driveSegment;
    }

    public Transport getWalkSegment() {
        return walkSegment;
    }

    public void setWalkSegment(Transport walkSegment) {
        this.walkSegment = walkSegment;
    }

}
