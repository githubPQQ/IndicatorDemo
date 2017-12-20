package com.example.pqq.indicatordemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pqq on 2017/12/16.
 */

public class WaveView extends View {
    public WaveView(Context context) {
        super(context);
        init(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private Paint mPaint;

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }


    private int width;
    private int height;
    private int maxRadius;
    private long animTime;
    private int circleCount;
    private Interpolator mInterpolator;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        maxRadius = Math.min(width, height) / 2;
        animTime = 3000;
        circleCount = 10;
        createCircle();
    }

    private List<WaveCircle> circleList;

    private void createCircle() {
        if (circleList == null) {
            circleList = new ArrayList<>();
        }
        circleList.clear();
        WaveCircle circle;

        mInterpolator = new LinearOutSlowInInterpolator();
        for (int i = 0; i < circleCount; i++) {
            circle = new WaveCircle(maxRadius, 1, mInterpolator);
            circle.setFactor(1 - (1.0f / circleCount * i));
            circleList.add(circle);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        WaveCircle circle;

        for (int i = 0; i < circleList.size(); i++) {
            circle = circleList.get(i);
            float factor = circle.getFactor();
            factor = 100.0f / animTime + factor;
            if (factor >= 1) {
                factor = 1.0f / circleCount;
            }
            circle.setFactor(factor);
            mPaint.setAlpha((int) (circle.getAlpha() * 255));
            canvas.drawCircle(width / 2, height / 2, circle.getRadius(), mPaint);
        }
        if (isAnim)
            postInvalidateDelayed(100);
    }

    private boolean isAnim = true;

    private void setIsAnim(boolean isAnim) {
        this.isAnim = isAnim;
        invalidate();
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
        if (circleList != null) {
            for (int i = 0; i < circleList.size(); i++) {
                circleList.get(i).setInterpolator(mInterpolator);
            }
        }
    }
}
