package com.example.pqq.indicatordemo.view.beizer;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by pqq on 2017/12/16.
 */

public class VerticalLine {
    private float c = 0.551915024494f;

    public PointF top = new PointF();
    public PointF middle = new PointF();
    public PointF bottom = new PointF();

    public VerticalLine(float x, float y, float radius) {
        top.x = x;
        top.y = y - radius * c;

        middle.x = x;
        middle.y = y;

        bottom.x = x;
        bottom.y = y + radius * c;
    }

    public void setX(float x) {
        top.x = x;
        middle.x = x;
        bottom.x = x;
    }

}
