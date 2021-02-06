package com.example.hackathing;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyController {
    private static VolleyController volleyController;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyController(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized VolleyController getInstance(Context context) {
        if (volleyController == null) {
            volleyController = new VolleyController(context);
        }
        return volleyController;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}