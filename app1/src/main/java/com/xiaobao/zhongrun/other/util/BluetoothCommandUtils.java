package com.xiaobao.zhongrun.other.util;

import android.annotation.SuppressLint;
import android.util.Log;
 /**
  *      电车参数
  * */
public class BluetoothCommandUtils {

    /*结构体1*/
    private int err_code = -1;//错误码
    private int battery;//电量
    private int dw;//档位
    private float speed;//速度
    private int tp;//踏频
    private boolean charge;//充电状态
    private float range;//剩余里程
    private boolean push;//助推状态
    private boolean light;//前灯
    private boolean shutdown;

    /*结构体2*/
    private int trip;//单次里程
    private int odo;//总里程
    private int qxtime;//骑行时间
    private byte trip_32, trip_24, trip_16, trip_8;//单次里程以10米为单位
    private byte odo_32, odo_24, odo_16, odo_8;//总里程以10米为单位
    private byte qxtime_32, qxtime_24, qxtime_16, qxtime_8;//骑行时间以10s为单位

    /*APP TO BLUE 结构体*/
    private byte xs_km;//限速
    private byte lj = -1;//轮径
    private byte ndw = -1;//总档位
    private byte offtime = -1;//关机时间
    private byte backlight_level = -1;//背光等级
    private byte unit_setting_active = -1;//单位设置
    private byte check_set;//校验
    private byte charge_status1 = 0;//充电状态
    private byte charge_status2 = 0;
    private byte dw_A;//档位
    private byte dw_B;
    private byte bit_switch1;//控制开关;
    private byte bit_switch2;

    private String deviceName, deviceAddress;
    private int dw_count = 3, push_count = 3, light_count = 3, shutdown_count = 3;
    private boolean struct1Status, struct2Status, updateParam;
    private static final String TAG = "BluetoothCommandUtils";


    private static volatile BluetoothCommandUtils mBluetoothCommandUtils = null;

    private BluetoothCommandUtils() {
    }

    public synchronized static BluetoothCommandUtils getInstance() {
        if (mBluetoothCommandUtils == null) {
            mBluetoothCommandUtils = new BluetoothCommandUtils();
        }
        return mBluetoothCommandUtils;
    }

    long last = 0;

    /* 更新结构体1*/
    public void updateDeviceStruct1(byte[] bytes) {
        struct1Status = true;
        if (bytes == null) {
            return;
        }
        if (bytes.length != 20) {
            return;
        }
        String bit_switch = getBit(bytes[18]);
        String strDw = getBit(bytes[3]);
        err_code = bytes[0] & 0xff;
        battery = bytes[1] & 0xff;
        speed = (bytes[5] & 0xff >> 8) | bytes[6] & 0xff;
        tp = (bytes[8] & 0xff >> 8) | bytes[9] & 0xff;
        charge = (bytes[13] & 0xff) > 0;
        range = (bytes[15] & 0xff >> 8) | bytes[16] & 0xff;

        Log.e(TAG, "updateDeviceStruct1: 进入更新结构体，间隔时间：" + (System.currentTimeMillis() - last));
        if (strDw.substring(0, 1).equals("1")) {
            Log.e(TAG, "updateDeviceStruct1: 档位发生变化");
            if (dw_count > 3) {
                //bit7:1表示bit0--bit3挡位有变化
                Log.e(TAG, "updateDeviceStruct1: 更新档位");
                dw = Integer.valueOf(strDw.substring(4), 2);//bit0--bit3表示档位
            }
        }

        if (bit_switch.substring(6, 7).equals("1")) {
            push = bit_switch.substring(7).equals("1");
        }

        if (bit_switch.substring(4, 5).equals("1")) {
            light = bit_switch.substring(5, 6).equals("1");
        }

        if (bit_switch.substring(2, 3).equals("1")) {
            shutdown = bit_switch.substring(3, 4).equals("1");
        }
        last = System.currentTimeMillis();
    }

    /* 更新结构体2*/
    @SuppressLint("LongLogTag")
    public void updateDeviceStruct(byte[] bytes) {
        struct2Status = true;
        if (bytes == null) {
            return;
        }
        if (bytes.length != 28) {
            return;
        }
        //单次里程以10米为单位
        trip_32 = bytes[0];
        trip_24 = bytes[1];
        trip_16 = bytes[2];
        trip_8 = bytes[3];
        //总里程以10米为单位
        odo_32 = bytes[5];
        odo_24 = bytes[6];
        odo_16 = bytes[7];
        odo_8 = bytes[8];
        //骑行时间以10s为单位
        qxtime_32 = bytes[10];
        qxtime_24 = bytes[11];
        qxtime_16 = bytes[12];
        qxtime_8 = bytes[13];

        trip = (trip_32 << trip_24 | trip_24 << trip_16 | trip_16 << trip_8 | trip_8 << 0) & 0xff;
        odo = (odo_32 << odo_24 | odo_24 << odo_16 | odo_16 << odo_8 | odo_8 << 0) & 0xff;
        qxtime = (qxtime_32 << qxtime_24 | qxtime_24 << qxtime_16 | qxtime_16 << qxtime_8 | qxtime_8 << 0) & 0xff;

        Log.e("BluetoothCommandUtils Byte", "单次里程=" + trip + ",总里程=" + odo + ",骑行时间=" + qxtime);
    }

    @SuppressLint("LongLogTag")
    public void updateDeviceStruct2(byte[] bytes) {
        struct2Status = true;
        if (bytes == null) {
            return;
        }
        if (bytes.length != 28) {
            return;
        }
        //单次里程以10米为单位
        trip_32 = bytes[0];
        trip_24 = bytes[1];
        trip_16 = bytes[2];
        trip_8 = bytes[3];
        //总里程以10米为单位
        odo_32 = bytes[5];
        odo_24 = bytes[6];
        odo_16 = bytes[7];
        odo_8 = bytes[8];
        //骑行时间以10s为单位
        qxtime_32 = bytes[10];
        qxtime_24 = bytes[11];
        qxtime_16 = bytes[12];
        qxtime_8 = bytes[13];
        //合成trip
        trip = trip_32;
        trip = trip << 8;

        trip |= trip_24;
        trip = trip << 8;

        trip |= trip_16;
        trip = trip << 8;

        trip |= trip_8;
        trip = trip&0xff;
        //合成odo
        odo = odo_32;
        odo = odo << 8;

        odo |= odo_24;
        odo = odo << 8;

        odo |= odo_16;
        odo = odo << 8;

        odo |= odo_8;
        odo = odo&0xff;
        //合成qxtime
        qxtime = qxtime_32;
        qxtime = qxtime << 8;

        qxtime |= qxtime_24;
        qxtime = qxtime << 8;

        qxtime |= qxtime_16;
        qxtime = qxtime << 8;

        qxtime |= qxtime_8;
        qxtime = qxtime&0xff;
        Log.e("BluetoothCommandUtils Int", "int 单次里程=" + (trip & 0xff) + ",总里程=" + (odo & 0xff) + ",骑行时间=" + (qxtime & 0xff));
    }

    public void initCommand() {
        err_code = -1;
        dw_count = 3;
        push_count = 3;
        light_count = 3;
        shutdown_count = 3;
        struct1Status = false;
        struct2Status = false;
        shutdown = false;
        updateParam = false;
    }

    public byte[] getCommand() {

        if (!struct1Status || !struct2Status) {
            return null;
        }

        byte bit0, bit1 = 0, bit2, bit3 = 0, bit4, bit5 = 0;

        //计算档位
        if (dw_count <= 3) {
            dw_A = (byte) ((8 << 4) | dw);
            dw_count++;
        } else {
            dw_A = (byte) dw;
        }
        dw_B = dw_A;

        bit0 = (byte) (push ? 1 : 0);
        bit2 = (byte) ((light ? 1 : 0) << 2);
        bit4 = (byte) ((shutdown ? 1 : 0) << 4);

        if (push_count < 3) {
            bit1 = 1 << 1;
            push_count++;
        }

        if (light_count < 3) {
            bit3 = 1 << 3;
            light_count++;
        }

        if (shutdown_count < 3) {
            bit5 = 1 << 5;
            shutdown_count++;
        }

        bit_switch1 = (byte) (bit5 | bit4 | bit3 | bit2 | bit1 | bit0);
        bit_switch2 = bit_switch1;

        byte[] bytes = new byte[13];
        bytes[0] = xs_km;
        bytes[1] = lj;
        bytes[2] = ndw;
        bytes[3] = offtime;
        bytes[4] = backlight_level;
        bytes[5] = (byte) ((1 << 7) | unit_setting_active);
        int check = (bytes[0] & 0xff) + (bytes[1] & 0xff) + (bytes[2] & 0xff) + (bytes[3] & 0xff) + (bytes[4] & 0xff) + (bytes[5] < 0 ? byteToInt(bytes[5]) : bytes[5] & 0xff);
//        bytes[6] = (byte) (bytes[0] & 0xff + bytes[1] & 0xff + bytes[2] & 0xff + bytes[3] & 0xff + bytes[4] & 0xff + bytes[5] & 0xff);
        bytes[6] = (byte) (check | 0);
        bytes[7] = charge_status1;
        bytes[8] = charge_status2;
        bytes[9] = dw_A;
        bytes[10] = dw_B;
        bytes[11] = bit_switch1;
        bytes[12] = bit_switch2;

        return bytes;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    private String getBit(byte b) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(b >> 7 & 0x1);
        buffer.append(b >> 6 & 0x1);
        buffer.append(b >> 5 & 0x1);
        buffer.append(b >> 4 & 0x1);
        buffer.append(b >> 3 & 0x1);
        buffer.append(b >> 2 & 0x1);
        buffer.append(b >> 1 & 0x1);
        buffer.append(b >> 0 & 0x1);
        return buffer.toString();
    }

    private int byteToInt(byte b) {
        return Integer.valueOf(Integer.toBinaryString(b).toString().substring(24), 2);
    }

    public int getShutdown_count() {
        return shutdown_count;
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

    public void setUpdateParam(boolean updateParam) {
        this.updateParam = updateParam;
    }

    public boolean isUpdateParam() {
        return updateParam;
    }

    /*设置档位*/
    public boolean setDw(int dw) {
        if (dw < 0 || dw > (ndw & 0xff)) {
            return false;
        }
        this.dw = dw;
        this.dw_count = 0;
        return true;
    }

    /*设置push状态*/
    public boolean setPush(boolean push) {
        if (push == this.push) {
            return false;
        }
        this.push = push;
        this.push_count = 0;
        return true;
    }

    /*设置前灯开关*/
    public boolean setLight(boolean light) {
        if (this.light == light) {
            return false;
        }
        this.light = light;
        this.light_count = 0;
        return true;
    }

    //设置充电状态
    public boolean setCharge_status(byte charge) {
        if (charge < 0 || charge > 100) {
            return false;
        }
        charge_status1 = charge;
        charge_status2 = charge;
        return true;
    }

    /*设置关机*/
    public boolean setShutdown(boolean shutdown) {
        if (this.shutdown == shutdown) {
            return false;
        }
        this.shutdown = shutdown;
        this.shutdown_count = 0;
        return true;
    }

    /*设置限速10-70*/
    public boolean setXs_km(byte xs_km) {
        if (xs_km < 10 || xs_km > 70) {
            return false;
        }
        this.xs_km = xs_km;
        return true;
    }

    /*设置轮径6-34*/
    public boolean setLj(byte lj) {
        if (lj < 6 || lj > 34) {
            return false;
        }
        this.lj = lj;
        return true;
    }

    /*设置总档位2-9*/
    public boolean setNdw(byte ndw) {
        if (ndw < 2 || ndw > 9) {
            return false;
        }
        this.ndw = ndw;
        return true;
    }

    /*设置仪表盘关机时间0-60*/
    public boolean setOfftime(byte offtime) {
        if (offtime < 0 || offtime > 60) {
            return false;
        }
        this.offtime = offtime;
        return true;
    }

    /*设置背光等级0-9*/
    public boolean setBacklight_level(byte backlight_level) {
        if (backlight_level < 0 || backlight_level > 9) {
            return false;
        }
        this.backlight_level = backlight_level;
        return true;
    }

    public byte getUnit_setting_active() {
        return unit_setting_active;
    }

    public void setUnit_setting_active(byte unit_setting_active) {
        this.unit_setting_active = unit_setting_active;
    }

    /*获取错误码*/
    public int getErr_code() {
        return err_code;
    }

    /*获取电池电量*/
    public int getBattery() {
        return battery;
    }

    /*获取当前档位*/
    public int getDw() {
        return dw;
    }

    /*获取速度*/
    public float getSpeed() {
        return speed;
    }

    /*获取踏频*/
    public int getTp() {
        return tp;
    }

    /*是否在充电*/
    public boolean getCharge() {
        return charge;
    }

    /*获取剩余里程*/
    public float getRange() {
        return range;
    }

    /*push是否打开*/
    public boolean isPush() {
        return push;
    }

    /*前灯是否打开*/
    public boolean isLight() {
        return light;
    }

    /*单次里程 KM*/
    public float getTrip() {
        return trip;
    }

    /*总里程 KM*/
    public float getOdo() {
        return odo;
    }

    /*骑行时间 min*/
    public float getQxtime() {
        return qxtime;
    }

    /*仪表盘状态*/
    public boolean isShutdown() {
        return shutdown;
    }
}
