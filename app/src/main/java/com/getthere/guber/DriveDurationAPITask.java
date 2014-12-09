package com.getthere.guber;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aznerik on 12/8/2014.
 */
public class DriveDurationAPITask extends AsyncTask<LatLng, Void, Integer> {

    private ArrayAdapter<String> mForecastAdapter;

    public DriveDurationAPITask(ArrayAdapter<String> mForecastAdapter) { this.mForecastAdapter = mForecastAdapter; }

    @Override
    protected Integer doInBackground(LatLng... params) {
        double cur_lat = params[0].latitude;
        double cur_long = params[0].longitude;
        double dest_lat = params[1].latitude;
        double dest_long = params[1].longitude;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("maps.googleapis.com")
                .appendPath("maps").appendPath("api").appendPath("directions")
                .appendQueryParameter("origin", String.valueOf(cur_lat) + String.valueOf(cur_long))
                .appendQueryParameter("destination", String.valueOf(dest_lat) + String.valueOf(dest_long))
                .appendQueryParameter("key", "AIzaSyDJO0LxGFsIRqkf4_MiI0aTsvW5PT5s49k")
                .appendQueryParameter("mode", "driving");
        String queryString = builder.build().toString();

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpResponse response = null;
        StringBuilder stringBuilder = new StringBuilder();

        try{
            String formattedURI = URLEncoder.encode(queryString, "UTF8");
            URI query = new URI(queryString);
            request.setURI(query);
            Log.d("query", formattedURI);
            response = client.execute(request);

            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (URISyntaxException e) {
            Log.d("error", "URIsyntaxexception");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.d("error", "unsupported encoding exc");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("error", "io exc");
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        Integer duration = new Integer(0);
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            duration  = ((JSONArray) jsonObject.get("routes")).getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONObject("duration").getInt("value");
        } catch( JSONException e) {
            e.printStackTrace();
        }
        Log.d("Drive", duration.toString());
        return duration;
    }

    @Override
    protected void onPostExecute(Integer duration) { mForecastAdapter.add(duration.toString()); }
}