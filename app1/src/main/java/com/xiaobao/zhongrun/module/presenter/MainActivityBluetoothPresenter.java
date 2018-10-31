package com.xiaobao.zhongrun.module.presenter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.xiaobao.zhongrun.other.base.BasePresenter;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.service.BluetoothLeService;
import com.xiaobao.zhongrun.other.util.BluetoothCommandUtils;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint({"NewApi", "MissingPermission"})
public class MainActivityBluetoothPresenter implements BasePresenter<BaseView.MainActivityView> {

    private BaseView.MainActivityView mMainActivityView;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器
    private BluetoothLeService mBluetoothLeService;
    private IntentFilter mIntentFilter;
    private DecimalFormat mDecimalFormat;
    private ExecutorService mExecutorService;
    private String mAClass;
    private int mAClassCount = -1;
    private final int TYPE_DISCONNECTED = 0;

    private static final String TAG = "MainActivityBluetoothPr";


    @Override
    public void attachView(BaseView.MainActivityView view) {
        mMainActivityView = view;
        init();
    }

    @Override
    public void detachView() {
        mMainActivityView = null;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_DISCONNECTED:
                    disconnected(); //(gq)调用硬件信息
                    break;
            }
        }
    };

    public IntentFilter getIntentFilter() {
        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter();
        }
        mIntentFilter.addAction(StringConstant.BROADCAST_BLUETOOTH_DISCOVERED);
        mIntentFilter.addAction(StringConstant.BROADCAST_BLUETOOTH_CONNECTED);
        mIntentFilter.addAction(StringConstant.BROADCAST_BLUETOOTH_DISCONNECTED);
        mIntentFilter.addAction(StringConstant.BROADCAST_BLUETOOTH_DATACHANGE);
        return mIntentFilter;
    }

    private void init() {
        mDecimalFormat = new DecimalFormat("#00.00");
        mExecutorService = Executors.newSingleThreadExecutor();
    }


    //开始搜索蓝牙、自动配对
    public void startScanBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        /*若蓝牙实例为空,表示该设备不支持蓝牙*/
        if (mBluetoothAdapter == null) {
            mMainActivityView.showToast(StringConstant.BLUETOOTH_NOTDEVICE);
            return;
        }

        /*检查蓝牙是否打开*/
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.enable()) {
                mMainActivityView.showToast(StringConstant.BLUETOOTH_START);
                return;
            }
        }
        stopScanBluetooth();
        onDestroy();
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void onDestroy() {
        if (mBluetoothLeService != null) {
            mMainActivityView.deviceParams(0, false, 0, 0, 0);
            mMainActivityView.updateConnect(StringConstant.BLUETOOTH_NOTCONNECT);
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
        }
    }

    public void stopScanBluetooth() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    //蓝牙搜索结果回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if (bluetoothDevice.getName() == null) {
                return;
            }
            if (bluetoothDevice.getAddress() == null) {
                mMainActivityView.showToast("蓝牙地址为空");
                return;
            }
            if (mBluetoothLeService == null) {
                mMainActivityView.showToast("蓝牙打开失败");
                return;
            }
            //搜索到硬件的蓝牙
            if (bluetoothDevice.getName().equals(StringConstant.CONSTANT_DEVICE_NAME)) {
                BluetoothCommandUtils.getInstance().setDeviceName(bluetoothDevice.getName());
                BluetoothCommandUtils.getInstance().setDeviceAddress(bluetoothDevice.getAddress());
                mMainActivityView.updateConnect(StringConstant.BLUETOOTH_CONNECTING);
                stopScanBluetooth();//停止搜索
                mBluetoothLeService.connect(bluetoothDevice.getAddress());//开始连接该蓝牙
            }
        }
    };

    //连接蓝牙服务
    public ServiceConnection getServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) iBinder).getService();
            if (!mBluetoothLeService.initalize()) {
                mMainActivityView.showToast(StringConstant.BLUETOOTH_INIT_FAILED_);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public BroadcastReceiver getBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StringConstant.BROADCAST_BLUETOOTH_DISCOVERED)) {//服务已连接正在连接
                mMainActivityView.updateConnect(StringConstant.BLUETOOTH_CONNECT); //已连接
            } else if (intent.getAction().equals(StringConstant.BROADCAST_BLUETOOTH_CONNECTED)) {//设备已连接
                mMainActivityView.updateConnect(StringConstant.BLUETOOTH_CONNECT);
                BluetoothCommandUtils.getInstance().initCommand();
                mAClassCount = -1;
                startCountTask();//启动计时器
            } else if (intent.getAction().equals(StringConstant.BROADCAST_BLUETOOTH_DISCONNECTED)) {//设备断开连接
                disconnected();
            } else if (intent.getAction().equals(StringConstant.BROADCAST_BLUETOOTH_DATACHANGE)) {//数据更新
                mAClassCount = 0;
                Log.e(TAG, "onReceive: " + mAClassCount);
                final byte[] bytes = intent.getByteArrayExtra(StringConstant.BROADCAST_BLUETOOTH_DATACHANGE);  //蓝牙传过来的
                String s = bytes.toString();
                 Log.e("qqqq",s);


                if (bytes.length == 20) {
                    BluetoothCommandUtils.getInstance().updateDeviceStruct1(bytes);
                } else if (bytes.length == 28) {
                    BluetoothCommandUtils.getInstance().updateDeviceStruct2(bytes);
                }
                updateUI();  //gq 更新UI

                byte[] command = BluetoothCommandUtils.getInstance().getCommand();
                if (command != null) {
                    if (!BluetoothCommandUtils.getInstance().isUpdateParam()) {
                        mMainActivityView.updateParams();
                    }
                    try {
                        mBluetoothLeService.WriteValue(command);
                    } catch (Exception e) {
                    }
                }
            }

        }
    };
          /**  （gq）battery :电池    charing:装电  speed:速度  gears：齿轮  unit 单位
           *   deviceDistance是硬件传过来的消息  （gq）
           * */
    private void disconnected() {
        mMainActivityView.deviceParams(0, false, 0, 0, 0);
        mMainActivityView.updateConnect(StringConstant.BLUETOOTH_NOTCONNECT);
        mMainActivityView.deviceDistance(mDecimalFormat.format(0), mDecimalFormat.format(0), mDecimalFormat.format(0));
        startScanBluetooth();//断开重新搜索
    }
          /**
           *  (gq)更新UI的数据
          * */
    private void updateUI() {
        int errCode = BluetoothCommandUtils.getInstance().getErr_code();
        int unit = BluetoothCommandUtils.getInstance().getUnit_setting_active();
        int battery = BluetoothCommandUtils.getInstance().getBattery();
        int gears = BluetoothCommandUtils.getInstance().getDw();
        float speed = (float) (BluetoothCommandUtils.getInstance().getSpeed() / 10.0);
        boolean charge = BluetoothCommandUtils.getInstance().getCharge();
        boolean light = BluetoothCommandUtils.getInstance().isLight();
        boolean push = BluetoothCommandUtils.getInstance().isPush();
        float trip = BluetoothCommandUtils.getInstance().getTrip(); //单次
        float odo = BluetoothCommandUtils.getInstance().getOdo();   //总里程
        float qxtime = (BluetoothCommandUtils.getInstance().getQxtime()); //时间
        mMainActivityView.deviceParams(battery, charge, speed, gears, unit);
        mMainActivityView.deviceStatus(light, push, charge);
        mMainActivityView.deviceDistance(mDecimalFormat.format(trip / 100.0), mDecimalFormat.format(odo / 100.0), formatTime((int) qxtime));

        }


    private String formatTime(int time) {
        String m = "0", s = "";
        if (time / 60 < 10) {
            m += (time / 60) + "";
        } else {
            m = (time / 60) + "";
        }
        if (time % 60 < 10) {
            s = "0"+(time % 60);
        } else {
            s = (time % 60) + "";
        }
        return m + ":" + s;
    }

    private void startCountTask() {
        CountTask countTask = new CountTask();
        mAClass = countTask.toString();
        mExecutorService.execute(countTask);
    }


    /*用来监控是否有心跳*/
    class CountTask implements Runnable {

        @Override
        public void run() {
            while (mAClass.equals(this.toString())) {
                try {
                    if (mAClassCount > 20) {
                        mHandler.sendEmptyMessage(TYPE_DISCONNECTED);
                        break;
                    }
                    Log.e(TAG, "run: " + mAClassCount);
                    if (mAClassCount == -1) {
                        Thread.sleep(10000);
                    }
                    Thread.sleep(100);
                    mAClassCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
