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
public class GoogleMapsFetchTimeTask extends AsyncTask<String, Void, Integer[]> {

    private Transport transport;
    private ListAdapter adapter;

    public GoogleMapsFetchTimeTask(Transport transport, ListAdapter adapter) {
        this.transport = transport;
        this.adapter = adapter;
    }

    @Override
    protected Integer[] doInBackground(String... params) {
        String mode = params[0];
        double cur_lat = transport.getStart().latitude;
        double cur_long = transport.getStart().longitude;
        double dest_lat = transport.getDest().latitude;
        double dest_long = transport.getDest().longitude;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("maps.googleapis.com")
                .appendPath("maps").appendPath("api").appendPath("directions").appendPath("json")
                .appendQueryParameter("departure_time","now")
                .appendQueryParameter("origin", String.valueOf(cur_lat) + "," + String.valueOf(cur_long))
                .appendQueryParameter("destination", String.valueOf(dest_lat) +","+ String.valueOf(dest_long))
                .appendQueryParameter("key", "AIzaSyD_5pG8APkJK0iCVRIfAlwLosqx4ZOlfXg")
                .appendQueryParameter("mode", mode);
        String queryString = builder.build().toString();

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpResponse response = null;
        StringBuilder stringBuilder = new StringBuilder();

        try{
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
        Integer duration = new Integer(0);
        Integer distance = new Integer(0);
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            duration  = ((JSONArray) jsonObject.get("routes")).getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONObject("duration").getInt("value");
            distance = ((JSONArray) jsonObject.get("routes")).getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONObject("distance").getInt("value");
        } catch( JSONException e) {
            duration = -1;
            distance = -1;
            e.printStackTrace();
            Log.d("EXCEPTION RESPONSE: ", stringBuilder.toString());
        }
        Log.d(transport.getType()+ " "+ mode + " Time: ", duration.toString());
        Log.d(transport.getType()+ " "+ mode + " Distance: ", distance.toString());

        Integer[] result = {duration, distance};
        return result;
    }

    @Override
    protected void onPostExecute(Integer[] result) {
        transport.setDuration(result[0]);
        transport.setDistance(result[1]);
        transport.addDetail("Travel Time: " + Transport.formatTime(result[0]));
        transport.addDetail("Distance: " + Transport.formatDistance(result[1]));
        adapter.notifyDataSetChanged();
    }
}
