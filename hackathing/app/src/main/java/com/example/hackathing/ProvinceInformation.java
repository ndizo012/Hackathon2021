package com.example.hackathing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProvinceInformation {
    private JSONArray provinceInformation;
    public enum Province {
        Alberta,
        BritishColumbia,
        Manitoba,
        NewBrunswick,
        NewfoundlandAndLabrador,
        NorthwestTerritories,
        NovaScotia,
        Nunavut,
        Ontario,
        PrinceEdwardIsland,
        Quebec,
        Saskatchewan,
        Yukon
    }

    public ProvinceInformation(JSONArray provinceInformation){
        this.provinceInformation = provinceInformation;
    }

    private int enumToInt(Province province){
        int result;
        switch(province){
            case Alberta:
                return 0;
            case BritishColumbia:
                return 1;
            case Manitoba:
                return 2;
            case NewBrunswick:
                return 3;
            case NewfoundlandAndLabrador:
                return 4;
            case NorthwestTerritories:
                return 5;
            case NovaScotia:
                return 6;
            case Nunavut:
                return 7;
            case Ontario:
                return 8;
            case PrinceEdwardIsland:
                return 9;
            case Quebec:
                return 10;
            case Saskatchewan:
                return 11;
            case Yukon:
                return 12;
            default:
                return 13;
        }
    }

    public int getCumulativeCases(Province province) {
        try {
            return (int) ((JSONObject) provinceInformation.get(enumToInt(province))).get("cumulative_cases");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getActiveCases(Province province) {
        try {
            return (int) ((JSONObject) provinceInformation.get(enumToInt(province))).get("active_cases");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getRecovered(Province province) {
        try {
            return (int) ((JSONObject) provinceInformation.get(enumToInt(province))).get("recovered");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
