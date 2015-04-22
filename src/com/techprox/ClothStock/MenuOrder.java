package com.techprox.ClothStock;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import com.techprox.ClothStock.model.OtherFragment;

/**
 * Created by stkornsmc on 2/11/14 AD.
 */
public class MenuOrder extends Activity {

    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;

    CoffeeFragment coffeeFragment;
    TeaFragment teaFragment;
    OtherFragment otherFragment;

    private static final String[] titles = { "COFFEE", "TEA", "CHOCOLATE"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catagoryview);
//        final PagerTabStrip strip = (PagerTabStrip) getActivity().findViewById(R.id.pager_title_strip);
//        strip.setDrawFullUnderline(false);
//        strip.setNonPrimaryAlpha(0.5f);
//        strip.setTextSpacing(25);

        mCollectionPagerAdapter = new CollectionPagerAdapter(this.getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);


        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTabIndicatorColor(Color.BLACK);



    }




    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    coffeeFragment = new CoffeeFragment();
                    return coffeeFragment;
                case 1:
                    teaFragment = new TeaFragment();
                    return teaFragment;
                case 2:
                    otherFragment = new OtherFragment();
                    return otherFragment;

                default: coffeeFragment = new CoffeeFragment();
                    return coffeeFragment;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }


}
