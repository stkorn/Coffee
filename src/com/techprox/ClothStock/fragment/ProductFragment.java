package com.techprox.ClothStock.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.devsmart.android.ui.HorizontalListView;
import com.techprox.ClothStock.CustomHorizonScroll;
import com.techprox.ClothStock.ExternalDbOpenHelper;
import com.techprox.ClothStock.ProductFullActivity;
import com.techprox.ClothStock.R;
import com.techprox.ClothStock.adapter.ImageAdapter;
import com.techprox.ClothStock.adapter.ProductAdapter;
import com.techprox.ClothStock.model.ProductItem;

/**
 * Created by stkornsmc on 2/7/14 AD.
 */
public class ProductFragment extends Fragment {

    private static final String APP_TAG = "clothshop";

    private ProductAdapter shirtAdapter;
    private ProductAdapter footAdapter;
    private CustomHorizonScroll shirtListView;
    private CustomHorizonScroll footListView;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.catalogview, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ImageAdapter mAdapter = new ImageAdapter(getActivity());
        viewPager.setAdapter(mAdapter);

        shirtListView = (CustomHorizonScroll) view.findViewById(R.id.shirt_list);
        footListView = (CustomHorizonScroll) view.findViewById(R.id.foot_list);

        //Create new adapter
        shirtAdapter = new ProductAdapter(getActivity());
        footAdapter = new ProductAdapter(getActivity());

        //Copy database to package
        //Our key helper

        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(getActivity(), DB_NAME);
        database = dbOpenHelper.openDataBase();


        database.beginTransaction();
        try {


            //Get shirth product
            Cursor stuCursor = database.query(PRODUCT_TABLE, new String[]{PRODUCT_ID,
                    PRODUCT_NAME, PRODUCT_PRICE, CATAGORY}, CATAGORY + "=" + "\"shirt\"", null, null, null, PRODUCT_ID);

            stuCursor.moveToFirst();
            if (!stuCursor.isAfterLast()) {
                do {

                    //Get content
                    int proID = stuCursor.getInt(0);
                    String name = stuCursor.getString(1);
                    int price = stuCursor.getInt(2);
                    String cata = stuCursor.getString(3);

                    //Get image
                    Cursor imgCursor = database.query(IMAGE_TABLE, new String[]{IMAGE_ID, IMAGE_NAME, PRODUCT_ID}
                            , PRODUCT_ID + "=" + proID, null, null, null, IMAGE_ID);

                    imgCursor.moveToFirst();
                    String imgName = imgCursor.getString(1);

                    shirtAdapter.add(new ProductItem(imgName, name, price, proID, cata));

                    imgCursor.close();

                } while (stuCursor.moveToNext());
            }
            stuCursor.close();

            //Update new data adapter
            shirtListView.setAdapter(shirtAdapter);


            //Get footwear product
            Cursor ftCursor = database.query(PRODUCT_TABLE, new String[]{PRODUCT_ID,
                    PRODUCT_NAME, PRODUCT_PRICE, CATAGORY}, CATAGORY + "=" + "\"footwear\"", null, null, null, PRODUCT_ID);

            ftCursor.moveToFirst();
            if (!ftCursor.isAfterLast()) {
                do {

                    //Get content
                    int proID = ftCursor.getInt(0);
                    String name = ftCursor.getString(1);
                    int price = ftCursor.getInt(2);
                    String cata = ftCursor.getString(3);

                    //Get image
                    Cursor imgCursor = database.query(IMAGE_TABLE, new String[]{IMAGE_ID, IMAGE_NAME, PRODUCT_ID}
                            , PRODUCT_ID + "=" + "\"" + proID + "\"", null, null, null, IMAGE_ID);

                    imgCursor.moveToFirst();
                    String imgName = imgCursor.getString(1);

                    footAdapter.add(new ProductItem(imgName, name, price, proID, cata));

                    imgCursor.close();

                } while (ftCursor.moveToNext());
            }
            ftCursor.close();

            database.setTransactionSuccessful();

        } catch (Exception e) {
            database.endTransaction();
            e.printStackTrace();

        }

        database.endTransaction();

        //Update new data adapter
        footListView.setAdapter(footAdapter);

        shirtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ProductFullActivity.class);
                ProductItem item = shirtAdapter.getItem(position);
                String namePro = item.nameProduct;
                String cata = item.cata;
                int price = item.price;
                int proid = item.id;
                i.putExtra("name", namePro);
                i.putExtra("cata", cata);
                i.putExtra("price", price);
                i.putExtra("proid", proid);
                getActivity().startActivity(i);
            }
        });

        footListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ProductFullActivity.class);
                ProductItem item = footAdapter.getItem(position);
                String namePro = item.nameProduct;
                String cata = item.cata;
                int price = item.price;
                int proid = item.id;
                i.putExtra("name", namePro);
                i.putExtra("cata", cata);
                i.putExtra("price", price);
                i.putExtra("proid", proid);
                getActivity().startActivity(i);
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }
}
