package com.example.hackathing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static TestCenters testCenters;
    static RequestQueue requestQueue;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the RequestQueue and make api call
        requestQueue = VolleyController.getInstance(this.getApplicationContext()).getRequestQueue();
        new testCenterRequest().execute();
    }

    public void OnOpenInGoogleMaps (View view) {
        EditText teamAddres = (EditText) findViewById(R.id.addr);

//        String locationName = teamAddres + ", " + "Canada";
//        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
//        try {
//            List<Address> address = geoCoder.getFromLocationName(locationName, 1);
//           latitude = address.get(0).getLatitude();
//           longitude = address.get(0).getLongitude();
//           Log.d("simon", String.valueOf(latitude));
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("simon",e.toString());
//        }

        Geocoder geoCoder = new Geocoder(this);
        String a = teamAddres.getText().toString();
        Log.d("simon", "wjdw");
        Log.d("simon", a);
        try {
            List<Address> addresses = geoCoder.getFromLocationName(a + ", " + "Canada", 1);
            if (addresses != null) {
                Address address = addresses.get(0);
                // Use the address as needed
                String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                Log.d("simon", message);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.d("simon", String.valueOf(latitude));
                Log.d("simon", String.valueOf(longitude));
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // handle exception
            Log.d("simon",e.toString());
        }

        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q="+teamAddres.getText());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
    }

    //Method for making the initial API call to fetch all of the test centers
    //Info stored in testCenters object in Main Activity
    private static class testCenterRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = "https://services1.arcgis.com/B6yKvIZqzuOr0jBR/arcgis/rest/services/COVID19_Testing_Centres_in_Canada/FeatureServer/0/query?where=1=1&outFields=*&outSR=4326&f=json";
            String output;
            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
            // Add the request to the RequestQueue.
            MainActivity.requestQueue.add(stringRequest);

            try {
                JSONObject result = future.get();
                MainActivity.testCenters = new TestCenters((JSONArray) result.get("features"));
                Log.d("MyTag", String.valueOf(MainActivity.testCenters.numberOfTestCenters()));
                output = "true";
            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
                output = "false";
            }
            return output;
        }

        @Override
        protected void onPostExecute(String result) {
            //Put everything to do with accessing TestCenters here
        }
    }
}