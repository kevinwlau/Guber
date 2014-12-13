package com.getthere.guber;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * Created by Manav on 12/8/14.
 */
abstract class Transport {

    private String type;
    private String cost;
    private int distance;
    private int duration;
    private LatLng start;
    private LatLng dest;
    private Intent intent;

    private ArrayList<String> details;

    Transport(LatLng start, LatLng dest, String type){
        this.start = start;
        this.dest = dest;
        this.type = type;
        details = new ArrayList<String>();
    }

    public String getCost() {return cost;}

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getDuration() {
        return duration;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getDetails() {
        String text = "";
        for(String s: details){
            text += s;
            text += "\n";
        }
        return text;
    }

    public void setDetails(ArrayList<String> details) { this.details = details; }

    public void addDetail(String detail) { details.add(detail); }

    public static String formatTime(int duration){
        if(duration<0) return "Unavailable";
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        String timeString = "";
        if(hours!=0){
            timeString += String.format("%d hrs ", hours);
        }
        timeString += String.format("%d mins %d secs", minutes, seconds);
        return timeString;
    }

    public static String formatDistance(int distance){
        if(distance<0) return "Unavailable";
        double miles = distance/1609.0;
        return String.format("%.2f mi", miles);
    }

}
