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
        builder.scheme("https").authority("api.uber.com")
                .appendPath("v1").appendPath("estimates").appendPath("time")
                .appendQueryParameter("server_token", "vHClWnn8Gjr1xijXhDHab2qCFuQo5TCuXe14CdAC")
                .appendQueryParameter("start_latitude", String.valueOf(cur_lat))
                .appendQueryParameter("start_longitude", String.valueOf(cur_long));
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
        int time = 0;
        try {

            jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray prices = (JSONArray) jsonObject.get("times");

            for(int i =0; i<prices.length(); i++) {
                if(prices.getJSONObject(i).getString("display_name").equals("uberX")){
                    time = prices.getJSONObject(i).getInt("estimate");
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("UberX Time: ", Integer.toString(time));
        return new LatLng(1.2, 1.2);
    }



    @Override
    protected void onPostExecute(LatLng location){
        car2Go.setLocation(location);
    }

}