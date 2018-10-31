package com.xiaobao.zhongrun.module.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bumptech.glide.util.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.xiaobao.zhongrun.App;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.other.base.BasePresenter;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("NewApi")
public class ShareActivityPresenter implements BasePresenter<BaseView.ShareActivityView> {

    //AMap是地图对象
    private AMap mMap;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    private AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private BaseView.ShareActivityView mShareActivityView;
    private double lat, lng;

    @Override
    public void attachView(BaseView.ShareActivityView view) {
        mShareActivityView = view;
    }

    @Override
    public void detachView() {
        mShareActivityView = null;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mShareActivityView.updateBackground((Integer) msg.obj);
                    break;
                case 2:
                    mShareActivityView.updateBarAlpha((Integer) msg.obj);
                    break;
            }
        }
    };

    public AMap getMap() {
        return mMap;
    }

    public void setMap(AMap mMap) {
        this.mMap = mMap;
        UiSettings settings = mMap.getUiSettings();//设置显示定位按钮 并且可以点击
        MyLocationStyle style = new MyLocationStyle();
        mMap.setLocationSource(mLocationSource);//设置了定位的监听
        settings.setMyLocationButtonEnabled(true);// 是否显示定位按钮
        mMap.setMyLocationStyle(style);
        mMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
    }

    public void startBackgroundUpdate() {
        new BackgroundUpdate().start();
    }

    public void onDestroy() {
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端
    }

    public void startLocation(Context context) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAmapLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void shareTextToWX(String text) {
        if (!App.getContext().isWeiXinAppInstall()) {
            mShareActivityView.showToast("您没有安装微信,请先安装微信");
            return;
        }
//        shareTextToWx(text, SendMessageToWX.Req.WXSceneSession);
        shareTextToWx(UrlConstant.SHARE + "?lat=" + lat + "&lng=" + lng, SendMessageToWX.Req.WXSceneSession);
    }

    public void shareTextToWxFriends(String text) {
        if (!App.getContext().isWeiXinAppInstall()) {
            mShareActivityView.showToast("您没有安装微信,请先安装微信");
            return;
        }
        shareTextToWx(UrlConstant.SHARE + "?lat=" + lat + "&lng=" + lng, SendMessageToWX.Req.WXSceneTimeline);
//        shareTextToWx(text, SendMessageToWX.Req.WXSceneTimeline);
    }

    public void shareWebToWx(Context context, int type) {
        if (!App.getContext().isWeiXinAppInstall()) {
            mShareActivityView.showToast("您没有安装微信,请先安装微信");
            return;
        }
        shareWebToWx(context,UrlConstant.SHARE + "?lat=" + lat + "&lng=" + lng, type);
    }

    private AMapLocationListener mAmapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    aMapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    df.format(date);//定位时间
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    String country = aMapLocation.getCountry();//国家信息
                    String province = aMapLocation.getProvince();//省信息
                    String city = aMapLocation.getCity();//城市信息
                    aMapLocation.getDistrict();//城区信息
                    aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();

                    String sharedCountry = SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_COUNTRY, "").toString();
                    String sharedCity = SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_COUNTRY, "").toString();
                    if (TextUtils.isEmpty(sharedCountry)) {
                        SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_COUNTRY, country);
                    }
                    if (TextUtils.isEmpty(sharedCity)) {
                        SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_PROVINCE, province);
                        SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_CITY, city);
                    }

                    // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                    if (isFirstLoc) {
                        //设置缩放级别
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                        //将地图移动到定位点
                        mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(aMapLocation);
                        isFirstLoc = false;
                    }

                }
            }
        }
    };

    private LocationSource mLocationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
        }

        @Override
        public void deactivate() {
            mListener = null;
        }
    };

    /**
     * 分享文本类型
     *
     * @param text 文本内容
     * @param type 微信会话或者朋友圈等
     */
    private void shareTextToWx(String text, int type) {
        if (text == null || text.length() == 0) {
            return;
        }

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = type;

        App.getContext().getIwxapi().sendReq(req);
    }

    private void shareWebToWx(Context context,String webpageUrl, int type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (type == SendMessageToWX.Req.WXSceneSession) {
            msg.title = "我的位置分享";
        } else if (type == SendMessageToWX.Req.WXSceneTimeline) {
            msg.title = "中润自发电超长续航电动车";
        }
        msg.description = "骑行发电更环保";
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.image_widget_wx);
        msg.thumbData = bitmapBytes(bitmap,32);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = type;

        App.getContext().getIwxapi().sendReq(req);
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @param
     * @return
     */
    private byte[] bitmapBytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }


    class BackgroundUpdate extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                sleep(200);
                int color = 130;
                for (int i = 1; i <= 100; i++) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = Color.argb((int) (color / 100.0 * i), 0, 0, 0);
                    mHandler.sendMessage(msg);
                    msg = new Message();
                    msg.what = 2;
                    msg.obj = (int) (color / 100.0 * i);
                    mHandler.sendMessage(msg);
                    sleep(3);

                }
            } catch (InterruptedException e) {
            }
        }
    }
}
