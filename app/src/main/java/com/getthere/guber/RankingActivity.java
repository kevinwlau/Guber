package com.getthere.guber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RankingActivity extends ActionBarActivity {

    public ArrayAdapter<Transport> mForecastAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
        // Add this line in order for this fragment to handle menu events.

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecastfragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public static class ForecastFragment extends Fragment {

        private final String LOG_TAG = RankingActivity.class.getSimpleName();
        public LatLng start, dest;

        public ListAdapter mForecastAdapter;

        @Override
        public void onStart(){
            super.onStart();
            updateRanking();
        }

        @Override
        public void onResume(){
             super.onResume();
        }

        public void updateRanking(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getParcelableExtra("bundle");
            LatLng start = bundle.getParcelable("from_position");
            LatLng dest = bundle.getParcelable("to_position");
            Log.v(LOG_TAG, "Start coordinates are: " + start.latitude + start.longitude);
            Log.v(LOG_TAG, "Dest coordinates are: " + dest.latitude + dest.longitude);


            final ArrayList<Transport> trans = new ArrayList<Transport>();
            mForecastAdapter = new ListAdapter(getActivity(), trans);
            mForecastAdapter.add(new Uber(start, dest, mForecastAdapter));
            mForecastAdapter.add(new Car2Go(start, dest, getActivity(), mForecastAdapter) );
            mForecastAdapter.add(new Transit(start, dest, mForecastAdapter));
            mForecastAdapter.add(new Walk(start, dest, mForecastAdapter));
            mForecastAdapter.add(new Drive(start, dest, mForecastAdapter));
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);

            listView.setAdapter(mForecastAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    final Transport transport = mForecastAdapter.getItem(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(transport.getDetails()).setTitle(transport.getType());
                    builder.setPositiveButton("Launch App", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            startActivity(transport.getIntent());
                        }

                    });

                    builder.show();





                }
            });

            return rootView;
        }
    }

    }




