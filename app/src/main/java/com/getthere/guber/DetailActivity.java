package com.getthere.guber;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(forecastStr);
            }

            Button searchButton= (Button) rootView.findViewById(R.id.launch_button);

            searchButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String start_location = "Austin";
                    String end_location = "Dallas";
                    String mode = "r"; // r = transit, b = biking, w = walking
                    Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + start_location + "&daddr=" + end_location + "&dirflg=" + mode));
                    startActivity(mapsIntent);

//                    Intent uberIntent = new Intent(android.content.Intent.ACTION_VIEW,
//                            Uri.parse("uber://?action=setPickup&product_id=91901472-f30d-4614-8ba7-9fcc937cebf5&pickup[latitude]=-33.9226277&pickup[longitude]=18.4232182&dropoff[latitude]=-33.972282&dropoff[longitude]=18.601956"));
//                    startActivity(uberIntent);

                    String start_latitude = "";
                    String start_longitude = "";
                    String end_latitude = "";
                    String end_longitude = "";
                    Intent uberIntent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("uber://?action=setPickup&product_id=91901472-f30d-4614-8ba7-9fcc937cebf5&pickup[latitude]=" + start_latitude + "&pickup[longitude]=" + start_longitude + "&dropoff[latitude]=" + end_latitude + "&dropoff[longitude]=" + end_longitude));
                    startActivity(uberIntent);

                    }
            });

            return rootView;
        }
    }
}
