package com.techprox.ClothStock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.techprox.ClothStock.R;
import com.techprox.ClothStock.model.CartItem;
import org.w3c.dom.Text;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class CartAdapter extends ArrayAdapter<CartItem> {

    Context mContext;

    public CartAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cartitemlist, null);

        TextView name = (TextView) view.findViewById(R.id.productname);
        TextView price = (TextView) view.findViewById(R.id.productprice);
        TextView amout = (TextView) view.findViewById(R.id.amount);
        TextView total = (TextView) view.findViewById(R.id.amounttotal);
        ImageView image = (ImageView) view.findViewById(R.id.cartImage);

        int am = getItem(position).amount;
        int pr = getItem(position).price;

        name.setText(getItem(position).nameProduct);
        price.setText("$"+pr);
        amout.setText("Qty X "+am);
        total.setText("Total:\n$"+pr*am);
        int img = mContext.getResources()
                .getIdentifier(getItem(position).imageProduct, "drawable", mContext.getPackageName());
        image.setImageResource(img);


        return view;
    }
}
