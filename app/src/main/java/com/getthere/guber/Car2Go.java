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

        if(walkSegment!=null) {
            if(walkSegment.getDuration() < 0) return -1;
            duration += walkSegment.getDuration();
        }
        if(driveSegment!=null) {
            if(driveSegment.getDuration() < 0) return -1;
            duration += driveSegment.getDuration();
        }

        return duration;
    }

    @Override
    public String getCost(){

        int duration = 0;
        double cost = 0;
        if(walkSegment!=null){
            if(walkSegment.getDuration() < 0) return "Unavailable";
            duration += walkSegment.getDuration();
        }
        if(driveSegment!=null){
            if(driveSegment.getDuration() < 0) return "Unavailable";
            duration += driveSegment.getDuration();
        }
        super.setDuration(duration);

        if(duration<0) return "Unavailable";

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
            if(walkSegment.getDistance() < 0) return -1;
            distance += walkSegment.getDistance();
        }
        if(driveSegment!=null) {
            if(driveSegment.getDistance() < 0) return -1;
            distance += driveSegment.getDistance();
        }
        return distance;
    }

    @Override
    public String getDetails(){
        ArrayList<String> details = new ArrayList<String>();
        if(walkSegment!=null) {
            details.add("Walking time: " + Transport.formatTime(walkSegment.getDuration()));
            details.add("Walking distance: " + Transport.formatDistance(walkSegment.getDistance()));
        }
        if(driveSegment!=null) {
            details.add("Driving time: " + Transport.formatTime(driveSegment.getDuration()));
            details.add("Driving distance: " + Transport.formatDistance(driveSegment.getDistance()));
        }
        details.add("Estimated Cost: " + getCost());
        super.setDetails(details);
        return super.getDetails();
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
