package com.example.hackathing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Making initial API call
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://services1.arcgis.com/B6yKvIZqzuOr0jBR/arcgis/rest/services/COVID19_Testing_Centres_in_Canada/FeatureServer/0/query?where=1=1&outFields=*&outSR=4326&f=json";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TestCenters testCenters = new TestCenters((JSONArray) response.get("features"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //TODO: Display error in GUI
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag", error.toString());
                //TODO: Display error in GUI
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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




}