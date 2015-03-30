package com.techprox.ClothStock.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.techprox.ClothStock.R;

/**
 * Created by stkornsmc on 2/7/14 AD.
 */
public class ImageAdapter extends PagerAdapter {

    Context context;
    private int[] GalImages = new int[] {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
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
        ImageView imageView = new ImageView(context);
        int padding = 0;
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(GalImages[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

}
