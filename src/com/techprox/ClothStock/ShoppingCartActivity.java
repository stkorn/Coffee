package com.techprox.ClothStock;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.techprox.ClothStock.adapter.CartAdapter;
import com.techprox.ClothStock.model.CartItem;
import com.techprox.ClothStock.model.ProductItem;

import java.util.*;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class ShoppingCartActivity extends Activity{

    private Context mContext;
    ListView cartListView;
    ArrayList<Integer> item;
    HashMap<Integer, Integer> amountItem;

    TextView pickupTime;
    Button selectTime;

    private SQLiteDatabase database;
    private static final String DB_NAME = "clothstock2";

    private static final String PRODUCT_TABLE = "product";
    private static final String IMAGE_TABLE = "image";
    private static final String PRODUCT_ID = "proid";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "price";
    private static final String CATAGORY = "catagory";
    private static final String IMAGE_ID = "imgid";
    private static final String IMAGE_NAME = "imgname";

    int totalprice = 0;
    int[] deliveryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcartview);
        mContext = getApplicationContext();

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
        TextView total = (TextView) findViewById(R.id.totalprice);
        selectTime = (Button) findViewById(R.id.selecttime);
        pickupTime = (TextView) findViewById(R.id.pickuptime);

        pickupTime.setText(deliveryTime[0] + ":" + deliveryTime[1]);

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


        //Copy database to package
        //Our key helper
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(mContext, DB_NAME);
        database = dbOpenHelper.openDataBase();

        database.beginTransaction();

        try {
        item = ShoppingItem.getItemList();
        amountItem = ShoppingItem.getAmount();
        for (int k = 0; k < item.size(); k++) {
            int id = item.get(k);


            //Get shirth product
            Cursor stuCursor = database.query(PRODUCT_TABLE, new String[] {PRODUCT_ID,
                    PRODUCT_NAME, PRODUCT_PRICE, CATAGORY}, PRODUCT_ID + "=" + "\""+id+"\"", null, null, null, PRODUCT_ID);

            stuCursor.moveToFirst();



            //Get content
            String name;
            int price;
            int amount;
            name = stuCursor.getString(1);
            price = stuCursor.getInt(2);
            amount = amountItem.get(id);

            totalprice += price*amount;


            stuCursor.close();


            Cursor imgCursor = database.query(IMAGE_TABLE, new String[]{IMAGE_ID, IMAGE_NAME, PRODUCT_ID},
                    PRODUCT_ID + "=" + "\"" + id + "\"", null, null, null, IMAGE_ID);

            imgCursor.moveToFirst();

            String imgname = imgCursor.getString(1);



            imgCursor.close();


            adapter.add(new CartItem(imgname, name, price, amount));

        }

        database.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
            database.endTransaction();
        }

        database.endTransaction();

        cartListView.setAdapter(adapter);
        total.setText("$"+totalprice);

        cartListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertB = new AlertDialog.Builder(ShoppingCartActivity.this);
                alertB  .setTitle("Delete")
                        .setMessage("Delete this item from cart?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete item
                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                                cartListView.setAdapter(adapter);

                                ShoppingItem.deleteitem(position);
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
