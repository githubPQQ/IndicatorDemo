package com.example.pqq.indicatordemo.view.beizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by pqq on 2017/12/16.
 */

public class BeizerCicleView extends View {
    public BeizerCicleView(Context context) {
        super(context);
        init(context, null);
    }

    public BeizerCicleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BeizerCicleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private Paint mPaint;

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#59c3e2"));
    }

    private int width;
    private int height;
    private int maxRadius;
    private float padding;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h - 20;
        maxRadius = Math.min(width, height) / 2;

        radius = 80;
        circleBottomCanMoveDis = height / 2 - radius;
        circleBottomCanCrimpDis = 50;
        animTime = 2000;

        circlePosX = width / 2;
        circlePosY = height / 2;

        initPath();
    }

    private Path path;
    private float beizerFactor = 0.551915024494f;

    private HorizontalLine topLine;
    private HorizontalLine bottomLine;
    private VerticalLine leftLine;
    private VerticalLine rightLine;
    private float radius = 100;
    private float circlePosY;
    private float circlePosX;
    private float circleBottomCanMoveDis;
    private float circleBottomCanCrimpDis;
    private long animTime;

    private void initPath() {

        leftLine = new VerticalLine(-radius, 0, radius);
        topLine = new HorizontalLine(0, -radius, radius);
        rightLine = new VerticalLine(radius, 0, radius);
        bottomLine = new HorizontalLine(0, radius, radius);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(circlePosX, circlePosY);

        if (mState == DOWN) {
            if (circlePosY > height / 2 + circleBottomCanMoveDis) {
                float crimpDis = circlePosY - (height / 2 + circleBottomCanMoveDis);
                bottomLine.setY(radius - crimpDis);
                topLine.setY(-radius - crimpDis/4);
            } else if (circlePosY < height / 2 - circleBottomCanMoveDis) {
                float crimpDis = (height / 2 - circleBottomCanMoveDis) - circlePosY;
                topLine.setY(-radius + crimpDis);
                bottomLine.setY(radius + crimpDis/4);

            }
        } else if (mState == UP) {
            if (circlePosY > height / 2 + circleBottomCanMoveDis) {
                float crimpDis = circlePosY - (height / 2 + circleBottomCanMoveDis);
                bottomLine.setY(radius - crimpDis);
                topLine.setY(-radius - crimpDis/4);
            } else if (circlePosY < height / 2 - circleBottomCanMoveDis) {
                float crimpDis = (height / 2 - circleBottomCanMoveDis) - circlePosY;
                topLine.setY(-radius + crimpDis);
                bottomLine.setY(radius + crimpDis/4);
            }
        }
        path = new Path();
        path.moveTo(topLine.middle.x, topLine.middle.y);
        path.cubicTo(topLine.right.x, topLine.right.y, rightLine.top.x, rightLine.top.y,
                rightLine.middle.x, rightLine.middle.y);
        path.cubicTo(rightLine.bottom.x, rightLine.bottom.y,
                bottomLine.right.x, bottomLine.right.y, bottomLine.middle.x, bottomLine.middle.y);
        path.cubicTo(bottomLine.left.x, bottomLine.left.y, leftLine.bottom.x, leftLine.bottom.y,
                leftLine.middle.x, leftLine.middle.y);
        path.cubicTo(leftLine.top.x, leftLine.top.y, topLine.left.x, topLine.left.y,
                topLine.middle.x, topLine.middle.y);
        canvas.drawPath(path, mPaint);

        handlePosY();

        postInvalidateDelayed(30);
    }

    private int mState = DOWN;
    private static final int DOWN = 1;
    private static final int UP = 2;

    private void handlePosY() {
        if (mState == DOWN) {
            circlePosY += 10;
            if (circlePosY > height / 2 + circleBottomCanMoveDis + circleBottomCanCrimpDis) {
                circlePosY = height / 2 + circleBottomCanMoveDis + circleBottomCanCrimpDis;
                mState = UP;
            }
        } else if (mState == UP) {
            circlePosY -= 10;
            if (circlePosY < height / 2 - circleBottomCanMoveDis - circleBottomCanCrimpDis) {
                circlePosY = height / 2 - circleBottomCanMoveDis - circleBottomCanCrimpDis;
                mState = DOWN;
            }
        }
    }
}
