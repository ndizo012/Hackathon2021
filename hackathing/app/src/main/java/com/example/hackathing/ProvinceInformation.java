package com.example.hackathing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProvinceInformation {
    private JSONArray provinceInformation;

    public ProvinceInformation(JSONArray provinceInformation){
        this.provinceInformation = provinceInformation;
    }

    private int stringToInt(String province){
        int result;
        switch(province){
            case "AB":
                return 0;
            case "BC":
                return 1;
            case "MB":
                return 2;
            case "NB":
                return 3;
            case "NL":
                return 4;
            case "NT":
                return 5;
            case "NS":
                return 6;
            case "NU":
                return 7;
            case "ON":
                return 8;
            case "PE":
                return 9;
            case "QC":
                return 10;
            case "SK":
                return 11;
            case "YT":
                return 12;
            default:
                return 13;
        }
    }

    public String getCumulativeCases(String province) {
        try {
            return String.valueOf((Number) ((JSONObject) provinceInformation.get(stringToInt(province))).get("cumulative_cases"));
        } catch (JSONException e) {
            e.printStackTrace();
            return "Not Available";
        }
    }

    public String getActiveCases(String province) {
        try {
            return String.valueOf((Number) ((JSONObject) provinceInformation.get(stringToInt(province))).get("active_cases"));
        } catch (JSONException e) {
            e.printStackTrace();
            return "Not Available";
        }
    }

    public String getRecovered(String province) {
        try {
            return String.valueOf((Number) ((JSONObject) provinceInformation.get(stringToInt(province))).get("recovered"));
        } catch (JSONException e) {
            e.printStackTrace();
            return "Not Available";
        }
    }
}
