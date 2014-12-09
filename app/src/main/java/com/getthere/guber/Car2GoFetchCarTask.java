package com.getthere.guber;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class Car2GoFetchCarTask extends AsyncTask<Double, Void, LatLng> {

    private Car2Go car2Go;

    public Car2GoFetchCarTask(Car2Go car){
        this.car2Go = car;
    }

    @Override
    protected LatLng doInBackground(Double... params) {
        double cur_lat = car2Go.getStart().latitude;
        double cur_long = car2Go.getStart().longitude;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("car2go.com")
                .appendPath("api").appendPath("v2.1").appendPath("vehicles")
                .appendQueryParameter("oauth_consumer_key", "Guber")
                .appendQueryParameter("loc", "austin")
                .appendQueryParameter("format", "json");
        String queryString = builder.build().toString();

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpResponse response = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
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

        JSONObject jsonObject;
        double nearestCarLat=0, nearestCarLng=0;
        try {

            jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray cars = (JSONArray) jsonObject.get("placemarks");

            for(int i =0; i<cars.length(); i++) {
                Log.d("Car location", cars.getJSONObject(i).getJSONArray("coordinates").get(0).toString());
                Double carLat = Double.parseDouble(cars.getJSONObject(i).getJSONArray("coordinates").get(0).toString());
                Double carLng = Double.parseDouble(cars.getJSONObject(i).getJSONArray("coordinates").get(1).toString());

                //calculate distance, get closest car's coordinates
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LatLng(nearestCarLat, nearestCarLng);
    }



    @Override
    protected void onPostExecute(LatLng location){
        car2Go.setLocation(location);
    }

}