package com.getthere.guber;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.ArrayList;

/**
 * Created by kevinlau on 10/12/2014.
 */
public class Geocode extends AsyncTask<String, Void, ArrayList<String>> {
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        String place = params[0];
        String placeQuery = "https://maps.googleapis.com/maps/api/geocode/json?address=%s";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpResponse response = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            String formattedURI = String.format(placeQuery, URLEncoder.encode(place, "UTF8"));
            URI query = new URI(formattedURI);
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
        ArrayList<String> location = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            String address = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getString("formatted_address");
            location.add(String.valueOf(lat));
            location.add(String.valueOf(lng));
            location.add(address);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    protected void onPostExecute(ArrayList<String> location){
    }
}
