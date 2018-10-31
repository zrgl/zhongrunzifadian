package com.xiaobao.zhongrun.module.model;

import com.xiaobao.zhongrun.App;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.BluetoothCommandUtils;
import com.xiaobao.zhongrun.other.util.HttpUtils;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;


public class MainActivityModel {

    private static volatile MainActivityModel mMainActivityModel = null;

    private MainActivityModel() {
    }

    public synchronized static MainActivityModel getInstance() {
        if (mMainActivityModel == null) {
            mMainActivityModel = new MainActivityModel();
        }
        return mMainActivityModel;
    }

    public void getUserDetail(StringCallback stringCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("mac", App.getContext().Mac());
        params.putAll(HttpUtils.getInstance().defaultParams());
        OkHttpUtils.post().url(UrlConstant.MAC_REGISTER).params(params)
                .build().execute(stringCallback);
    }

    public void getAddData(String errCode, StringCallback stringCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_UID, "").toString());
        params.put("card", BluetoothCommandUtils.getInstance().getDeviceAddress());
        params.put("manufacturer", "");
        params.put("version", BluetoothCommandUtils.getInstance().getDeviceName());
        params.put("size", "");
        params.put("error_code", errCode);
        params.put("electricity", "");
        params.put("mileage", "");
        params.put("calorie", "");
        params.putAll(HttpUtils.getInstance().defaultParams());
        OkHttpUtils.post().url(UrlConstant.APIADDDATA).params(params)
                .build().execute(stringCallback);
    }
}
