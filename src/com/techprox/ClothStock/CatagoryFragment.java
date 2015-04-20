package com.techprox.ClothStock;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stkornsmc on 2/11/14 AD.
 */
public class CatagoryFragment extends Fragment {

    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;

    CoffeeFragment shFragment;
    TeaFragment ftFragment;

    private static final String[] titles = { "COLD", "HOT"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final PagerTabStrip strip = (PagerTabStrip) getActivity().findViewById(R.id.pager_title_strip);
//        strip.setDrawFullUnderline(false);
//        strip.setNonPrimaryAlpha(0.5f);
//        strip.setTextSpacing(25);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.catagoryview, container, false);
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mCollectionPagerAdapter =
                new CollectionPagerAdapter(
                        getActivity().getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);


        PagerTabStrip pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pager_title_strip);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTabIndicatorColor(Color.BLACK);



//        PagerTabStrip strip = PagerTabStrip.class.cast(view.findViewById(R.id.pager_title_strip));
//        strip.setDrawFullUnderline(false);
//        strip.setTabIndicatorColor(Color.DKGRAY);
//        strip.setBackgroundColor(Color.GRAY);
//        strip.setNonPrimaryAlpha(0.5f);
//        strip.setTextSpacing(25);
//        strip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

//        final ActionBar actionBar = getActivity().getActionBar();


        // Specify that tabs should be displayed in the action bar.
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
//        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // show the given tab
//                mViewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // hide the given tab
//            }
//
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // probably ignore this event
//            }
//        };
//
//        // Add 2 tabs, specifying the tab's text and TabListener
//        actionBar.addTab(actionBar.newTab().setText("SHIRT").setTabListener(tabListener));
//        actionBar.addTab(actionBar.newTab().setText("FOOTWEAR").setTabListener(tabListener));

//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                getActivity().getActionBar().setSelectedNavigationItem(i);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });

        return view;
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
                    shFragment = new CoffeeFragment();
                    return shFragment;
                case 1:
                    ftFragment = new TeaFragment();
                    return ftFragment;

                default: shFragment = new CoffeeFragment();
                    return shFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (shFragment != null)
            getFragmentManager().beginTransaction().remove(shFragment).commit();

        if (ftFragment != null)
            getFragmentManager().beginTransaction().remove(ftFragment).commit();
    }
}
