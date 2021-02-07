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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static TestCenters testCenters;
    static RequestQueue requestQueue;
    static double latitude;
    static double longitude;
    static double hosLong;
    static double hosLat;
    static double lowestV;
    static double [] x = new double[148];
    static double [] xcl = new double[148];
    static int [] finalind = new int [10];
    static int r = 6371;
    static int lowest;
    static String nameof;
    static String addre;
    static int [] ind = new int[148];

    ListView clinicInfoList;
    ArrayList<String> clinicInfo;
    ListView statsList;
    ArrayList<String> localStats;
    Button searchButton;
    Button previousButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the RequestQueue and make api call
        requestQueue = VolleyController.getInstance(this.getApplicationContext()).getRequestQueue();
        new testCenterRequest().execute();

        //clinic info to display in list
        clinicInfoList = (ListView) findViewById(R.id.clinicInfoList);
        clinicInfo = new ArrayList<>();
        clinicInfo.add("Enter postal code to find nearest COVID-19 clinic");
        clinicInfo.add("testing1");
        final ArrayAdapter<String> adapterClinicInfo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clinicInfo);
        clinicInfoList.setAdapter(adapterClinicInfo);

        // area stats to display in list
        statsList = (ListView) findViewById(R.id.statsList);
        localStats = new ArrayList<>();
        localStats.add("local stats");
        final ArrayAdapter<String> adapterStats = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, localStats);
        statsList.setAdapter(adapterStats);
    }

    public void Search (View view){
        EditText teamAddres = (EditText) findViewById(R.id.addr);
        TextView textView12 = (TextView) findViewById(R.id.textView12);

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

            JSONObject jsonOb;
            JSONObject jsonOb2;
            try {


                for(int i = 0; i < 149; i++){
                    jsonOb = MainActivity.testCenters.getCoordinates(i);

                    hosLong = jsonOb.getDouble("x");
                    hosLat = jsonOb.getDouble("y");
                    //double dLat = (hosLat - latitude)*(Math.PI/180);
                    //double dLon = (hosLong - longitude)*(Math.PI/180);
                    //double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(latitude*(Math.PI/180)) * Math.cos(hosLat*(Math.PI/180))* Math.sin(dLon/2) * Math.sin(dLon/2);
                    //double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                    //double d = r*c;
                    //longitude = -75.695;
                    //latitude = 45.424721;
                    double p = Math.PI/180;
                    double aa = 0.5-Math.cos((hosLat - latitude)*p)/2+Math.cos(latitude*p)*Math.cos(hosLat*p)*(1-Math.cos((hosLong-longitude)*p))/2;


                    x[i]=12742*Math.asin(Math.sqrt(aa));

                    Log.d("Bille", String.valueOf(longitude));
                    Log.d("Bille", String.valueOf(latitude));

                }
            } catch (JSONException e) {
                e.printStackTrace();
                //JSONObject jsonOb = new JSONObject();
                //output = "false";
            }
            lowestV = x[0];
            //Arrays.sort(x);
            //Log.d("hii", String.valueOf(x[0]));
            for(int j = 0; j<x.length;j++){

                if(lowestV > x[j]){
                    lowestV = x[j];
                    lowest = j;
                }

            }
            Log.d("hii", String.valueOf(lowestV));

            Log.d("hii", String.valueOf(lowest));
            try {
                jsonOb2 = MainActivity.testCenters.getName(lowest);
                nameof = jsonOb2.getString("USER_Name");
                addre = jsonOb2.getString("USER_Street");
            }catch(JSONException e) {
                e.printStackTrace();
            }
            Log.d("hiiiii", nameof);
        } catch (IOException e) {
            // handle exception
            Log.d("simon",e.toString());
        }
        textView12.setText(nameof);



    }

    public void Next (View view){

    }

    public void OnOpenInGoogleMaps (View view) {


        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q="+addre);

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