package com.xiaobao.zhongrun.module.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.xiaobao.zhongrun.module.model.MainActivityModel;
import com.xiaobao.zhongrun.module.model.bean.DeviceParamBean;
import com.xiaobao.zhongrun.module.model.bean.UserDetailBean;
import com.xiaobao.zhongrun.other.base.BasePresenter;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.util.BluetoothCommandUtils;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

@SuppressLint({"NewApi", "MissingPermission"})
public class MainActivityPresenter implements BasePresenter<BaseView.MainActivityView> {

    private BaseView.MainActivityView mMainActivityView;

    @Override
    public void attachView(BaseView.MainActivityView view) {
        mMainActivityView = view;
    }

    @Override
    public void detachView() {
        mMainActivityView = null;
    }

    public void init(Context context) {
        initPer(context);
        updateUser();
    }

    public void updateUser() {
        MainActivityModel.getInstance().getUserDetail(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mMainActivityView.showToast(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    UserDetailBean bean = new Gson().fromJson(response, UserDetailBean.class);
                    if (bean.getCode().equals(StringConstant.HTTP_CODE_SUCCESS)) {
                        mMainActivityView.updateUser(bean); /** 数据源
                          下面存储
                         */
                        SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_UID, bean.getData().getId() + "");
                    } else {
                        mMainActivityView.showToast(bean.getMsg());
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void addData() {
        BluetoothCommandUtils.getInstance().setUpdateParam(true);
        MainActivityModel.getInstance().getAddData(BluetoothCommandUtils.getInstance().getErr_code() + "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mMainActivityView.showToast(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    DeviceParamBean paramBean = new Gson().fromJson(response, DeviceParamBean.class);
                    BluetoothCommandUtils.getInstance().setXs_km(Byte.parseByte(paramBean.getData().getSpeed()));
                    BluetoothCommandUtils.getInstance().setLj(Byte.parseByte(paramBean.getData().getDiameter()));
                    BluetoothCommandUtils.getInstance().setNdw(Byte.parseByte(paramBean.getData().getGears()));
                    BluetoothCommandUtils.getInstance().setOfftime(Byte.parseByte(paramBean.getData().getDown_time()));
                    BluetoothCommandUtils.getInstance().setBacklight_level(Byte.parseByte(paramBean.getData().getBacklight()));
                    BluetoothCommandUtils.getInstance().setUnit_setting_active(Byte.parseByte(paramBean.getData().getUnit()));
                    BluetoothCommandUtils.getInstance().setCharge_status(Byte.parseByte(paramBean.getData().getVoltage()));
                    mMainActivityView.setMaxParams(Integer.parseInt(paramBean.getData().getGears()),Integer.parseInt(paramBean.getData().getSpeed()));
                } catch (Exception e) {
                }
            }
        });
    }

    /*权限判断*/
    private void initPer(Context context) {
        String[] per = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_WIFI_STATE
                , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
                , Manifest.permission.CHANGE_WIFI_STATE
                , Manifest.permission.INTERNET
        };
        PermissionsUtil.requestPermission(context, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permissions) {
            }

            @Override
            public void permissionDenied(@NonNull String[] permissions) {
                String str = "";
                for (String s : permissions) {
                    if (s.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        str += "蓝牙,";
                    } else if (s.equals(Manifest.permission.CAMERA)) {
                        str += "相册,";
                    } else if (s.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        str += "地图写入,";
                    } else if (s.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        str += "地图读取,";
                    } else if (s.equals(Manifest.permission.READ_PHONE_STATE)) {
                        str += "定位,";
                    }
                }
                if (!TextUtils.isEmpty(str)) {
                    mMainActivityView.showToast(str + "将无法正常使用");
                }
            }
        }, per, false, null);
    }

}
