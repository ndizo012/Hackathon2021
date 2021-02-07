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
    public JSONObject getName(int number) {
        JSONObject nameJson;
        try {
            nameJson = (JSONObject) ((JSONObject) locations.get(number)).get("attributes");
        } catch (JSONException e) {
            nameJson = new JSONObject();
        }
        return  nameJson;
    }
}