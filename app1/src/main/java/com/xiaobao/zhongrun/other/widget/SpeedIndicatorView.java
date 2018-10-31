package com.xiaobao.zhongrun.other.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.xiaobao.zhongrun.R;

import java.text.DecimalFormat;

public class SpeedIndicatorView extends View {

    private Context mContext;
    private float mSpeed, MAX = 60;
    private boolean isStart;
    private int mWidth, mHeight;
    private Paint mTextPaint, mCirclePaint, mScalePaint;
    private String unit = "km/h";

    private float mNormalScale, mTenScale, mInside, mOuter;

    public SpeedIndicatorView(Context context) {
        super(context);
    }

    public SpeedIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
        invalidate();
    }

    public void setMAX(float MAX) {
        this.MAX = MAX;
        this.invalidate();
    }

    public void setUnit(int unit) {
        this.unit = unit == 1 ? "mi/h" : "km/h";
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mNormalScale = (float) (mHeight / 2 * 0.26);
        mTenScale = (float) (mHeight / 2 * 0.24);
        mInside = (float) (mHeight / 2 * 0.1);
        mOuter = (float) (mHeight / 2 * 0.3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpeedIndicator(canvas);
        drawSpeedText(canvas);
        drawScale(canvas);
    }

    public void start() {
        isStart = true;
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, MAX * 2);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value <= MAX) {
                    mSpeed = value;
                } else {
                    mSpeed = MAX * 2 - value;
                }
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void close() {
        isStart = false;
        invalidate();
    }


    //码表圆盘
    private void drawSpeedIndicator(Canvas canvas) {
        //填充内圆
        LinearGradient linearGradient;

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        mCirclePaint = new Paint();
        BlurMaskFilter blu = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
        mCirclePaint.setMaskFilter(blu);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mContext.getResources().getColor(R.color.theme));
        mCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mHeight / 2 - 60, mCirclePaint);

        linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{getResources().getColor(R.color.gradient_start), Color.BLACK}, null, LinearGradient.TileMode.CLAMP);
        mCirclePaint.setShader(linearGradient);
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);

        canvas.save();
        canvas.rotate((float) (270 / 60.0 * mSpeed + 60));

        //外圆
        canvas.drawCircle(0, 0, mHeight / 2 - 5, mCirclePaint);
        //内圆
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{getResources().getColor(R.color.gradient_start), Color.WHITE}, null, LinearGradient.TileMode.CLAMP);
        mCirclePaint.setShader(linearGradient);
        canvas.drawCircle(0, 0, mHeight / 2 - 60, mCirclePaint);
        //填充
        mCirclePaint.setStrokeWidth(50);
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{getResources().getColor(R.color.gradient_start), getResources().getColor(R.color.gradient_end)}, null, LinearGradient.TileMode.CLAMP);
        mCirclePaint.setShader(linearGradient);
        canvas.drawCircle(0, 0, mHeight / 2 - 35, mCirclePaint);
        canvas.restore();

        canvas.restore();
    }

    //刻度
    private void drawScale(Canvas canvas) {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(0xffb5b5b5);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);

        mScalePaint = new Paint();
        mScalePaint.setColor(isStart ? Color.WHITE : 0xffdddddd);
        mScalePaint.setAntiAlias(true);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeWidth(isStart ? 20 : 10);

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.rotate(45);
        for (float i = 0; i <= MAX; i += 1) {
            mCirclePaint.setColor(0xffb5b5b5);
            if (i == (int) mSpeed) {
                canvas.drawLine(0, mHeight / 2 - 60, 0, mHeight / 2, mScalePaint);
                mCirclePaint.setColor(Color.WHITE);
            }
            if (i % 10 == 0) {
                canvas.drawLine(0, mHeight / 2 - 120, 0, mHeight / 2 - 80, mCirclePaint);
            } else {
                canvas.drawLine(0, mHeight / 2 - 120, 0, mHeight / 2 - 90, mCirclePaint);
            }
//            canvas.rotate(4.5f);
            canvas.rotate((float) (270.0 / MAX));
        }
        canvas.restore();
    }

    //码表文字显示
    private void drawSpeedText(Canvas canvas) {
        //速度
        mTextPaint = new Paint();
        mTextPaint.setColor(isStart ? Color.WHITE : 0xffdddddd);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen._38px_sp));
        float tw = mTextPaint.measureText(formatFloat(mSpeed));
        canvas.drawText("" + formatFloat(mSpeed), (mWidth - tw) / 2, mHeight / 2, mTextPaint);

        //单位
        mTextPaint.setColor(0xffdddddd);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen._18px_sp));
        tw = mTextPaint.measureText(unit);
        canvas.drawText("" + unit, (mWidth - tw) / 2, mHeight - mHeight / 3, mTextPaint);
    }

    private String formatFloat(float value) {
        String newValue = "0";
        if (value < 10) {
            newValue += new DecimalFormat("#0.00").format(value);
            return newValue;
        }
        return new DecimalFormat("#0.00").format(value);
    }
}
