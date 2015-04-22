package com.techprox.ClothStock;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techprox.ClothStock.adapter.CartAdapter;
import com.techprox.ClothStock.model.CartItem;
import com.techprox.ClothStock.model.ProductItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class ShoppingCartActivity extends Activity{

    private Context mContext;
    ListView cartListView;
    ArrayList<HashMap<String, String>> item;
//    HashMap<Integer, Integer> amountItem;

    TextView pickupTime;
    Button selectTime;

    SharedPreferences prefs;

    int totalprice = 0;
    int[] deliveryTime;
    private Button checkout;

    String name = "";

    int sentRequest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcartview);
        mContext = getApplicationContext();

        prefs = mContext.getSharedPreferences("com.techprox.ClothStock", Context.MODE_PRIVATE);
        name = prefs.getString("name", "");

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        deliveryTime = getDeliveryTime(hour, minute);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.icon);

        final CartAdapter adapter = new CartAdapter(mContext);

        cartListView = (ListView) findViewById(R.id.list);
        final TextView total = (TextView) findViewById(R.id.totalprice);
        selectTime = (Button) findViewById(R.id.selecttime);
        pickupTime = (TextView) findViewById(R.id.pickuptime);


        pickupTime.setText(String.format("%d:%d", deliveryTime[0], deliveryTime[1]));

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                final int hourD = deliveryTime[0];
                final int minuteD = deliveryTime[1];
                mTimePicker = new TimePickerDialog(ShoppingCartActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > hourD) {
                            pickupTime.setText(selectedHour + ":" + selectedMinute);
                        } else if (selectedHour == hourD && selectedMinute >= minuteD) {
                            pickupTime.setText(selectedHour + ":" + selectedMinute);
                        }
                        else {
                            Toast.makeText(mContext, "Your pick up time can't delivery", 200).show();
                            pickupTime.setText(hourD + ":" + minuteD);
                        }
                    }
                }, hourD, minuteD, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Pick up Time");
                mTimePicker.show();
            }
        });



        item = ShoppingItem.getItemList();
//        amountItem = ShoppingItem.getAmount();
        for (int k = 0; k < item.size(); k++) {



            //Get content
            String name;
            int id = Integer.parseInt(item.get(k).get("id"));
            int price;
            int amount;
            name = item.get(k).get("name");
            price = Integer.parseInt(item.get(k).get("price"));
            amount = Integer.parseInt(item.get(k).get("quantity"));

//            totalprice += price;



            String imgname = item.get(k).get("img");
            int mix = Integer.parseInt(item.get(k).get("admix"));

            adapter.add(new CartItem(imgname, name, price, amount, mix));

        }

        totalprice = ShoppingItem.getTotal();

        cartListView.setAdapter(adapter);
        total.setText("฿" + totalprice);

        cartListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertB = new AlertDialog.Builder(ShoppingCartActivity.this);
                alertB.setTitle("Delete")
                        .setMessage("Delete this item from cart?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete item
                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                                cartListView.setAdapter(adapter);

                                ShoppingItem.deleteitem(position);

                                totalprice = ShoppingItem.getTotal();
                                total.setText("฿" + totalprice);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert = alertB.create();
                alert.show();

                return false;
            }
        });

        checkout = (Button) findViewById(R.id.sendOrder);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alert = new AlertDialog.Builder(ShoppingCartActivity.this)
                        .setTitle("Sent your order")
                        .setMessage("Are you sure to sent your order?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                processSentOrder();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();




            }
        });


    }

    private void processSentOrder() {
        // volley send order
        int totalUnit = ShoppingItem.getTotalUnit();
        String pickT =  pickupTime.getText().toString();
        String uri = String.format("http://10.0.2.2/coffee/public/order/send?user=%1$s&ttprice=%2$s&ttunit=%3$s&time=%4$s", name, totalprice, totalUnit, pickT);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            boolean suc = response.getBoolean("success");
                            if (suc) {
                                processSendItem();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), 3000).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), 3000).show();

                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);







    }

    private void processSendItem() {


        // volley create orderItem from ShoppingItem
        for (int i = 0; i < item.size(); i++) {

            HashMap<String, String> obj = item.get(i);
            int unit = Integer.parseInt(obj.get("quantity"));
            int price = Integer.parseInt(obj.get("price"));
            int admix = Integer.parseInt(obj.get("admix"));
            int bvid = Integer.parseInt(obj.get("id"));


            String urii = String.format("http://10.0.2.2/coffee/public/order/store?unit=%1$s&price=%2$s&admix=%3$s&bvid=%4$s&name=%5$s", unit, price, admix, bvid, name);
            JsonObjectRequest reqOrder = new JsonObjectRequest(Request.Method.GET,
                    urii,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();



                            try {


                                boolean suc = response.getBoolean("success");
                                if (suc) {
                                    String id = response.getString("id");
                                    alertFinish(item.size(), id);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("error", e.toString());

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString() + "EKIZAI", Toast.LENGTH_LONG).show();

                        }
                    });

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(reqOrder);
        }




    }

    private void alertFinish(int size, String id) {
        sentRequest += 1;
        if (sentRequest == size) {
//            AlertDialog alert = new AlertDialog.Builder(this)
//                    .setTitle("Order send")
//                    .setMessage("Your order send already")
//                    .setIcon(android.R.drawable.ic_dialog_info)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            sentRequest = 0;
//                        }
//                    }).show();
            Intent i = new Intent(this, TrackOrderActivity.class);
            i.putExtra("id", id);
            startActivity(i);

        }

    }

    private int[] getDeliveryTime(int hour, int minute) {
        int[] time = new int[2];
//        int minutePlus = ((minute + 15) / 5) * 5 ;
        int minutePlus = minute + 15;

        if (minutePlus > 60) {
            minutePlus -= 60;
            hour += 1;
        }
        time[0] = hour;
        time[1] = minutePlus;
        return time;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
