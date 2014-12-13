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

public class UberFetchTimeTask extends AsyncTask<Double, Void, Integer> {

    private Uber uber;
    private ListAdapter adapter;

    public UberFetchTimeTask(Uber uber, ListAdapter adapter){
        this.adapter = adapter;
        this.uber = uber;
    }


    @Override
    protected Integer doInBackground(Double... params) {
        double cur_lat = uber.getStart().latitude;
        double cur_long = uber.getStart().longitude;

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
            time = -1;
            Log.d("EXCEPTION RESPONSE: ", stringBuilder.toString());
        }
        Log.d("UberX Time: ", Integer.toString(time));
        return new Integer(time);
    }



    @Override
    protected void onPostExecute(Integer time){
        uber.setTimeToArrive(time);
        uber.addDetail("Pickup ETA: " + Transport.formatTime(time));
        adapter.notifyDataSetChanged();
    }

}