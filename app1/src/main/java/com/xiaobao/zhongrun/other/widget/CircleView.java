package com.xiaobao.zhongrun.other.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

    private int number = 6, select = 0;
    private int mWidth, mHeight;
    private int mColor = Color.rgb(207,207,207), mSelectColor = Color.WHITE;
    private Paint mPaint;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNumber(int number) {
        this.number = number;
        this.invalidate();
    }

    public int getNumber() {
        return number;
    }

    public void setSelect(int select) {
        this.select = select;
        if (select >= number) {
            this.select = number - 1;
        }
        invalidate();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(20f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        for (int i = 0; i < number; i++) {
            mPaint.setColor(mColor);
            float x = mWidth / number * i;
            float y = mHeight / 2;
            x += mWidth / number / 2;
            if (i == select) {
                mPaint.setColor(mSelectColor);
            }
            canvas.drawCircle(x, y, mHeight / 2, mPaint);
        }
    }
}
