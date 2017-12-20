package com.example.pqq.indicatordemo.view.beizer;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by pqq on 2017/12/18.
 */

public class BeizerViewPager extends ViewPager {
    private boolean isCanTouch = true;

    public BeizerViewPager(Context context) {
        super(context);
    }

    public BeizerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanTouch) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanTouch) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    public void setCanTouch(boolean isCanTouch) {
        this.isCanTouch = isCanTouch;
    }
}
