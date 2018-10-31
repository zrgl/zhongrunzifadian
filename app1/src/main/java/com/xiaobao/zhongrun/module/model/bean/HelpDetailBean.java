package com.xiaobao.zhongrun.module.model.bean;

public class HelpDetailBean {

    /**
     * Code : 000000
     * Msg : 请求OK
     * Time : 2018-08-10 11:49:06
     * ApiUrl : http://xmap18070034.php.hzxmnet.com/api/Article/articledetail
     * Data : {"content":"<p>测试自动禁用<br/><\/p>"}
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
         * content : <p>测试自动禁用<br/></p>
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
