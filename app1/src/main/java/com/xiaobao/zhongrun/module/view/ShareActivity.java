package com.xiaobao.zhongrun.module.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.maps2d.MapView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.presenter.ShareActivityPresenter;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.util.StatusbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
 /**
  *  微信分享（gq）
  * */
@SuppressLint("NewApi")
public class ShareActivity extends AppCompatActivity implements BaseView.ShareActivityView {

    @BindView(R.id.activity_share_btn_cancel)
    Button mCancelButton;
    @BindView(R.id.activity_share_layout)
    LinearLayout mLinearLayout;
    @BindView(R.id.activity_share_ll_wx)
    LinearLayout mWxLayout;
    @BindView(R.id.activity_share_ll_wx_friends)
    LinearLayout mWxFriendsLayout;
    @BindView(R.id.activity_share_map_view)
    MapView mMapView;

    private ShareActivityPresenter mShareActivityPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mWxLayout.setOnClickListener(mOnClickListener);
        mCancelButton.setOnClickListener(mOnClickListener);
        mWxFriendsLayout.setOnClickListener(mOnClickListener);

        mMapView.onCreate(savedInstanceState);

        mShareActivityPresenter = new ShareActivityPresenter();
        mShareActivityPresenter.attachView(this);
        if (mShareActivityPresenter.getMap() == null) {
            mShareActivityPresenter.setMap(mMapView.getMap());
        }
        mShareActivityPresenter.startLocation(this);
        mShareActivityPresenter.startBackgroundUpdate();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_share_btn_cancel:
                    mLinearLayout.setBackgroundColor(Color.TRANSPARENT);
                    finish();
                    break;
                case R.id.activity_share_ll_wx:
                    if (mShareActivityPresenter.getLat() == 0 || mShareActivityPresenter.getLng() == 0) {
                        showToast("定位失败，无法进行位置分享");
                        return;
                    }
//                    mShareActivityPresenter.shareTextToWX("");
                    mShareActivityPresenter.shareWebToWx(ShareActivity.this, SendMessageToWX.Req.WXSceneSession);
                    break;
                case R.id.activity_share_ll_wx_friends:
                    if (mShareActivityPresenter.getLat() == 0 || mShareActivityPresenter.getLng() == 0) {
                        showToast("定位失败，无法进行位置分享");
                        return;
                    }
//                    mShareActivityPresenter.shareTextToWxFriends("");
                    mShareActivityPresenter.shareWebToWx(ShareActivity.this, SendMessageToWX.Req.WXSceneTimeline);
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mShareActivityPresenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void finish() {
        overridePendingTransition(0, R.anim.push_bottom_out);
        super.finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(String progress) {
        showProgress(progress);
    }

    @Override
    public void hideProgress() {
        hideProgress();
    }

    @Override
    public void updateBackground(int color) {
        mLinearLayout.setBackgroundColor(color);
    }

    @Override
    public void updateBarAlpha(int alpha) {
        StatusbarUtils.setColor(this, getResources().getColor(R.color.theme), alpha);
    }
}
