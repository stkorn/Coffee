package com.techprox.ClothStock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stkornsmc on 4/22/15 AD.
 */
public class TrackOrderActivity extends Activity {

    private Context mContext;

    private TextView status1;
    private TextView status2;
    private Button cancelBtn;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackorder);

        Intent i = getIntent();

        status1 = (TextView) findViewById(R.id.status1);
        status2 = (TextView) findViewById(R.id.status2);
        cancelBtn = (Button) findViewById(R.id.cancelOrder);

        String uri = String.format("http://localhost/coffee/public/track?id=%1$s", id);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                String status = response.getString("status");
                                updateStatus();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);


    }

    private void updateStatus() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
