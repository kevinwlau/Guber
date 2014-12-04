package com.gubertravel.guber;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends Activity implements AsyncResponse {
    Geocode location = new Geocode();
    Uber uber = new Uber();
    private GoogleMap mMap;

    private Criteria criteria;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;
    private String bestProvider;

    private Double dest_lat = null;
    private Double dest_long = null;

    private Boolean locationSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        location_setup();

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

        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        Intent intent = getIntent();
        String place = intent.getStringExtra(HomeActivity.EXTRA_LOCATION);

        //get current location before running anything else
        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);

        if (place != null) {
            location.delegate = this;
            location.execute(place);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }

    public void markMap(ArrayList<String> output) {
        if (output.size() != 0) {
            dest_lat = Double.parseDouble(output.get(0));
            dest_long = Double.parseDouble(output.get(1));
            String address = output.get(2);

            TextView loc = (TextView) findViewById(R.id.locationName);
            loc.setText(address);

            LatLng location = new LatLng(dest_lat, dest_long);
            MarkerOptions marker = new MarkerOptions().position(location).title(address);
            mMap.addMarker(marker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
            locationSet = true;

            Double cur_lat = lastKnownLocation.getLatitude();
            Double cur_long = lastKnownLocation.getLongitude();

            uber.delegate2 = this;
            uber.execute(cur_lat, cur_long, dest_lat, dest_long);

        } else {
            locationSet = false;
            Toast.makeText(getApplicationContext(), "Could not find location", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void getUberCost(String cost) {
        TextView costText = (TextView) findViewById(R.id.uberCost);
        costText.setText((cost.length() > 0) ? cost : "Location too far for Uber");
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
