package com.techprox.ClothStock;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.techprox.ClothStock.adapter.CartAdapter;
import com.techprox.ClothStock.model.CartItem;
import com.techprox.ClothStock.model.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class ShoppingCartActivity extends Activity{

    private Context mContext;
    ListView cartListView;
    ArrayList<Integer> item;
    HashMap<Integer, Integer> amountItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcartview);
        mContext = getApplicationContext();




        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.deadstock_icon2);

        final CartAdapter adapter = new CartAdapter(mContext);

        cartListView = (ListView) findViewById(R.id.list);
        TextView total = (TextView) findViewById(R.id.totalprice);



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
