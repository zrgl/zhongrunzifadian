package com.xiaobao.zhongrun.module.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.xiaobao.zhongrun.MainActivity;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.other.util.StatusbarUtils;

public class WelcomeActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去标题
        StatusbarUtils.setColor(this, Color.rgb(92,201,237), 0);  //状态栏
        setContentView(R.layout.activity_welcome);
        mHandler.sendEmptyMessageDelayed(0, 3 * 1000); //导航页3秒跳转

    }


}
