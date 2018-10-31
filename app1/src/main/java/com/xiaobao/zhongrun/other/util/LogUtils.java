package com.xiaobao.zhongrun.other.util;

import android.util.Log;

public class LogUtils {

    private static final boolean isDebug = true;
    private static final String TAG = "LogUtils";

    private LogUtils() {
    }

    public static void e(String str) {
        if (isDebug) {
            Log.e(TAG, str);
        }
    }


}
