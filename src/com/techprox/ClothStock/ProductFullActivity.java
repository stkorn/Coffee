package com.techprox.ClothStock;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techprox.ClothStock.model.ProductItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stkornsmc on 2/12/14 AD.
 */
public class ProductFullActivity extends Activity {

    ArrayList<Integer> imageProduct;
    TextView sizeBox[] = new TextView[3];
    int boxNum;

    private SQLiteDatabase database;
    private static final String DB_NAME = "clothstock2";

    private static final String IMAGE_TABLE = "image";
    private static final String PRODUCT_ID = "proid";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "price";
    private static final String CATAGORY = "catagory";
    private static final String IMAGE_ID = "imgid";
    private static final String IMAGE_NAME = "imgname";

    private boolean addedToCart = false;
    private String imageCart;
    private String size;
    private String cata;
    private int type; // 1:COLD  2:HOT  3:FRAPPE

    private TextView priceTv;

    private TextView coldType;
    private TextView hotType;
    private TextView frappeType;

    final int wipPrice = 15;
    final int caramelPrice = 5;
    final int syrupPrice = 5;

    int priceCold;
    int priceHot;
    int priceFrappe;

    CheckBox wipChk = null;
    CheckBox caramelChk = null;
    CheckBox syrupChk = null;

    private Button addQuan;
    private Button minusQuan;
    private TextView showQuan;

    private int quantity = 1;

    private int admix = 0; //2 = wipcream 3 = caramel 4 = syrup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productfullview);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.icon);


        TextView nameTv = (TextView) findViewById(R.id.name);
        priceTv = (TextView) findViewById(R.id.price);
        wipChk = (CheckBox) findViewById(R.id.wip);
        caramelChk = (CheckBox) findViewById(R.id.caramel);
        syrupChk = (CheckBox) findViewById(R.id.syrup);

        coldType = (TextView) findViewById(R.id.cold);
        hotType = (TextView) findViewById(R.id.hot);
        frappeType = (TextView) findViewById(R.id.frappe);

        final ImageView cartpic = (ImageView) findViewById(R.id.imgcart);
        final TextView addCartTv = (TextView) findViewById(R.id.carttext);

        LinearLayout addCartBtn = (LinearLayout) findViewById(R.id.addcart);
        LinearLayout back = (LinearLayout) findViewById(R.id.backtomenu);
        LinearLayout checkout = (LinearLayout) findViewById(R.id.checkout);

        addQuan = (Button) findViewById(R.id.addQ);
        minusQuan = (Button) findViewById(R.id.minusQ);
        showQuan = (TextView) findViewById(R.id.quan);

        showQuan.setText(String.valueOf(quantity));

        priceCold = 0;
        priceHot = 0;
        priceFrappe = 0;


        addQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity += 1;
                showQuan.setText(String.valueOf(quantity));
                updatePrice();

            }
        });

        minusQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity-1 != 0) {
                    quantity -= 1;
                    showQuan.setText(String.valueOf(quantity));
                    updatePrice();

                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                startActivity(i);
            }
        });

        coldType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColdType(true);
                updatePrice();

            }
        });

        hotType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHotType(true);
                updatePrice();

            }
        });

        frappeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrappeType(true);
                updatePrice();

            }
        });

        wipChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (wipChk.isChecked()) {
                    price += wipPrice;
                    admix = 2;
                    caramelChk.setChecked(false);
                    syrupChk.setChecked(false);
                    updatePrice();


                } else {
                    price -= wipPrice;
                    admix = 0;
                    updatePrice();

                }
            }
        });

        caramelChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (caramelChk.isChecked()) {
                    price += caramelPrice;
                    admix = 3;
                    wipChk.setChecked(false);
                    syrupChk.setChecked(false);
                    updatePrice();

                } else {
                    price -= caramelPrice;
                    admix = 0;
                    updatePrice();

                }
            }
        });

        syrupChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (syrupChk.isChecked()) {
                    price += syrupPrice;
                    admix = 4;
                    wipChk.setChecked(false);
                    caramelChk.setChecked(false);
                    updatePrice();

                } else {
                    price -= syrupPrice;
                    admix = 0;
                    updatePrice();

                }
            }
        });


        Intent pro = getIntent();
        String nm =  pro.getStringExtra("name");
        if (nm.contains(" ")) {
            nm = nm.replace(" ", "%20");
        }
        final String namePro = nm;
        final int price = pro.getIntExtra("price", 0);
        final String cata = pro.getStringExtra("cata");
        final int proid = pro.getIntExtra("proid", 0);
        final String img = pro.getStringExtra("img");
        type = Integer.parseInt(pro.getStringExtra("type"));
        switch (type) {
            case 10:
                setHotType(true);
                break;
            case 20:
                setColdType(true);
                break;
            case 30:
                setFrappeType(true);
                break;
        }

        nameTv.setText(namePro.replace("%20", " "));
        priceTv.setText(String.valueOf(price));
        imageProduct = new ArrayList<Integer>();
        imageProduct.add(getResources().getIdentifier(img, "drawable", getPackageName()));


        // Query get Price for others type

        String uri = String.format("http://10.0.2.2/coffee/public/menu/type?name=%1$s", namePro);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "Load... ", 1000).show();

                        try {


                            boolean suc = response.getBoolean("success");
                            if (suc) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int pr = obj.getInt("BV_UnitPrice");
                                    String type = obj.getString("BV_Type");
                                    if (type.matches("10")) {
                                        priceHot = pr;
                                    }
                                    if (type.matches("20")) {
                                        priceCold = pr;
                                    }
                                    if (type.matches("30")) {
                                        priceFrappe = pr;
                                    }
                                }

                                if (priceCold == 0) {
                                    coldType.setClickable(false);
                                    coldType.setTextColor(Color.WHITE);
                                    coldType.setBackgroundColor(Color.GRAY);
                                }
                                if (priceHot == 0) {
                                    hotType.setClickable(false);
                                    hotType.setTextColor(Color.WHITE);
                                    hotType.setBackgroundColor(Color.GRAY);
                                }
                                if (priceFrappe == 0) {
                                    frappeType.setClickable(false);
                                    frappeType.setTextColor(Color.WHITE);
                                    frappeType.setBackgroundColor(Color.GRAY);
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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


        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> item = new HashMap<String, String>();
                item.put("id", String.valueOf(proid));
                item.put("name", namePro);
                item.put("type", String.valueOf(type));
                item.put("admix", String.valueOf(admix));
                item.put("price", priceTv.getText().toString());
                item.put("quantity", String.valueOf(quantity));
                item.put("img", img);


                ShoppingItem.addCart(item);


                AlertDialog.Builder alertB = new AlertDialog.Builder(ProductFullActivity.this);
                alertB
                        .setMessage("Add to order already.")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert = alertB.create();
                alert.show();

            }
        });




        ViewPager viewPager = (ViewPager) findViewById(R.id.image_pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter();
        viewPager.setAdapter(mAdapter);

    }

    private void updatePrice() {
        int totalPrice;
        int priceType = 0;
        int priceAdmix = 0;

        switch (type) {
            case 1:
                priceType = priceCold;
                break;
            case 2:
                priceType = priceHot;
                break;
            case 3:
                priceType = priceFrappe;
        }

        switch (admix) {
            case 0 :
                priceAdmix = 0;
                break;
            case 2:
                priceAdmix = wipPrice;
                break;
            case 3:
                priceAdmix = caramelPrice;
                break;
            case 4:
                priceAdmix = syrupPrice;
        }

        totalPrice = priceType * quantity + priceAdmix;

        priceTv.setText(String.valueOf(totalPrice));

    }

//    private String addMixPrice(int pr) {
//        int price = pr;
//
//        if (wipChk.isChecked()) {
//            price += wipPrice;
//        }
//
//        if (caramelChk.isChecked()) {
//            price += caramelPrice;
//        }
//
//        if (syrupChk.isChecked()) {
//            price += syrupPrice;
//        }
//
//        return String.valueOf(price);
//
//    }
//
//    private void setPrice(int pr) {
//        priceTv.setText(String.valueOf(pr));
//    }

    private void setColdType(boolean t) {
        if (t) {
            coldType.setTextColor(Color.WHITE);
            coldType.setBackgroundColor(Color.BLACK);
            type = 1;
            if (priceHot!=0) {
                setHotType(false);
            }
            if (priceFrappe!=0) {
                setFrappeType(false);
            }
        } else {
            coldType.setTextColor(Color.BLACK);
            coldType.setBackgroundColor(Color.WHITE);
        }


    }

    private void setHotType(boolean t) {
        if (t) {
            hotType.setTextColor(Color.WHITE);
            hotType.setBackgroundColor(Color.BLACK);
            type = 2;
            if (priceCold!=0) {
                setColdType(false);
            }
            if (priceFrappe!=0) {
                setFrappeType(false);
            }
        } else {
            hotType.setTextColor(Color.BLACK);
            hotType.setBackgroundColor(Color.WHITE);
        }


    }

    private void setFrappeType(boolean t) {
        if (t) {
            frappeType.setTextColor(Color.WHITE);
            frappeType.setBackgroundColor(Color.BLACK);
            type = 3;
            if (priceCold!=0) {
                setColdType(false);
            }
            if (priceHot!=0) {
                setHotType(false);
            }
        } else {
            frappeType.setTextColor(Color.BLACK);
            frappeType.setBackgroundColor(Color.WHITE);
        }


    }

//    private void changeSize() {
//
//        for (int i = 0; i < sizeBox.length; i++) {
//            if (i == boxNum) {
//                sizeBox[i].setBackgroundResource(R.drawable.sizebox);
//            } else {
//                sizeBox[i].setBackgroundResource(R.drawable.sizebox_unselected);
//            }
//        }
//
//    }


    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageProduct.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == ((ImageView) o);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Context context = ProductFullActivity.this;
            ImageView imageView = new ImageView(context);
            int padding = 15;
            imageView.setPadding(padding, padding, padding, padding);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imageProduct.get(position));
            ((ViewPager) container).addView(imageView, 0);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        Intent i = new Intent(getApplicationContext(), ImageViewerActivity.class);
//                        i.putExtra("url", imageListUrl.get(position));
//                        startActivity(i);
                }
            });


            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title

        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_settings:
//                return true;
            case R.id.cart:
                Intent i = new Intent(ProductFullActivity.this, ShoppingCartActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
