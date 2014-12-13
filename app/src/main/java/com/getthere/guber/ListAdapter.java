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
        String timeString = Transport.formatTime(duration);

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
            transportation.setText("Public Transit");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            imageView.setImageResource(R.drawable.bus);
        }
        else if(transport.get(position).getType().equals("Driving")){
            transportation.setText("Drive");
            information.setText("Estimated Time: " + timeString + "\nEstimated Cost:  " + cost);
            imageView.setImageResource(R.drawable.car);
        }

        return rowView;
    }

    @Override
    public void add(Transport T){
        super.add(T);
    }
}