package com.example.hackathing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static TestCenters testCenters;
    static ProvinceInformation provinceInformation;
    static RequestQueue requestQueue;

    static double latitude;
    static double longitude;
    static double hosLong;
    static double hosLat;
    static double lowestV;
    static double [] x = new double[148];
    static double [] xcl = new double[148];
    static int [] finalind = new int [10];
    static double [] finalDis = new double [10];
    static double []xdis = new double[148];
    static int r = 6371;
    static int lowest;
    static String nameof;
    static String addre;
    static int [] ind = new int[148];
    static Button searchButton;
    static Button searchGoogleMap;
    static Button previousButton;
    static Button nextButton;

    ListView clinicInfoList;
    ArrayList<String> clinicInfo= new ArrayList<>();

    ListView statsList;
    ArrayList<String> localStats;

    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Disable buttons until API calls are finalized
        searchButton = (Button) findViewById(R.id.searchButton);;
        searchGoogleMap = (Button) findViewById(R.id.searchGoogleMap);
        previousButton = (Button) findViewById(R.id.previousButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        searchButton.setEnabled(false);
        searchGoogleMap.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);


        // Instantiate the RequestQueue and make api calls for information
        requestQueue = VolleyController.getInstance(this.getApplicationContext()).getRequestQueue();
        new testCenterRequest().execute();
        new provinceInfoRequest().execute();

        //clinic info to display in list
        clinicInfoList = (ListView) findViewById(R.id.clinicInfoList);
        clinicInfo.add("Enter postal code to find nearest COVID-19 clinic");
        final ArrayAdapter<String> adapterClinicInfo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clinicInfo);
        clinicInfoList.setAdapter(adapterClinicInfo);

        // area stats to display in list
        statsList = (ListView) findViewById(R.id.statsList);
        localStats = new ArrayList<>();
        localStats.add("local stats");
        final ArrayAdapter<String> adapterStats = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, localStats);
        statsList.setAdapter(adapterStats);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 8){
                    nextButton.setEnabled(false);
                }
                else {
                    counter++;
                    clinicInfo.clear();
                    reloadList();
                    populateProvinceInformation();

                    adapterClinicInfo.notifyDataSetChanged();
                    adapterStats.notifyDataSetChanged();
                    previousButton.setEnabled(true);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 0){
                    previousButton.setEnabled(false);
                }
                else {
                    counter--;
                    clinicInfo.clear();
                    reloadList();
                    populateProvinceInformation();

                    adapterClinicInfo.notifyDataSetChanged();
                    adapterStats.notifyDataSetChanged();
                    nextButton.setEnabled(true);

                    if (counter == 1) {
                        previousButton.setEnabled(false);
                    }
                }
            }
        });


    }

    public void Search (View view){
        EditText teamAddres = (EditText) findViewById(R.id.addr);
        TextView textView12 = (TextView) findViewById(R.id.textView12);

        Geocoder geoCoder = new Geocoder(this);
        String a = teamAddres.getText().toString();

        try {
            List<Address> addresses = geoCoder.getFromLocationName(a + ", " + "Canada", 1);
            if (addresses != null) {
                Address address = addresses.get(0);
                // Use the address as needed
                String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
            }

            JSONObject jsonOb;
            //longitude = -75.6950;
            //latitude = 45.424721;

            try {


                for(int i = 0; i < 149; i++){
                    jsonOb = MainActivity.testCenters.getCoordinates(i);
                    hosLong = jsonOb.getDouble("x");
                    hosLat = jsonOb.getDouble("y");
                    double p = Math.PI/180;
                    double aa = 0.5-Math.cos((hosLat - latitude)*p)/2+Math.cos(latitude*p)*Math.cos(hosLat*p)*(1-Math.cos((hosLong-longitude)*p))/2;
                    x[i]=12742*Math.asin(Math.sqrt(aa));
                    xcl[i]=12742*Math.asin(Math.sqrt(aa));
                    Log.d("Bille", String.valueOf(longitude));
                    Log.d("Bille", String.valueOf(latitude));

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
            boolean sorted = false;
            double temp;
            while(!sorted){
                sorted = true;
                for(int i = 0; i<x.length-1;i++){
                    if(xcl[i]>xcl[i+1]){
                        temp = xcl[i];
                        xcl[i]=xcl[i+1];
                        xcl[i+1]=temp;
                        sorted = false;
                    }
                }
            }

            for(int i = 0; i<x.length-1;i++){
                for(int j = 1; j<x.length;j++){
                    if(xcl[i]==x[j]){
                        ind[i]=j;
                        xdis[i]=x[j];
                    }
                }

            }
            lowestV = xcl[0];
            lowest = ind[0];
            finalind = Arrays.copyOfRange(ind,0,9);
            finalDis = Arrays.copyOfRange(xdis,0,9);

           Log.d("namee", MainActivity.testCenters.getName(finalind[0]));
           Log.d("namee", MainActivity.testCenters.getAddress(finalind[0]));
           Log.d("namee", MainActivity.testCenters.getHOP(finalind[0]));
           Log.d("namee", MainActivity.testCenters.getNumber(finalind[0]));

        } catch (IOException e) {
            // handle exception
            Log.d("simon",e.toString());
        }
        nameof =  MainActivity.testCenters.getName(finalind[0]);
        addre = MainActivity.testCenters.getAddress(finalind[0]);
        counter = 0;

        searchGoogleMap.setEnabled(true);
        nextButton.setEnabled(true);

        reloadList();
        populateProvinceInformation();
    }

    public void reloadList (){


        String hours = MainActivity.testCenters.getHOP(finalind[counter]);
        String num = MainActivity.testCenters.getNumber(finalind[counter]);
        java.util.Date date=new java.util.Date();
        String day = date.toString().substring(0,3);
        clinicInfo.clear();
        String s =  MainActivity.testCenters.getName(finalind[counter]);
        addre = MainActivity.testCenters.getAddress(finalind[counter]);
        clinicInfo.add(s + "  " + addre);

        clinicInfo.add(day + "'s Working Hour: " + hours);
        clinicInfo.add("Phone #: " + num);
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
            MainActivity.searchButton.setEnabled(true);
        }
    }

    //Method for making the initial API call to fetch all of the province information
    //Info stored in provinceInformation object in Main Activity
    private static class provinceInfoRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = "https://api.opencovid.ca/summary";
            String output;
            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
            // Add the request to the RequestQueue.
            MainActivity.requestQueue.add(stringRequest);

            try {
                JSONObject result = future.get();
                MainActivity.provinceInformation = new ProvinceInformation((JSONArray) result.get("summary"));
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
            MainActivity.searchButton.setEnabled(true);
        }
    }

    public void populateProvinceInformation() {
        localStats.clear();
        localStats.add("Active Cases: " + provinceInformation.getActiveCases(testCenters.getProvince(finalind[counter])));
        localStats.add("Cumulative Cases: " + provinceInformation.getCumulativeCases(testCenters.getProvince(finalind[counter])));
        localStats.add("Recovered: " + provinceInformation.getRecovered(testCenters.getProvince(finalind[counter])));
    }
}