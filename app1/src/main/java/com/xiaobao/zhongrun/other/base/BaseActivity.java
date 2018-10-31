package com.xiaobao.zhongrun.other.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.other.util.StatusbarUtils;

import butterknife.ButterKnife;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FrameLayout mFrameLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTitleTextView, mRightTextView;
    private LinearLayout mLeftLinearLayout, mCenterLinearLayout, mRightLinearLayout;

    private LoadingDailog.Builder mBuilder;
    private LoadingDailog mLoadingDailog;

    private boolean setStatusbar = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置常亮
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//取消常亮
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);

        mToolbar = findViewById(R.id.activity_base_toolBar);
        mFrameLayout = findViewById(R.id.activity_base_view_content);
        mSwipeRefreshLayout = findViewById(R.id.activity_base_swipe_refresh_layout);
        mTitleTextView = findViewById(R.id.activity_base_tv_title);
        mRightTextView = findViewById(R.id.activity_base_tv_right);
        mLeftLinearLayout = findViewById(R.id.activity_base_ll_left);
        mCenterLinearLayout = findViewById(R.id.activity_base_ll_center);
        mRightLinearLayout = findViewById(R.id.activity_base_ll_right);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        LayoutInflater.from(this).inflate(getContentView(), mFrameLayout);
        ButterKnife.bind(this);
        init(savedInstanceState);

        if (setStatusbar) {
            StatusbarUtils.setColor(this, getResources().getColor(R.color.theme), 0);
        }
        mBuilder = new LoadingDailog.Builder(this);
        mLoadingDailog = mBuilder.create();
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener());
    }

    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract SwipeRefreshLayout.OnRefreshListener onRefreshListener();

    protected void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    protected void setSwipeRefreshLayoutEnabled(boolean enabled) {
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title))
            mTitleTextView.setText(title);
    }

    protected void setRight(String right) {
        if (!TextUtils.isEmpty(right))
            mRightTextView.setText(right);
    }

    protected void showToolBar(boolean isShow) {
        mToolbar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**该方法必须在init里面调用 ，设置状态栏*/
    protected void isSetStatusbar(boolean set) {
        setStatusbar = set;
    }

    protected void showLeft(boolean isShow) {
        mLeftLinearLayout.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    protected void showCenter(boolean isShow) {
        mCenterLinearLayout.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    protected void showRight(boolean isShow) {
        mRightLinearLayout.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setLeftClickListener(View.OnClickListener onClickListener) {
        mLeftLinearLayout.setOnClickListener(onClickListener);
    }

    protected void setCenterClickListener(View.OnClickListener onClickListener) {
        mCenterLinearLayout.setOnClickListener(onClickListener);
    }

    protected void setRightClickListener(View.OnClickListener onClickListener) {
        mRightLinearLayout.setOnClickListener(onClickListener);
    }

    protected void setLeftClickFinish() {
        mLeftLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    protected void showProgress() {
        mBuilder.setMessage("加载中");
        mBuilder.setCancelable(true);
        mLoadingDailog = mBuilder.create();
        mLoadingDailog.show();
    }

    protected void showProgress(String message) {
        mBuilder.setMessage(message);
        mBuilder.setCancelable(true);
        mLoadingDailog = mBuilder.create();
        mLoadingDailog.show();
    }

    protected void hideProgress() {
        if (mLoadingDailog == null || mBuilder == null) {
            return;
        }
        if (mLoadingDailog.isShowing()) {
            mLoadingDailog.dismiss();
        }
    }
}
