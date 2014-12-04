package com.gubertravel.guber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class HomeActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.gubertravel.guber.MESSAGE";
    public final static String EXTRA_LOCATION = "com.gubertravel.guber.LOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCoord(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        EditText locationName = (EditText) findViewById(R.id.locationName);
        String place = locationName.getText().toString();
        if(place.length()>0) {
            intent.putExtra(EXTRA_LOCATION, place);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Address", Toast.LENGTH_SHORT).show();
        }
    }
}
