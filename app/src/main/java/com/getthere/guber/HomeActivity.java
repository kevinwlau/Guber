package com.getthere.guber;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RouteFragment())
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

    public static class RouteFragment extends Fragment {

        private final String LOG_TAG = HomeActivity.class.getSimpleName();


        public RouteFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View inflateView = inflater.inflate(R.layout.fragment_home, container, false);

            Button searchButton= (Button) inflateView.findViewById(R.id.search_button);

            searchButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){
                    EditText start_loc = (EditText) inflateView.findViewById(R.id.start_loc);
                    EditText dest_loc = (EditText) inflateView.findViewById(R.id.dest_loc);

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
}
