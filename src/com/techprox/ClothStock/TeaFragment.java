package com.techprox.ClothStock;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.techprox.ClothStock.adapter.ProductAdapter;
import com.techprox.ClothStock.model.ProductItem;

/**
 * Created by stkornsmc on 2/11/14 AD.
 */
public class TeaFragment extends Fragment {

    private Context mContext;

    private static String cata = "footwear";

    private int viewShow = 0; // 0 = gridview , 1 = listview
    private GridView gridView;
    private ProductAdapter footAdapter;

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
        mContext = getActivity();
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(getActivity(), DB_NAME);
        database = dbOpenHelper.openDataBase();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (viewShow == 0) {
             view = inflater.inflate(R.layout.shirtgridview, container, false);
        } else {
            //set list view
            view = inflater.inflate(R.layout.shirtgridview, container, false);

        }

        gridView = (GridView) view.findViewById(R.id.gridView);

        footAdapter = new ProductAdapter(mContext);

        //Get footwear product
        Cursor ftCursor = database.query(PRODUCT_TABLE, new String[] {PRODUCT_ID,
                PRODUCT_NAME, PRODUCT_PRICE, CATAGORY}, CATAGORY + "=" + "\"" + cata + "\"", null, null, null, PRODUCT_ID);

        ftCursor.moveToFirst();
        if(!ftCursor.isAfterLast()) {
            do {

                //Get content
                int proID = ftCursor.getInt(0);
                String name = ftCursor.getString(1);
                int price = ftCursor.getInt(2);
                String cata = ftCursor.getString(3);

                //Get image
                Cursor imgCursor = database.query(IMAGE_TABLE, new String[] {IMAGE_ID, IMAGE_NAME, PRODUCT_ID}
                        , PRODUCT_ID + "=" + "\"" + proID + "\"", null, null, null, IMAGE_ID);

                imgCursor.moveToFirst();
                String imgName = imgCursor.getString(1);

                footAdapter.add(new ProductItem(imgName, name, price, proID, cata));

                imgCursor.close();

            } while (ftCursor.moveToNext());
        }
        ftCursor.close();

        gridView.setAdapter(footAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    public void onResume() {
        super.onResume();
        Log.i("app", "onResume from footwear");

    }
}
