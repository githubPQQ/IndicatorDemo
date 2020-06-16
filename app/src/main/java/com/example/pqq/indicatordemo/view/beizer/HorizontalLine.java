package com.example.pqq.indicatordemo.view.beizer;

import android.graphics.PointF;

/**
 * Created by pqq on 2017/12/16.
 */

public class HorizontalLine {
    private float c = 0.551915024494f;

    public PointF left = new PointF();
    public PointF middle = new PointF();
    public PointF right = new PointF();


    public HorizontalLine(float x, float y, float radius) {
        left.x = x - radius * c;
        left.y = y;

        middle.x = x;
        middle.y = y;

        right.x = x + radius * c;
        right.y = y;
    }

    public void setY(float y) {
        left.y = y;
        middle.y = y;
        right.y = y;
    }

}
