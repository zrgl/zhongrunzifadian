package com.xiaobao.zhongrun.other.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


public class BatteryView extends View {

    private int mWidth;
    private int mHeight;
    private int mPercent = 0;
    private float mStrokeWidth = 5f;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;

    public BatteryView(Context context) {
        super(context);
        initValueAnimator();
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setmPercent(int mPercent) {
        this.mPercent = mPercent;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        //-----开始处理padding
        //获取padding的值
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        //计算MyCircleView减去padding后的可用宽高
        int canUsedWidth = mWidth - paddingLeft - paddingRight;
        int canUsedHeight = mHeight - paddingTop - paddingBottom;

        //圆心坐标
        mWidth = canUsedWidth - paddingLeft;
        mHeight = canUsedHeight - paddingTop;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mPaint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(0, 0, mWidth / 10 * 9, mHeight);
        canvas.drawRoundRect(rectF, 0, 0, mPaint);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        rectF = new RectF(mWidth / 10 * 9 - (mWidth / 10 / 2), mWidth - mWidth / 10 * 9, mWidth - (mWidth / 10 / 2), mHeight - (mWidth - mWidth / 10 * 9));
        canvas.drawArc(rectF, 270, 180, true, mPaint);

        float right = (float) (mWidth / 10.0 * 9.0 / 100.0 * mPercent) - (mStrokeWidth * 2);
        rectF = new RectF(mStrokeWidth, mStrokeWidth, right, mHeight - mStrokeWidth);
        canvas.drawRect(rectF, mPaint);

    }

    private void initValueAnimator() {
        if (mValueAnimator != null) {
            return;
        }
        mValueAnimator = ValueAnimator.ofInt(0, 100);
        mValueAnimator.setDuration(3000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    public void charging(boolean isCharging) {
        if (mValueAnimator == null) {
            initValueAnimator();
        }
        if (isCharging) {
            if (!mValueAnimator.isRunning()) {
                mValueAnimator.start();
            }
        } else {
            mValueAnimator.end();
            mPercent = 0;
            invalidate();
        }
    }


}
