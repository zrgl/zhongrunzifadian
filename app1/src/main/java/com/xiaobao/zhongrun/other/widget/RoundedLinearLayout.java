package com.xiaobao.zhongrun.other.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RoundedLinearLayout extends LinearLayout {

    private RectF mRectF;
    private Paint mPaint;

    public RoundedLinearLayout(Context context) {
        super(context);
    }

    public RoundedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bitmap);
        super.onDraw(mCanvas);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mRectF.set(0,0,getWidth(),getHeight());
        mCanvas.drawRoundRect(mRectF,50,50,mPaint);
    }
}
