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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.techprox.ClothStock.model.ProductItem;
import org.w3c.dom.Text;

import java.util.ArrayList;
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

    private TextView coldType;
    private TextView hotType;
    private TextView frappeType;

    final int wipPrice = 15;
    final int caramelPrice = 5;
    final int syrupPrice = 5;

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
        final TextView priceTv = (TextView) findViewById(R.id.price);
        final CheckBox wipChk = (CheckBox) findViewById(R.id.wip);
        final CheckBox caramelChk = (CheckBox) findViewById(R.id.caramel);
        final CheckBox syrupChk = (CheckBox) findViewById(R.id.syrup);

        coldType = (TextView) findViewById(R.id.cold);
        hotType = (TextView) findViewById(R.id.hot);
        frappeType = (TextView) findViewById(R.id.frappe);

        final ImageView cartpic = (ImageView) findViewById(R.id.imgcart);
        final TextView addCartTv = (TextView) findViewById(R.id.carttext);

        LinearLayout addCartBtn = (LinearLayout) findViewById(R.id.addcart);
        LinearLayout back = (LinearLayout) findViewById(R.id.backtomenu);
        LinearLayout checkout = (LinearLayout) findViewById(R.id.checkout);

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

            }
        });

        hotType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHotType(true);
            }
        });

        frappeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrappeType(true);
            }
        });

        wipChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (wipChk.isChecked()) {
                    price += wipPrice;
                    priceTv.setText(price);
                } else {
                    price -= wipPrice;
                    priceTv.setText(price);
                }
            }
        });

        caramelChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (caramelChk.isChecked()) {
                    price += caramelPrice;
                    priceTv.setText(price);
                } else {
                    price -= caramelPrice;
                    priceTv.setText(price);
                }
            }
        });

        syrupChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (syrupChk.isChecked()) {
                    price += syrupPrice;
                    priceTv.setText(price);
                } else {
                    price -= syrupPrice;
                    priceTv.setText(price);
                }
            }
        });


        Intent pro = getIntent();
        final String namePro = pro.getStringExtra("name");
        final int price = pro.getIntExtra("price", 0);
        final String cata = pro.getStringExtra("cata");
        final int proid = pro.getIntExtra("proid", 0);

        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ShoppingItem.addCart(proid);


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

        nameTv.setText(namePro);
        priceTv.setText(price + "");








        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        database = dbOpenHelper.openDataBase();


        //Get image shirth product
        imageProduct = new ArrayList<Integer>();
        Cursor stuCursor = database.query(IMAGE_TABLE, new String[]{IMAGE_ID, IMAGE_NAME, PRODUCT_ID},
                PRODUCT_ID + "=" + "\"" + proid + "\"", null, null, null, IMAGE_ID);

        stuCursor.moveToFirst();
        imageCart = stuCursor.getString(1);
        if (!stuCursor.isAfterLast()) {
            do {
                //Get content
                String imgname = stuCursor.getString(1);
                imageProduct.add(getResources().getIdentifier(imgname, "drawable", getPackageName()));

            } while (stuCursor.moveToNext());
        }
        stuCursor.close();

        ViewPager viewPager = (ViewPager) findViewById(R.id.image_pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter();
        viewPager.setAdapter(mAdapter);

    }

    private void setColdType(boolean t) {
        if (t) {
            coldType.setTextColor(Color.WHITE);
            coldType.setBackgroundColor(Color.BLACK);
            type = 1;
            setHotType(false);
            setFrappeType(false);
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
            setColdType(false);
            setFrappeType(false);
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
            setColdType(false);
            setHotType(false);
        } else {
            frappeType.setTextColor(Color.BLACK);
            frappeType.setBackgroundColor(Color.WHITE);
        }


    }

    private void changeSize() {

        for (int i = 0; i < sizeBox.length; i++) {
            if (i == boxNum) {
                sizeBox[i].setBackgroundResource(R.drawable.sizebox);
            } else {
                sizeBox[i].setBackgroundResource(R.drawable.sizebox_unselected);
            }
        }

    }


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
