package com.xiaobao.zhongrun.module.model.bean;

public class UserDetailBean{

    /**
     * Code : 000000
     * Msg : 返回成功
     * Time : 2018-08-30 10:23:30
     * ApiUrl : http://xmap18070034.php.hzxmnet.com/api/User/memberdetail
     * Data : {"id":24,"email":"123456@qq.com","mobile":"13211111111","realname":"","sex":"3","age":"0","country":"中国","city":"天津市","nickname":"小宝","face_path":"http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-29/5fe158d93a38364f2525c6798eadb91b.jpg","province":""}
     */
    //上传个人信息
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
         * id : 24
         * email : 123456@qq.com
         * mobile : 13211111111
         * realname :
         * sex : 3
         * age : 0
         * country : 中国
         * city : 天津市
         * nickname : 小宝
         * face_path : http://xmap18070034.php.hzxmnet.com/uploads/picture/2018-08-29/5fe158d93a38364f2525c6798eadb91b.jpg
         * province :
         */

        private int id;
        private String email;
        private String mobile;
        private String realname;
        private String sex;
        private String age;
        private String country;
        private String city;
        private String nickname;
        private String face_path;
        private String province;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFace_path() {
            return face_path;
        }

        public void setFace_path(String face_path) {
            this.face_path = face_path;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }
}
