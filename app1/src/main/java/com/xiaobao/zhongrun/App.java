package com.xiaobao.zhongrun;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.util.CrashHandlerUtil;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import java.util.Random;

@SuppressLint({"ServiceCast", "NewApi", "MissingPermission"})
public class App extends Application {

    private static App mApp;
    private IWXAPI mIwxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        init();
    }

    private void init() {
        initWx();
        CrashHandlerUtil.getInstance().init(this);
    }

    public static App getContext() {
        return mApp;
    }

    private void initWx() {
        mIwxapi = WXAPIFactory.createWXAPI(this, StringConstant.CONSTANT_WEIXIN_APPID, true);
        mIwxapi.registerApp(StringConstant.CONSTANT_WEIXIN_APPID);
    }

    /*获取手机的唯一标识*/
    public String Mac() {
        if (SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_MAC, null) == null) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String str = null;
            try {
                str = telephonyManager.getDeviceId();//获取设备ID
                if (TextUtils.isEmpty(str)) {
                    str = telephonyManager.getSimSerialNumber();//获取SIM卡标识
                }
                if (TextUtils.isEmpty(str)) {
                    str = telephonyManager.getImei();//获取IMEI
                }
                if (TextUtils.isEmpty(str)) {
                    str = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);//系统标识
                }
            } catch (Exception e) {
            }
            if (!TextUtils.isEmpty(str)) {
                SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_MAC, str);//随机数
                return str;
            }
            SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_MAC, "R" + new Random().nextInt(Integer.MAX_VALUE));
        }
        return SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_MAC, "").toString();
    }

    public IWXAPI getIwxapi() {
        return mIwxapi;
    }

    /*是否安装微信*/
    public boolean isWeiXinAppInstall() {
        if (mIwxapi == null)
            mIwxapi = WXAPIFactory.createWXAPI(this, StringConstant.CONSTANT_WEIXIN_APPID, true);
        if (mIwxapi.isWXAppInstalled()) {
            return true;
        } else {
            return false;
        }
    }

}
