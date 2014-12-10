package com.getthere.guber;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Practice001 on 12/9/2014.
 */
public class ListAdapter extends ArrayAdapter<Transport> {
    private final Context context;
    private ArrayList<Transport> transport;

    public ListAdapter(Context context, ArrayList<Transport> transport) {
        super(context, R.layout.custom_view, transport);
        this.context = context;
        this.transport = transport;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_view, parent, false);
        TextView transportation = (TextView) rowView.findViewById(R.id.Transport);
        TextView information = (TextView) rowView.findViewById(R.id.Info);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.Image);

        String cost = transport.get(position).getCost();
        int duration = transport.get(position).getDuration();

        //Generate time String
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        String timeString = "";
        if(hours!=0){
            timeString += String.format("%d hrs ", hours);
        }
        timeString += String.format("%d mins %d secs", minutes, seconds);

        if(transport.get(position).getType().equals("Uber")){
            transportation.setText("Uber");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            imageView.setImageResource(R.drawable.uber);
        }
        else if(transport.get(position).getType().equals("Car2Go")){
            transportation.setText("Car2Go");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            imageView.setImageResource(R.drawable.car2go);
        }
        else if(transport.get(position).getType().equals("Walking")){
            transportation.setText("Walk");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            imageView.setImageResource(R.drawable.walk);
        }
        else if(transport.get(position).getType().equals("Public Transit")){
            transportation.setText("Bus");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            imageView.setImageResource(R.drawable.bus);
        }
        else if(transport.get(position).getType().equals("Driving")){
            transportation.setText("Car");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            //no icon for this yet?!
            imageView.setImageResource(R.drawable.ic_launcher);
        }

        return rowView;
    }

    @Override
    public void add(Transport T){
        super.add(T);
    }
}