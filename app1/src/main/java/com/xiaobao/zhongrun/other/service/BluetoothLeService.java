package com.xiaobao.zhongrun.other.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xiaobao.zhongrun.other.constant.StringConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("NewApi")
public class BluetoothLeService extends Service {

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private IBinder mBinder = new LocalBinder();
    private Intent mDiscoveredIntent, mConnectedIntent, mDisConnectedIntent, mDataIntent;

    private transient long mLastTime;
    private transient ArrayList<byte[]> mDataList;

    public final static UUID UUID_NOTIFY =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final String TAG = "BluetoothLeService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    public boolean initalize() {
        mDataList = new ArrayList<>();
        mDiscoveredIntent = new Intent();
        mDiscoveredIntent.setAction(StringConstant.BROADCAST_BLUETOOTH_DISCOVERED);
        mConnectedIntent = new Intent();
        mConnectedIntent.setAction(StringConstant.BROADCAST_BLUETOOTH_CONNECTED);
        mDisConnectedIntent = new Intent();
        mDisConnectedIntent.setAction(StringConstant.BROADCAST_BLUETOOTH_DISCONNECTED);
        mDataIntent = new Intent();
        mDataIntent.setAction(StringConstant.BROADCAST_BLUETOOTH_DATACHANGE);

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        }
        if (mBluetoothManager == null) {
            return false;
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            return false;
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        mBluetoothGatt = device.connectGatt(this, false, mBluetoothGattCallback);
        return true;
    }


    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void WriteValue(byte[] bs) {
        if (mNotifyCharacteristic == null) {
            return;
        }
        mNotifyCharacteristic.setValue(bs);
        mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public void findService(List<BluetoothGattService> gattServices) {
        for (BluetoothGattService gattService : gattServices) {
            if (gattService.getUuid().toString().equalsIgnoreCase(UUID_SERVICE.toString())) {
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
                for (BluetoothGattCharacteristic gattCharacteristic :
                        gattCharacteristics) {
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(UUID_NOTIFY.toString())) {
                        mNotifyCharacteristic = gattCharacteristic;
                        setCharacteristicNotification(gattCharacteristic, true);
                        // TODO: 2018/8/2  收到信号，可以干活了
                        sendBroadcast(mDiscoveredIntent);
                        return;
                    }
                }
            }
        }
    }

    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // TODO: 2018/8/2 当前是连接状态
                    mBluetoothGatt.discoverServices();
                    sendBroadcast(mConnectedIntent);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // TODO: 2018/8/2 断开连接了
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                    sendBroadcast(mDisConnectedIntent);
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                findService(gatt.getServices());
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            // TODO: 2018/8/2 收到数据 characteristic
            Log.e(TAG, "onCharacteristicChanged: "+(System.currentTimeMillis()-last) );
            last = System.currentTimeMillis();
            addValue(characteristic.getValue());
        }

    };

    long last = 0;

    //保存接收到的值
    private synchronized void addValue(byte[] bytes) {
        mDataList.add(bytes);//添加到集合
        if (System.currentTimeMillis() - mLastTime < 100) {//判断两次调用时间差是否小于100
            //如果集合长度为3代表两次结构体都接受完成，可以发送数据了
            //如果集合长度不为3代表数据不完整，清空集合
            if (mDataList.size() != 3) {
                mDataList.clear();
            } else {
                byte[] b20 = mDataList.get(0);
                byte[] b28 = byteMergerAll(mDataList.get(1), mDataList.get(2));
                String s20f = byteToString(b20, false);
                String s20t = byteToString(b20, true);
                String s28f = byteToString(b28, false);
                String s28t = byteToString(b28, true);
                Log.e(TAG, "结构体1 = "+s20f);
                Log.e(TAG, "结构体1&0xff = "+s20t);
                Log.e(TAG, "结构体2 = "+s28f);
                Log.e(TAG, "结构体2&0xff = "+s28t);
                sendDataBroadcast();
                mDataList.clear();
            }
        }
        mLastTime = System.currentTimeMillis();//记录调用的时间
    }

    private String byteToString(byte[] bytes, boolean isAnd) {
        String str = "";
        for (int i = 0; i < bytes.length; i++) {
            if (i == bytes.length - 1) {
                if (isAnd) {
                    str += (bytes[i] & 0xff) + "。";
                } else {
                    str += bytes[i] + "。";
                }
                continue;
            }
            if (isAnd) {
                str += (bytes[i] & 0xff) + ",";
            } else {
                str += bytes[i] + ",";
            }
        }
        return str;
    }

    private void sendDataBroadcast() {
        mDataIntent.putExtra(StringConstant.BROADCAST_BLUETOOTH_DATACHANGE, mDataList.get(0));
        sendBroadcast(mDataIntent);
        mDataIntent.putExtra(StringConstant.BROADCAST_BLUETOOTH_DATACHANGE, byteMergerAll(mDataList.get(1), mDataList.get(2)));
        sendBroadcast(mDataIntent);
    }


    //合并多个byte数组
    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

}
