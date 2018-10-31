package com.xiaobao.zhongrun.module.model.bean;

import java.util.List;

public class UserListBean {


    /**
     * Code : 000000
     * Msg : 请求OK
     * Time : 2018-08-28 09:23:37
     * ApiUrl : http://xmap18070034.php.hzxmnet.com/api/Device/userlistdata
     * Data : {"total":0,"page":0,"limit":0,"remainder":0,"lists":[{"id":34,"card":"00:15:87:00:45:75","manufacturer":"","version":"BT05","size":"","error_code":"48","electricity":"","mileage":"","voltage":"0","calorie":"","speed":"60","diameter":"14","gears":"5","down_time":"","backlight":"6","pic_id":"http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-27/b417ac0ed6fcb6b7fde8d50faf5ebbbf.jpg","content":"这是个介绍","device_name":"中润发电一号电车","unit":"0"},{"id":34,"card":"00:15:87:00:45:75","manufacturer":"","version":"BT05","size":"","error_code":"48","electricity":"","mileage":"","voltage":"0","calorie":"","speed":"60","diameter":"14","gears":"5","down_time":"","backlight":"6","pic_id":"http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-27/b417ac0ed6fcb6b7fde8d50faf5ebbbf.jpg","content":"这是个介绍","device_name":"中润发电一号电车","unit":"0"},{"id":4,"card":"a123456","manufacturer":"","version":"","size":"","error_code":"","electricity":"","mileage":"","voltage":"","calorie":"","speed":"","diameter":"","gears":"","down_time":"","backlight":"","pic_id":"","content":"","device_name":"","unit":""},{"id":6,"card":"A888888","manufacturer":"测试厂家","version":"","size":"","error_code":"400","electricity":"","mileage":"","voltage":"","calorie":"","speed":"","diameter":"","gears":"","down_time":"","backlight":"","pic_id":"","content":"","device_name":"","unit":""},{"id":59,"card":"20:15:87:00:45:75","manufacturer":"","version":"","size":"","error_code":"3","electricity":"","mileage":"","voltage":"","calorie":"","speed":"","diameter":"","gears":"","down_time":"","backlight":"","pic_id":"","content":"","device_name":"","unit":""}]}
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
         * total : 0
         * page : 0
         * limit : 0
         * remainder : 0
         * lists : [{"id":34,"card":"00:15:87:00:45:75","manufacturer":"","version":"BT05","size":"","error_code":"48","electricity":"","mileage":"","voltage":"0","calorie":"","speed":"60","diameter":"14","gears":"5","down_time":"","backlight":"6","pic_id":"http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-27/b417ac0ed6fcb6b7fde8d50faf5ebbbf.jpg","content":"这是个介绍","device_name":"中润发电一号电车","unit":"0"},{"id":34,"card":"00:15:87:00:45:75","manufacturer":"","version":"BT05","size":"","error_code":"48","electricity":"","mileage":"","voltage":"0","calorie":"","speed":"60","diameter":"14","gears":"5","down_time":"","backlight":"6","pic_id":"http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-27/b417ac0ed6fcb6b7fde8d50faf5ebbbf.jpg","content":"这是个介绍","device_name":"中润发电一号电车","unit":"0"},{"id":4,"card":"a123456","manufacturer":"","version":"","size":"","error_code":"","electricity":"","mileage":"","voltage":"","calorie":"","speed":"","diameter":"","gears":"","down_time":"","backlight":"","pic_id":"","content":"","device_name":"","unit":""},{"id":6,"card":"A888888","manufacturer":"测试厂家","version":"","size":"","error_code":"400","electricity":"","mileage":"","voltage":"","calorie":"","speed":"","diameter":"","gears":"","down_time":"","backlight":"","pic_id":"","content":"","device_name":"","unit":""},{"id":59,"card":"20:15:87:00:45:75","manufacturer":"","version":"","size":"","error_code":"3","electricity":"","mileage":"","voltage":"","calorie":"","speed":"","diameter":"","gears":"","down_time":"","backlight":"","pic_id":"","content":"","device_name":"","unit":""}]
         */

        private int total;
        private int page;
        private int limit;
        private int remainder;
        private List<ListsBean> lists;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getRemainder() {
            return remainder;
        }

        public void setRemainder(int remainder) {
            this.remainder = remainder;
        }

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            /**
             * id : 34
             * card : 00:15:87:00:45:75
             * manufacturer :
             * version : BT05
             * size :
             * error_code : 48
             * electricity :
             * mileage :
             * voltage : 0
             * calorie :
             * speed : 60
             * diameter : 14
             * gears : 5
             * down_time :
             * backlight : 6
             * pic_id : http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-27/b417ac0ed6fcb6b7fde8d50faf5ebbbf.jpg
             * content : 这是个介绍
             * device_name : 中润发电一号电车
             * unit : 0
             */

            private int id;
            private String card;
            private String manufacturer;
            private String version;
            private String size;
            private String error_code;
            private String electricity;
            private String mileage;
            private String voltage;
            private String calorie;
            private String speed;
            private String diameter;
            private String gears;
            private String down_time;
            private String backlight;
            private String pic_id;
            private String content;
            private String device_name;
            private String unit;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCard() {
                return card;
            }

            public void setCard(String card) {
                this.card = card;
            }

            public String getManufacturer() {
                return manufacturer;
            }

            public void setManufacturer(String manufacturer) {
                this.manufacturer = manufacturer;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getError_code() {
                return error_code;
            }

            public void setError_code(String error_code) {
                this.error_code = error_code;
            }

            public String getElectricity() {
                return electricity;
            }

            public void setElectricity(String electricity) {
                this.electricity = electricity;
            }

            public String getMileage() {
                return mileage;
            }

            public void setMileage(String mileage) {
                this.mileage = mileage;
            }

            public String getVoltage() {
                return voltage;
            }

            public void setVoltage(String voltage) {
                this.voltage = voltage;
            }

            public String getCalorie() {
                return calorie;
            }

            public void setCalorie(String calorie) {
                this.calorie = calorie;
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

            public String getPic_id() {
                return pic_id;
            }

            public void setPic_id(String pic_id) {
                this.pic_id = pic_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getDevice_name() {
                return device_name;
            }

            public void setDevice_name(String device_name) {
                this.device_name = device_name;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }
    }
}
