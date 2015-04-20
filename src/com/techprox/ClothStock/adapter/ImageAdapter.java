package com.techprox.ClothStock.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.techprox.ClothStock.R;

/**
 * Created by stkornsmc on 2/7/14 AD.
 */
public class ImageAdapter extends PagerAdapter {

    Context context;
    private int[] GalImages = new int[] {
            R.drawable.cup1,
            R.drawable.cup1,
            R.drawable.cup1
    };
    public ImageAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView name = new TextView(context);

        ImageView imageView = new ImageView(context);
        int padding = 10;
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(GalImages[position]);

        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

}
