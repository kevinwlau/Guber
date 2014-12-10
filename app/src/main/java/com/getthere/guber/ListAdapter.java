package com.getthere.guber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Practice001 on 12/9/2014.
 */
public class ListAdapter extends ArrayAdapter<Transport> {
    private final Context context;
    private Transport[] transport;

    public ListAdapter(Context context, Transport[] transport) {
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

        String cost = transport[position].getCost();
        String duration = Integer.toString(transport[position].getDuration());
        //There is a better way to do this, but for now I'll just brute force it
        if(transport[position].getType().equals("Uber")){
            transportation.setText("Uber");
            information.setText("Estimated Time: " + duration + "\nEstimated Cost:  $" + cost);
            imageView.setImageResource(R.drawable.uber);
        }
        else if(transport[position].getType().equals("Car2Go")){
            transportation.setText("Car2Go");
            information.setText("Estimated Time: " + duration + "\nEstimated Cost:  $" + cost);
            imageView.setImageResource(R.drawable.car2go);
        }
        else if(transport[position].getType().equals("Walking")){
            transportation.setText("Walk");
            information.setText("Estimated Time: " + duration + "\nEstimated Cost:  $" + cost);
            imageView.setImageResource(R.drawable.walk);
        }
        else if(transport[position].getType().equals("Public Transit")){
            transportation.setText("Bus");
            information.setText("Estimated Time: " + duration + "\nEstimated Cost:  $" + cost);
            imageView.setImageResource(R.drawable.bus);
        }
        else if(transport[position].getType().equals("Driving")){
            transportation.setText("Car");
            information.setText("Estimated Time: " + duration + "\nEstimated Cost:  $" + cost);
            //no icon for this yet?!
            imageView.setImageResource(R.drawable.ic_launcher);
        }

        return rowView;
    }

}