package com.xiaobao.zhongrun;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaobao.zhongrun.module.model.bean.UserDetailBean;
import com.xiaobao.zhongrun.module.presenter.MainActivityBluetoothPresenter;
import com.xiaobao.zhongrun.module.presenter.MainActivityPresenter;
import com.xiaobao.zhongrun.module.view.FeedBackActivity;
import com.xiaobao.zhongrun.module.view.HelpActivity;
import com.xiaobao.zhongrun.module.view.ShareActivity;
import com.xiaobao.zhongrun.module.view.UserActivity;
import com.xiaobao.zhongrun.module.view.UserDeviceActivity;
import com.xiaobao.zhongrun.module.view.UserDeviceSetActivity;
import com.xiaobao.zhongrun.module.view.UserFaultActivity;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.service.BluetoothLeService;
import com.xiaobao.zhongrun.other.util.BluetoothCommandUtils;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.xiaobao.zhongrun.other.widget.BatteryImageView;
import com.xiaobao.zhongrun.other.widget.ButtonDialogFragment;
import com.xiaobao.zhongrun.other.widget.CircleView;
import com.xiaobao.zhongrun.other.widget.SpeedIndicatorView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主页的Activity，逻辑代码在P层(MainActivityPresenter)
 */
@SuppressLint("NewApi")  /** 屏蔽一切新api中才能使用的方法报的android lint错误*/
public class MainActivity extends BaseActivity implements BaseView.MainActivityView {  //View 接口实现的方法

    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.activity_main_view_circle)
    CircleView mCircleView;
    @BindView(R.id.activity_main_view_battery)
    BatteryImageView mBatteryView;
    @BindView(R.id.activity_main_view_speed)
    SpeedIndicatorView mSpeedIndicatorView;
    @BindView(R.id.activity_main_iv_share)
    ImageView mShareImageView;
    @BindView(R.id.activity_main_iv_set)
    ImageView mSetImageView;
    @BindView(R.id.activity_main_iv_light)
    ImageView mLightImageView;
    @BindView(R.id.activity_main_iv_push)
    ImageView mPushImageView;
    @BindView(R.id.activity_main_iv_charging)
    ImageView mChargingImageView;
    @BindView(R.id.activity_main_iv_shutdown)
    ImageView mShutdownImageView;
    @BindView(R.id.activity_main_iv_gears_down)
    ImageView mGearsDownImageView;
    @BindView(R.id.activity_main_iv_gears_up)
    ImageView mGearsUpImageView;
    @BindView(R.id.activity_main_tv_gears)
    TextView mGearsTextView;
    @BindView(R.id.activity_main_tv_connect)
    TextView mConnectTextView;
    @BindView(R.id.activity_main_tv_distance)   /** 单次里程*/
    TextView mDistanceTextView;
    @BindView(R.id.activity_main_tv_distance_total)  /**总里程*/
    TextView mTotalTextView;
    @BindView(R.id.activity_main_tv_time)    /**骑行时间*/
    TextView mTimeTextView;

    private View mHeaderView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private CircleImageView mHeadImageView;
    private LinearLayout mHeadLinearLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private MainActivityPresenter mMainActivityPresenter;
    private MainActivityBluetoothPresenter mainActivityBluetoothPresenter;
    private Intent mIntent;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;  //首页
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        showToolBar(false);
        isSetStatusbar(false);
        mBatteryView.init();
        mHeaderView = mNavigationView.getHeaderView(0);

        mNameTextView = mHeaderView.findViewById(R.id.activity_main_header_tv_name);
        mAddressTextView = mHeaderView.findViewById(R.id.activity_main_header_tv_address);
        mHeadImageView = mHeaderView.findViewById(R.id.activity_main_header_iv_head);
        mHeadLinearLayout = mHeaderView.findViewById(R.id.activity_main_header_ll_head);

        mNavigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mHeadLinearLayout.setOnClickListener(onClickListener);
        mSetImageView.setOnClickListener(onClickListener);
        mShareImageView.setOnClickListener(onClickListener);
        mLightImageView.setOnClickListener(onClickListener);
        mPushImageView.setOnClickListener(onClickListener);
        mChargingImageView.setOnClickListener(onClickListener);
        mShutdownImageView.setOnClickListener(onClickListener);
        mGearsDownImageView.setOnClickListener(onClickListener);
        mGearsUpImageView.setOnClickListener(onClickListener);

        mIntent = new Intent(this, BluetoothLeService.class);
        mMainActivityPresenter = new MainActivityPresenter();
        mMainActivityPresenter.attachView(this);
        mMainActivityPresenter.init(this);
        mainActivityBluetoothPresenter = new MainActivityBluetoothPresenter();
        mainActivityBluetoothPresenter.attachView(this);
        mainActivityBluetoothPresenter.startScanBluetooth();

        bindService(mIntent, mainActivityBluetoothPresenter.getServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mainActivityBluetoothPresenter.getBroadcastReceiver, mainActivityBluetoothPresenter.getIntentFilter());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainActivityPresenter.updateUser();
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mConnectTextView.getText().toString().equals(StringConstant.BLUETOOTH_CONNECT)) {
                    mainActivityBluetoothPresenter.startScanBluetooth();
                }
                mMainActivityPresenter.updateUser();
                setRefreshing(false);
            }
        };
    }

    int dw;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_main_header_ll_head://跳转到用户详情
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                    break;
                case R.id.activity_main_iv_share://点击分享
                    startActivity(new Intent(MainActivity.this, ShareActivity.class));
                    overridePendingTransition(R.anim.push_bottom_in, 0);
                    break;
                case R.id.activity_main_iv_set://点击设置
                    mDrawerLayout.openDrawer(mNavigationView);
                    break;
                case R.id.activity_main_iv_light://点击灯光开关
                    BluetoothCommandUtils.getInstance().setLight(!BluetoothCommandUtils.getInstance().isLight());
                    break;
                case R.id.activity_main_iv_push://点击push
                    BluetoothCommandUtils.getInstance().setPush(!BluetoothCommandUtils.getInstance().isPush());
                    break;
                case R.id.activity_main_iv_charging://点击充电

                    break;
                case R.id.activity_main_iv_shutdown://点击关机
                    if (mConnectTextView.getText().toString().equals(StringConstant.BLUETOOTH_CONNECT)) {
                        new ButtonDialogFragment().show("确定要关机吗？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BluetoothCommandUtils.getInstance().setShutdown(!BluetoothCommandUtils.getInstance().isShutdown());
                            }
                        }, null, getSupportFragmentManager());
                    } else {
                        mainActivityBluetoothPresenter.startScanBluetooth();
                    }
                    break;
                case R.id.activity_main_iv_gears_down://点击降档
                    if (mConnectTextView.getText().toString().equals(StringConstant.BLUETOOTH_NOTCONNECT)) {
                        return;
                    }
                    if (BluetoothCommandUtils.getInstance().getDw() == 0) {
                        showToast("档位已经是最小值");
                        return;
                    }
                    showProgress("正在更改档位");  //带进度条的toash；
                    dw = BluetoothCommandUtils.getInstance().getDw() - 1;
                    BluetoothCommandUtils.getInstance().setDw(dw);
                    break;
                case R.id.activity_main_iv_gears_up://点击加档
                    if (mConnectTextView.getText().toString().equals(StringConstant.BLUETOOTH_NOTCONNECT)) {
                        return;
                    }
                    if (BluetoothCommandUtils.getInstance().getDw() == (mCircleView.getNumber()-1)) {
                        showToast("档位已经是最大值");
                        return;
                    }
                    showProgress("正在更改档位");
                    dw = BluetoothCommandUtils.getInstance().getDw() + 1;
                    BluetoothCommandUtils.getInstance().setDw(dw);
                    break;

            }
        }
    };

    //侧边栏菜单点击事件
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.main_device://我的设备
                    startActivity(new Intent(MainActivity.this, UserDeviceActivity.class));
                    break;
                case R.id.main_list://故障列表
                    startActivity(new Intent(MainActivity.this, UserFaultActivity.class));
                    break;
                case R.id.main_set://参数设置
                    startActivity(new Intent(MainActivity.this, UserDeviceSetActivity.class));
                    break;
                case R.id.main_help://帮助中心
                    startActivity(new Intent(MainActivity.this, HelpActivity.class));
                    break;
                case R.id.main_feedback://意见反馈
                    startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                    break;
            }
            return true;
        }
    };
     // (gq) 四个按钮的
    private void setImageViewBackground(boolean p) {
        mLightImageView.setImageDrawable(p ? getDrawable(R.drawable.image_widget_light)
                : getDrawable(R.drawable.image_widget_light_gray));
        mPushImageView.setImageDrawable(p ? getDrawable(R.drawable.image_widget_push)
                : getDrawable(R.drawable.image_widget_push_gray));
        mChargingImageView.setImageDrawable(p ? getDrawable(R.drawable.image_widget_charging)
                : getDrawable(R.drawable.image_widget_charging_gray));
        mShutdownImageView.setImageDrawable(p ? getDrawable(R.drawable.image_widget_shutdown)
                : getDrawable(R.drawable.image_widget_shutdown_gray));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityBluetoothPresenter.onDestroy();
        unbindService(mainActivityBluetoothPresenter.getServiceConnection);
        unregisterReceiver(mainActivityBluetoothPresenter.getBroadcastReceiver);
    }

    @Override
    public void showToast(String message) {
        super.showToast(message);
    }

    @Override
    public void showProgress(String progress) {
        super.showProgress(progress);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void deviceParams(int battery, boolean charging, float speed, int gears, int unit) {
        hideProgress();
        if (charging) {
            mBatteryView.startBattery();
        } else {
            mBatteryView.setBattery(battery);
            mBatteryView.stopBattery();
        }
        mSpeedIndicatorView.setSpeed(speed);
        mSpeedIndicatorView.setUnit(unit);
        mCircleView.setSelect(gears );
        mGearsTextView.setText(gears + "档");
    }

    @Override
    public void deviceStatus(boolean light, boolean push, boolean charging) {
        mLightImageView.setImageDrawable(light ? getDrawable(R.drawable.image_widget_light)
                : getDrawable(R.drawable.image_widget_light_gray));
        mPushImageView.setImageDrawable(push ? getDrawable(R.drawable.image_widget_push)
                : getDrawable(R.drawable.image_widget_push_gray));
        mChargingImageView.setImageDrawable(charging ? getDrawable(R.drawable.image_widget_charging)
                : getDrawable(R.drawable.image_widget_charging_gray));

    }

   /**
    *  （gq）
    *  distance 单次里程
    *  total   总里程
    *  time   骑行时间  int dis = Integer.valueOf(distance).intValue();
    * */
    @Override
    public void deviceDistance(String distance, String total, String time) {
        mDistanceTextView.setText(distance);
        mTotalTextView.setText(total);
        mTimeTextView.setText(time);
    }
    @Override
    public void updateConnect(String connect) {
        hideProgress();
        if (connect.equals(StringConstant.BLUETOOTH_CONNECTING)) {
            mSpeedIndicatorView.start();
            showToast("设备" + connect);
        } else if (connect.equals(StringConstant.BLUETOOTH_NOTCONNECT)) {
            mSpeedIndicatorView.close();
            showToast("设备已断开");
            setImageViewBackground(false);
            mBatteryView.init();
        } else if (connect.equals(StringConstant.BLUETOOTH_CONNECT)) {
            showToast("设备" + connect);
            setImageViewBackground(true);
        }
        mConnectTextView.setText(connect);
    }
     //gqUserDetailBean 上传个人信息
    @Override
    public void updateUser(UserDetailBean userDetailBean) {
        if (userDetailBean.getData().getFace_path() != null && !TextUtils.isEmpty(userDetailBean.getData().getFace_path())) {
            Glide.with(this).load(userDetailBean.getData().getFace_path()).into(mHeadImageView);
        }
        if (userDetailBean.getData().getNickname() != null && !TextUtils.isEmpty(userDetailBean.getData().getNickname())) {
            mNameTextView.setText(userDetailBean.getData().getNickname());
        } else {
            mNameTextView.setText(App.getContext().Mac());
        }
        String country = (String) SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_COUNTRY, null);
        String city = (String) SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_CITY, null);
        if (TextUtils.isEmpty(country)) {
            country = "";
        }
        if (TextUtils.isEmpty(city)) {
            city = "";
        }
        mAddressTextView.setText(country + " " + city);
    }

    @Override
    public void updateParams() {
        mMainActivityPresenter.addData();
    }

    @Override
    public void setMaxParams(int dw, int speed) {
        mCircleView.setNumber(dw+1);
        mSpeedIndicatorView.setMAX(speed);
    }
}

