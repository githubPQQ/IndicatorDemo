package com.example.pqq.indicatordemo.view.beizer;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pqq on 2017/12/17.
 */

public class BeizerView extends View {

    private Paint circlePaint;
    private Paint beiZerPaint;
    private Paint touchPaint;
    private int width;
    private int height;
    private Path path;
    private HorizontalLine topLine;
    private HorizontalLine bottomLine;
    private VerticalLine leftLine;
    private VerticalLine rightLine;
    private float radius;

    private float disL = 0.5f;   //离开圆的阈值
    private float disM = 0.8f;  //最大值的阈值
    private float disA = 0.9f;  //到达下个圆框的阈值

    private int circleCount = 0;

    private Matrix matrix_bounceL;
    private Xfermode clearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public BeizerView(Context context) {
        super(context);
        init(context, null);
    }

    public BeizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BeizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.parseColor("#59c3e2"));

        beiZerPaint = new Paint();
        beiZerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        beiZerPaint.setColor(Color.parseColor("#59c3e2"));

        clearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        touchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchPaint.setStyle(Paint.Style.FILL);
        touchPaint.setColor(Color.WHITE);
        touchPaint.setXfermode(clearXfermode);

        matrix_bounceL = new Matrix();
        matrix_bounceL.preScale(-1, 1);

        radius = 40;
        curBeizerPos = 0;
        nextBeizePos = 0;
        circleCount = 4;
        initPath();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        setCount(circleCount);

    }

    private void initPath() {
        leftLine = new VerticalLine(-radius, 0, radius);
        topLine = new HorizontalLine(0, -radius, radius);
        rightLine = new VerticalLine(radius, 0, radius);
        bottomLine = new HorizontalLine(0, radius, radius);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, height / 2);

        //绘制背景滑块的圆环
        for (int i = 0; i < circleCount; i++) {
            canvas.drawCircle(circlePos.get(i), 0, radius, circlePaint);
        }

        if (isTouchAniming) {
            int count = canvas.saveLayer(rectF_touch, touchPaint, Canvas.ALL_SAVE_FLAG);
            canvas.drawCircle(circlePos.get(nextBeizePos), 0, touchAnimValue, touchPaint);
            touchPaint.setXfermode(clearXfermode);
            canvas.drawCircle(circlePos.get(nextBeizePos), 0, 0.5f * radius, touchPaint);
            if (touchAnimValue >= radius) {             //如果白色的圆半径>=mRadis ，就开始绘制透明的圆
                canvas.drawCircle(circlePos.get(nextBeizePos), 0,
                        (touchAnimValue - radius) / 0.5f * 1.4f, touchPaint);
            }
            touchPaint.setXfermode(null);
            canvas.restoreToCount(count);
        }
        //绘制贝塞尔圆圈

        float tranX = 0;
        if (nextBeizePos != curBeizerPos) {
            if (animateValue == 0 || animateValue == 1) {
                tranX = circlePos.get(curBeizerPos);
            } else if (animateValue > 0 && animateValue <= disL) {//当前圆圈拉出一部分
                tranX = circlePos.get(curBeizerPos);

            } else if (animateValue > disL && animateValue <= disM) {//整体拉出，拉到两个圈中间
                if (curBeizerPos > nextBeizePos) {
                    tranX = circlePos.get(curBeizerPos) -
                            width / (circleCount + 1) / 2 * range0Until1(disL, disM);
                } else {
                    tranX = circlePos.get(curBeizerPos) +
                            width / (circleCount + 1) / 2 * range0Until1(disL, disM);
                }
            } else if (animateValue > disM && animateValue <= disA) {//从两个圈中间，拉到第二个圈中间
                if (curBeizerPos > nextBeizePos) {
                    tranX = circlePos.get(curBeizerPos) - width / (circleCount + 1) / 2 -
                            width / (circleCount + 1) / 2 * range0Until1(disM, disA);
                } else {
                    tranX = circlePos.get(curBeizerPos) + width / (circleCount + 1) / 2 +
                            width / (circleCount + 1) / 2 * range0Until1(disM, disA);
                }
            } else if (animateValue > disA && animateValue < 1) {//在第二个圈中回弹，填满第二个圈
                tranX = circlePos.get(nextBeizePos);

            }
        } else {
            tranX = circlePos.get(curBeizerPos);
        }
        canvas.translate(tranX, 0);
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
        if (nextBeizePos < curBeizerPos) {
            path.transform(matrix_bounceL);
        }
        canvas.drawPath(path, beiZerPaint);
    }

    private float range0Until1(float minValue, float maxValue) {
        return (animateValue - minValue) / (maxValue - minValue);
    }

    private List<Float> circlePos;
    private int curBeizerPos;
    private int nextBeizePos;

    public void setCount(int circleCount) {
        this.circleCount = circleCount;
        circlePos = new ArrayList<>();
        float spacing = width / (circleCount + 1);
        for (int i = 0; i < circleCount; i++) {
            circlePos.add(spacing * (i + 1));
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isAniming)
                    return true;
                float touchX = event.getX();
                float touchY = event.getY();
                for (int i = 0; i < circleCount; i++) {
                    if (touchX < circlePos.get(i) + radius && touchX > circlePos.get(i) - radius
                            && touchY < height / 2 + radius && touchY > height / 2 - radius) {
                        nextBeizePos = i;
                        rectF_touch = new RectF(circlePos.get(nextBeizePos) - 1.5f * radius, -1.5f * radius,
                                circlePos.get(nextBeizePos) + 1.5f * radius, 1.5f * radius);
                        if (nextBeizePos != curBeizerPos) {
                            startTouchAnim();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

    private ValueAnimator touchAnimator;
    private float touchAnimValue;
    private boolean isTouchAniming = false;
    private RectF rectF_touch;

    private void startTouchAnim() {
        if (touchAnimator != null) {
            if (touchAnimator.isRunning()) {
                return;
            }
            touchAnimator.start();
        } else {
            touchAnimator = ValueAnimator.ofFloat(0f, 1.5f * radius)
                    .setDuration(300);
            touchAnimator.setInterpolator(timeInterpolator);
            touchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    touchAnimValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            touchAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isTouchAniming = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isTouchAniming = false;
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(nextBeizePos);
                    }
                    startAnimator();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isTouchAniming = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

    }

    private ValueAnimator animatorStart;
    private TimeInterpolator timeInterpolator = new LinearInterpolator();
    private float animateValue;//[0,1];
    private boolean isAniming = false;

    public void startAnimator() {
        if (animatorStart != null) {
            if (animatorStart.isRunning()) {
                return;
            }
            animatorStart.start();
        } else {
            animatorStart = ValueAnimator.ofFloat(0, 1f).setDuration(500);
//            animatorStart.setRepeatMode(ValueAnimator.RESTART);
//            animatorStart.setRepeatCount(ValueAnimator.INFINITE);
            animatorStart.setInterpolator(timeInterpolator);
            animatorStart.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAniming = true;
                    if (onSelectListener != null) {
                        onSelectListener.onAnim(true);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAniming = false;
                    if (onSelectListener != null) {
                        onSelectListener.onAnim(false);
                    }

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAniming = false;
                    if (onSelectListener != null) {
                        onSelectListener.onAnim(false);
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animatorStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animateValue = (float) animation.getAnimatedValue();
                    Log.e("onAnimationUpdate", animateValue + "");
                    changeBeizerShape(animateValue);
                    invalidate();
                }
            });
            animatorStart.start();
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isAniming) {
            return;
        }
        animateValue = positionOffset;
        if (position + positionOffset > curBeizerPos) {
            nextBeizePos = curBeizerPos + 1;
        } else {
            nextBeizePos = curBeizerPos - 1;
            animateValue = 1 - animateValue;
        }
        if (positionOffset == 0) {
            curBeizerPos = position;
            nextBeizePos = position;
        }
        changeBeizerShape(animateValue);
        invalidate();
    }

    private void changeBeizerShape(float animateValue) {
        if (animateValue > 0 && animateValue <= disL) {
            rightLine.setX(radius + radius * animateValue * 2);
        } else if (animateValue > disL && animateValue <= disM) {
            float deltaX = (float) ((2 * radius - 1.5 * radius) / (disM - disL) * (animateValue - disL));
            rightLine.setX(2 * radius - deltaX);
            leftLine.setX(-radius - deltaX);
            float deltaY = (float) ((radius / 3 * 1) / (disM - disL) * (animateValue - disL));
            topLine.setY(-radius + deltaY);
            bottomLine.setY(radius - deltaY);
        } else if (animateValue > disM && animateValue <= disA) {
            float deltaX = (float) ((1.5 * radius - 1 * radius) / (disA - disM) * (animateValue - disM));
            rightLine.setX((float) (1.5 * radius - deltaX));

            float leftDeltaX = (float) ((1.5 * radius - 1.2 * radius) / (disA - disM) * (animateValue - disM));
            leftLine.setX((float) (-1.5 * radius + leftDeltaX));

            float deltaY = (float) ((radius / 3 * 1) / (disA - disM) * (animateValue - disM));
            topLine.setY(-(radius / 3 * 2) - deltaY);
            bottomLine.setY(radius / 3 * 2 + deltaY);
        } else if (animateValue > disA && animateValue <= 1) {
            rightLine.setX(radius);

            float leftDeltaX = (float) ((1.2 * radius - 1 * radius) / (1 - disA) * (animateValue - disA));
            leftLine.setX((float) (-1.2 * radius + leftDeltaX));

            topLine.setY(-radius);
            bottomLine.setY(radius);

            if (animateValue == 1) {
                curBeizerPos = nextBeizePos;
            }
        }
    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelect(int pos);

        void onAnim(boolean isAnim);
    }

}
