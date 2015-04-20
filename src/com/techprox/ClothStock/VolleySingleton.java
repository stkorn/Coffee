package com.techprox.ClothStock;

import android.content.Context;
import android.graphics.Bitmap;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by stkornsmc on 4/20/15 AD.
 */
public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context);


    }


    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("App");
        getRequestQueue().add(req);
    }

}
