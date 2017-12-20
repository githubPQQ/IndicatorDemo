package com.example.pqq.indicatordemo.view;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by pqq on 2017/12/16.
 */

public class WaveCircle {
    private float radius;
    private float alpha;
    private float factor;
    private Interpolator mInterpolator;

    public WaveCircle(float radius, float alpha, Interpolator mInterpolator) {
        this.radius = radius;
        this.alpha = alpha;
        this.mInterpolator = mInterpolator;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public float getRadius() {
        return radius * mInterpolator.getInterpolation(factor);
    }

    public float getAlpha() {
        return alpha * (1 - mInterpolator.getInterpolation(factor));
    }

}
