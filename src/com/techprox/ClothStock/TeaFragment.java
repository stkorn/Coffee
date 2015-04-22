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
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techprox.ClothStock.adapter.ProductAdapter;
import com.techprox.ClothStock.model.ProductItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stkornsmc on 2/11/14 AD.
 */
public class TeaFragment extends Fragment {

    private Context mContext;

    private static String cata = "TEA";

    private int viewShow = 0; // 0 = gridview , 1 = listview
    private GridView gridView;
    private ProductAdapter teaAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


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

        teaAdapter = new ProductAdapter(mContext);

        String uri = "http://10.0.2.2/coffee/public/menu?cata=2";
        JsonObjectRequest menuReq = new JsonObjectRequest(Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            try {
                                boolean suc = response.getBoolean("success");
                                if (suc) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject list = jsonArray.getJSONObject(i);
                                        int bvid = Integer.parseInt(list.getString("BeveragesID"));
                                        String name = list.getString("BV_Name");
                                        int price = Integer.parseInt(list.getString("BV_UnitPrice"));
                                        String type = list.getString("BV_Type");
                                        String img = list.getString("BV_IMG");
                                        teaAdapter.add(new ProductItem(img, name, price, bvid, type));


                                    }

                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), response.toString(), 3000).show();

                                }

                                gridView.setAdapter(teaAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else

                        {
                            Toast.makeText(getActivity().getApplicationContext(), response.toString(), 3000).show();

                        }
                    }
                }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), 3000).show();

            }
        }

        );

        VolleySingleton.getInstance(mContext).

                addToRequestQueue(menuReq);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                        {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(getActivity(), ProductFullActivity.class);
                                                ProductItem item = teaAdapter.getItem(position);
                                                String namePro = item.nameProduct;
                                                int price = item.price;
                                                int proid = item.id;
                                                String img = item.imageProduct;
                                                String type = item.type;


                                                i.putExtra("name", namePro);
                                                i.putExtra("price", price);
                                                i.putExtra("proid", proid);
                                                i.putExtra("img", img);
                                                i.putExtra("type", type);

                                                getActivity().startActivity(i);
                                            }
                                        }

        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("app", "onResume from footwear");

    }
}
