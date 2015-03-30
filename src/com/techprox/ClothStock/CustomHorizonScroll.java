package com.techprox.ClothStock;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.devsmart.android.ui.HorizontalListView;

/**
 * Created by stkornsmc on 2/7/14 AD.
 */
public class CustomHorizonScroll extends HorizontalListView {

    private float yDistance, lastY;
    private final int threshold = 40;

    public CustomHorizonScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                yDistance = Math.abs(event.getY() - lastY);
                if (yDistance > threshold) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

        }


        return super.onTouchEvent(event);
    }
}
