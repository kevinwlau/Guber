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

public class Uber extends AsyncTask<Double, Void, String> {

    private ArrayAdapter<String> mForecastAdapter;

    public Uber(ArrayAdapter<String> mForecastAdapter){
        this.mForecastAdapter = mForecastAdapter;
    }

    @Override
    protected String doInBackground(Double... params) {
        double cur_lat = params[0];
        double cur_long = params[1];
        double dest_lat = params[2];
        double dest_long = params[3];


        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("api.uber.com")
                .appendPath("v1").appendPath("estimates").appendPath("price")
                .appendQueryParameter("server_token", "vHClWnn8Gjr1xijXhDHab2qCFuQo5TCuXe14CdAC")
                .appendQueryParameter("start_latitude", String.valueOf(cur_lat))
                .appendQueryParameter("start_longitude", String.valueOf(cur_long))
                .appendQueryParameter("end_latitude", String.valueOf(dest_lat))
                .appendQueryParameter("end_longitude", String.valueOf(dest_long));
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

        JSONObject jsonObject = new JSONObject();
        String cost = new String();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            cost = ((JSONArray) jsonObject.get("prices")).getJSONObject(0).getString("estimate");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Uber", cost);
        return cost;
    }



    @Override
    protected void onPostExecute(String cost){
        mForecastAdapter.add(cost);
    }

}