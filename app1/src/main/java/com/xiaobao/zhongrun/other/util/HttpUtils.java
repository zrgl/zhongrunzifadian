package com.xiaobao.zhongrun.other.util;

import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static HttpUtils mHttpUtils;
    private Map<String, String> defaultParams;
    private static final String TAG = "HttpUtils";

    private HttpUtils() {
        if (defaultParams == null) {
            defaultParams = new HashMap<>();
            defaultParams.put("hash", "1");
            defaultParams.put("apiId", "1");
            defaultParams.put("terminal", "3");
            defaultParams.put("is_inside", "1");
        }
    }

    public static HttpUtils getInstance() {
        if (mHttpUtils == null) {
            mHttpUtils = new HttpUtils();
        }
        return mHttpUtils;
    }

    public Map<String, String> defaultParams() {
        defaultParams.put("time", new Date().getTime() + "");
        Log.e(TAG, "defaultParams: " +defaultParams.size());
        return defaultParams;
    }

}
