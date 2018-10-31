package com.xiaobao.zhongrun.module.model.bean;

public class DeviceParamBean {

    /**
     * Code : 000000
     * Msg : 查询成功
     * Time : 2018-08-27 19:38:14
     * ApiUrl : http://xmap18070034.php.hzxmnet.com/api/Device/apiadddata
     * Data : {"id":8,"speed":"60","diameter":"14","gears":"5","down_time":"60","backlight":"6","unit":"0","voltage":"0"}
     */

    private String Code;
    private String Msg;
    private String Time;
    private String ApiUrl;
    private DataBean Data;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getApiUrl() {
        return ApiUrl;
    }

    public void setApiUrl(String ApiUrl) {
        this.ApiUrl = ApiUrl;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * id : 8
         * speed : 60
         * diameter : 14
         * gears : 5
         * down_time : 60
         * backlight : 6
         * unit : 0
         * voltage : 0
         */

        private int id;
        private String speed;
        private String diameter;
        private String gears;
        private String down_time;
        private String backlight;
        private String unit;
        private String voltage;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getDiameter() {
            return diameter;
        }

        public void setDiameter(String diameter) {
            this.diameter = diameter;
        }

        public String getGears() {
            return gears;
        }

        public void setGears(String gears) {
            this.gears = gears;
        }

        public String getDown_time() {
            return down_time;
        }

        public void setDown_time(String down_time) {
            this.down_time = down_time;
        }

        public String getBacklight() {
            return backlight;
        }

        public void setBacklight(String backlight) {
            this.backlight = backlight;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getVoltage() {
            return voltage;
        }

        public void setVoltage(String voltage) {
            this.voltage = voltage;
        }
    }
}
