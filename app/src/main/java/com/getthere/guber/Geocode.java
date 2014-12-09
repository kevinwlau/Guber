package com.getthere.guber;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kevinlau on 10/12/2014.
 */
public class Geocode extends AsyncTask<String, Void, LatLng> {
    String formattedAddress;
    private static final String LOG_TAG = "ExampleApp";
    private double[] coordinates;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    private static final String OUT_JSON = "/json";
    // Method to geocode address

    @Override
    protected LatLng doInBackground(String... params) {

        String address = params[0];
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + OUT_JSON);
            sb.append("?address=" + URLEncoder.encode(address, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Maps API URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Maps API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        double[] coordinates = new double[2];
        JSONObject jsonObject = new JSONObject();
        double lat=0, lng=0;
        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            lng = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            formattedAddress = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getString("formatted_address");

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);

//            locations.latitude = lat;
//            locations.longitude = lng;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng locations = new LatLng(lat, lng);
        return locations;
    }

    public String getFormattedAddress(){
        return formattedAddress;
    }
}
