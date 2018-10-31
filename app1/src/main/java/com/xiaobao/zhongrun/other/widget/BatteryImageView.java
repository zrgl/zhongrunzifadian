package com.xiaobao.zhongrun.other.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.xiaobao.zhongrun.R;

/**
 * 电池的image，图片资源写死的
 */
@SuppressLint("AppCompatCustomView")
public class BatteryImageView extends ImageView {

    private int batteryCount;
    private boolean isBattery;
    private ValueAnimator mValueAnimator;
    private static final String TAG = "BatteryImageView";

    public BatteryImageView(Context context) {
        super(context);
    }

    public BatteryImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        setImageResource(R.drawable.image_widget_battery_0);
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(0, 100);
            mValueAnimator.setDuration(1000);
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (isBattery) {
                        if (batteryCount > 100) {
                            batteryCount = 0;
                        }
                        if (batteryCount == 0) {
                            setImageResource(R.drawable.image_widget_battery_0);
                        } else if (batteryCount == 20) {
                            setImageResource(R.drawable.image_widget_battery_20);
                        } else if (batteryCount == 40) {
                            setImageResource(R.drawable.image_widget_battery_40);
                        } else if (batteryCount == 60) {
                            setImageResource(R.drawable.image_widget_battery_60);
                        } else if (batteryCount == 80) {
                            setImageResource(R.drawable.image_widget_battery_80);
                        } else if (batteryCount == 100) {
                            setImageResource(R.drawable.image_widget_battery_100);
                        }
                        batteryCount++;
                    }
                    postInvalidate();
                }
            });
        }
    }

    public void setBattery(int battery) {
        if (battery < 5) {
            setImageResource(R.drawable.image_widget_battery_0_red);
        } else if (battery >= 75) {
            setImageResource(R.drawable.image_widget_battery_100);
        } else if (battery >= 50) {
            setImageResource(R.drawable.image_widget_battery_80);
        } else if (battery >= 30) {
            setImageResource(R.drawable.image_widget_battery_60);
        } else if (battery >= 10) {
            setImageResource(R.drawable.image_widget_battery_40);
        } else if (battery >= 5) {
            setImageResource(R.drawable.image_widget_battery_20);
        }
    }

    public void startBattery() {
        isBattery = true;
        if (mValueAnimator != null) {
            if (!mValueAnimator.isRunning()) {
                batteryCount = 0;
                mValueAnimator.start();
            }
        }
    }

    public void stopBattery() {
        isBattery = false;
        if (mValueAnimator != null) {
            if (mValueAnimator.isRunning()) {
                mValueAnimator.end();
            }
        }
    }

}
