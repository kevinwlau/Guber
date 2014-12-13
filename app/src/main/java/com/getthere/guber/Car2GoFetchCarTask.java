package com.getthere.guber;

import android.net.Uri;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class Car2GoFetchCarTask extends AsyncTask<Double, Void, LatLng> {

    private Car2Go car2Go;
    private ListAdapter adapter;

    public Car2GoFetchCarTask(Car2Go car, ListAdapter adapter){
        this.adapter = adapter;
        this.car2Go = car;
    }

    @Override
    protected LatLng doInBackground(Double... params) {
        double curLat = car2Go.getStart().latitude;
        double curLng = car2Go.getStart().longitude;

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
//            Log.d("query", formattedURI);
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
        String result = null;
        try {

            jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray cars = (JSONArray) jsonObject.get("placemarks");

            final double R = Double.MAX_VALUE; //Radius of earth in km
            double closest = R;
            Double curLatRad = Math.toRadians(curLat);

            for(int i =0; i<cars.length(); i++) {
                //parse car coordinates
                JSONObject car = cars.getJSONObject(i);
                Double carLat = Double.parseDouble(car.getJSONArray("coordinates").get(1).toString());
                Double carLng = Double.parseDouble(car.getJSONArray("coordinates").get(0).toString());

                //convert coordinates to Radians for haversine formula
                Double carLatRad = Math.toRadians(carLat);
                Double latDiff = Math.toRadians(carLat - curLat);
                Double lngDiff = Math.toRadians(carLng - curLng);

                //Haversine formula to find distance between two coordinates
                Double a = Math.pow(Math.sin(latDiff/2.0), 2) + Math.cos(carLatRad) * Math.cos(curLatRad) * Math.pow(Math.sin(lngDiff/2.0), 2);
                Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                Double distance = R*c;

                //Get closest car and save it
                if (distance < closest) {
                    closest = distance;
                    //save the car
                    result = car.toString();
                    nearestCarLat = carLat;
                    nearestCarLng = carLng;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION RESPONSE: ", stringBuilder.toString());
        }
        return new LatLng(nearestCarLat, nearestCarLng);
    }

    @Override
    protected void onPostExecute(LatLng location){
        car2Go.setCarLocation(location);
        car2Go.setDriveSegment(new Drive(car2Go.getCarLocation(), car2Go.getDest(), adapter) );
        car2Go.setWalkSegment(new Walk(car2Go.getStart(), car2Go.getCarLocation(), adapter) );
    }

}