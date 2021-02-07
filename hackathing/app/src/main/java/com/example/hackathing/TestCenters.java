package com.example.hackathing;

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
            nameJson = new JSONObject();
            names = new String();
        }
        return  names;
    }
    public String getNumber(int number) {
        JSONObject numberJson;
        String numbers="";
        try {
            numberJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
            if(numberJson.getString("USER_Phone").equals("null")){
                numbers = "Not Available";
            }else{
                numbers = numberJson.getString("USER_Phone");
            }

        } catch (JSONException e) {
            numberJson = new JSONObject();
            numbers = new String();
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
            addressJson = new JSONObject();
            address = new String();
        }
        return  address;
    }
    public String getHOP(int number) {
        JSONObject hopJson;
        String mon;
        String tue;
        String wed;
        String thu;
        String fri;
        String sat;
        String sun;
        String hop="";
        try {
            hopJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");

            if(hopJson.getString("USER_Mon").equals("null")){
                mon = "Not Available";
            }else{
                mon = hopJson.getString("USER_Mon");
            }
            if(hopJson.getString("USER_Tue").equals("null")){
                tue = "Not Available";
            }else{
                tue = hopJson.getString("USER_Tue");
            }
            if(hopJson.getString("USER_Wed").equals("null")){
                wed = "Not Available";
            }else{
                wed = hopJson.getString("USER_Wed");
            }
            //wed = hopJson.getString("USER_Wed");
            if(hopJson.getString("USER_Thur").equals("null")){
                thu = "Not Available";
            }else{
                thu = hopJson.getString("USER_Thur");
            }
            if(hopJson.getString("USER_Fri").equals("null")){
                fri = "Not Available";
            }else{
                fri= hopJson.getString("USER_Fri");
            }

            //thu = hopJson.getString("USER_Thur");
            //fri= hopJson.getString("USER_Fri");
            if(hopJson.getString("USER_Sat").equals("null")){
                sat = "Not Available";
            }else{
                sat = hopJson.getString("USER_Sat");
            }
            //sat = hopJson.getString("USER_Sat");
            if(hopJson.getString("USER_Sun").equals("null")){
                sun = "Not Available";
            }else{
                sun = hopJson.getString("USER_Sun");
            }
            hop = "Mon: "+mon+", "+"Tue: "+tue+", "+"Wed: "+wed+", "+"Thur: "+thu+", "+"Fri: "+fri+", "+"Sat: "+sat+", "+"Sun: "+sun;
        } catch (JSONException e) {
            hopJson = new JSONObject();
            hop = new String();
        }
        return  hop;
    }
}