package com.getthere.guber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.maps.model.LatLng;


public class HomeActivity extends ActionBarActivity{

    private Criteria criteria;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;
    private String bestProvider;
    private final String LOG_TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        location_setup();

        location_init();

        RouteFragment fragobj = new RouteFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragobj)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    private void location_setup() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, true);
        Log.d("provider", bestProvider);

        if (bestProvider == null) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                bestProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER)
                        .getName();
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                bestProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();
            } else {
                AlertDialog.Builder noproviders_builder = new AlertDialog.Builder(this);
                noproviders_builder
                        .setMessage(
                                "You don't have any location services enabled. If you want to see your "
                                        + "location you'll need to enable at least one in the Location menu of your device's Settings.  "
                                        + "Would you like to do that now?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 0);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog noproviders = noproviders_builder.create();
                noproviders.show();
            }
        }

        if (bestProvider != null) {
            locationListener = new mylocationlistener();
            locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
            lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
        }
    }

    private void location_init(){
        locationManager.addGpsStatusListener(new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int status) {
                switch (status) {
                    case GpsStatus.GPS_EVENT_STARTED:
                        Toast.makeText(getApplicationContext(), "GPS On", Toast.LENGTH_SHORT);
                        bestProvider = locationManager.getBestProvider(criteria, true);
                        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
                        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                        Log.d("provider", "Switched to : " + bestProvider);
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        Toast.makeText(getApplicationContext(), "GPS Off", Toast.LENGTH_SHORT);
                        bestProvider = locationManager.getBestProvider(criteria, true);
                        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
                        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                        Log.d("provider", "Switched to : " + bestProvider);
                        break;
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        break;
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        break;
                }
            }
        });

        location_update();
    }

    public void location_update(){
        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
    }

    public Location getLocation(){
        return lastKnownLocation;
    }

    public static class RouteFragment extends Fragment implements OnItemClickListener{
        private final String LOG_TAG = HomeActivity.class.getSimpleName();

        public RouteFragment() {
        }


        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String str = (String) adapterView.getItemAtPosition(position);
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

            final View inflateView = inflater.inflate(R.layout.fragment_home, container, false);

            final ClearableAutoCompleteTextView dest_loc = (ClearableAutoCompleteTextView) inflateView.findViewById(R.id.dest_loc);
            final ClearableAutoCompleteTextView start_loc = (ClearableAutoCompleteTextView) inflateView.findViewById(R.id.start_loc);

            Log.v(LOG_TAG, "checking results: " + dest_loc.getLayout());
            PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item);
            dest_loc.setAdapter(adapter);
            start_loc.setAdapter(adapter);
            start_loc.setOnItemClickListener(this);
            dest_loc.setOnItemClickListener(this);

            start_loc.setHint("Current location");

            Button searchButton= (Button) inflateView.findViewById(R.id.search_button);

            searchButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){
                    ((HomeActivity)getActivity()).location_update();
                    Location start = ((HomeActivity)getActivity()).getLocation();

                    if (dest_loc.getText().toString().trim().equalsIgnoreCase("")) {
                        dest_loc.setError("This field can not be blank");
                        return;
                    } else {
                        if (start_loc.getText().toString().trim().equalsIgnoreCase("")) {
                            start_loc.setText(start.getLatitude() + "," + start.getLongitude());
                        }
                    }

                    Geocode getLatLng = new Geocode();
                    Log.v(LOG_TAG, "dest loc is: " + dest_loc.getText().toString());
                    getLatLng.execute(start_loc.getText().toString());
                    LatLng start_coordinates = null;
                    try {
                        start_coordinates = getLatLng.get();
                    }
                    catch(Exception e){

                    }

                    Geocode getEndLatLng = new Geocode();
                    getEndLatLng.execute(dest_loc.getText().toString());
                    LatLng dest_coordinates = null;
                    try {
                        dest_coordinates = getEndLatLng.get();
                    }
                    catch(Exception e){

                    }

                    Log.v(LOG_TAG, "Start coordinates are: " + start_coordinates.latitude);
                    Log.v(LOG_TAG, "Dest coordinates are: " + dest_coordinates.longitude);

                    Bundle args = new Bundle();
                    args.putParcelable("from_position", start_coordinates);
                    args.putParcelable("to_position", dest_coordinates);

                    Intent intent = new Intent(getActivity(), RankingActivity.class).putExtra("bundle", args);
                    startActivity(intent);
                }
            });

            return inflateView;
        }
    }

    private class mylocationlistener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (lastKnownLocation != null) {
                lastKnownLocation.set(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("provider", "Provider changed");
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    Toast.makeText(getApplicationContext(), provider + " out of service", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.AVAILABLE:
                    Toast.makeText(getApplicationContext(), provider + " available", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(getApplicationContext(), provider + " temporarily unavailable", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
