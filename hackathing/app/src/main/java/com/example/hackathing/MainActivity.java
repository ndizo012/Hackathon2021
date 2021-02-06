package com.example.hackathing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

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

        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q="+teamAddres.getText());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
    }


}