package com.example.hackathing;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestCenters {
    private JSONArray locations;

    public TestCenters(JSONArray locations){
        this.locations = locations;
    }

    public int numberOfTestCenters() { return locations.length(); }

    //Input: Index of the hospital in locations (from 0 to numberOfTestCenters()
    //Output: Coordinates in JSONObject type
    // x => latitude, y => longitude
    public JSONObject getCoordinates(int number) {
        JSONObject coordinateJson;
        try {
            coordinateJson = (JSONObject) ((JSONObject) locations.get(number)).get("geometry");
        } catch (JSONException e) {
            coordinateJson = new JSONObject();
        }
        return  coordinateJson;
    }

    public String getName(int number) {
        JSONObject nameJson;
        String names;
        try {
            nameJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
            names = nameJson.getString("USER_Name");
        } catch (JSONException e) {
            names = new String();
        }
        return  names;
    }

    public String getProvince(int number) {
        JSONObject nameJson;
        try {
            nameJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
            return nameJson.getString("USER_Prov");
        } catch (JSONException e) {
            return "Not Available";
        }
    }

    public String getNumber(int number) {
        JSONObject numberJson;
        String numbers="";
        try {
            numberJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
            if(numberJson.getString("USER_Phone").equals("null") || numberJson.getString("USER_Phone").length() == 0){
                numbers = "Not Available";
            }else{
                numbers = numberJson.getString("USER_Phone");
            }

        } catch (JSONException e) {
            numbers = "Not Available";
        }
        return  numbers;
    }

    public String getAddress(int number) {
        JSONObject addressJson;
        String st;
        String city;
        String prov;
        String pos;
        String address="";
        try {
            addressJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
            st = addressJson.getString("USER_Street");
            city = addressJson.getString("USER_City");
            prov = addressJson.getString("USER_Prov");
            pos = addressJson.getString("USER_PostalCode");
            address = st+", "+city+", "+prov+", "+pos;
        } catch (JSONException e) {
            address = new String();
        }
        return  address;
    }
    public String getHOP(int number) {

        java.util.Date date=new java.util.Date();
        String day = date.toString().substring(0,3);
        Log.d("wdwwe", day);

        JSONObject hopJson;

        try {
            hopJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
            if(day.equals("Thu")){
                day = "Thur";
            }
            if(hopJson.getString("USER_" + day).equals("null") || hopJson.getString("USER_" + day).length() == 0) {
                return "Not Available";
            }else {
                return hopJson.getString("USER_" + day);
            }

        } catch (JSONException e) {
            hopJson = new JSONObject();
            return "you capping";
        }

    }
}