package com.xiaobao.zhongrun.module.model;

import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.HttpUtils;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserActivityModel {

    private static volatile UserActivityModel mUserActivityModel = null;

    private UserActivityModel() {
    }

    public synchronized static UserActivityModel getInstance() {
        if (mUserActivityModel == null) {
            mUserActivityModel = new UserActivityModel();
        }
        return mUserActivityModel;
    }

    public void uploadHead(File file, StringCallback stringCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", (String) SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_UID, null));
        params.putAll(HttpUtils.getInstance().defaultParams());
        OkHttpUtils.post()
                .url(UrlConstant.UPDATE_HEAD_IMAGE)
                .params(params).addFile("fileName", file.getName(), file)
                .build().execute(stringCallback);
    }

    public void dataChange(String fieldName, String updata, StringCallback stringCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("id", SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_UID, "").toString());
        params.put("uid", (String) SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_UID, null));
        params.put("fieldName", fieldName);
        params.put("updata", updata);
        params.putAll(HttpUtils.getInstance().defaultParams());
        OkHttpUtils.post().url(UrlConstant.QUICK_EDIT_USER_DETAILDATA)
                .params(params)
                .build().execute(stringCallback);
    }
}
