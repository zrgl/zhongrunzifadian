package com.xiaobao.zhongrun.other.base;

public class BaseResponse<T> {

    private String Code;
    private String Msg;
    private String Time;
    private String ApiUrl;
    private T Data;

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

    public T getData() {
        return Data;
    }

    public void setData(T Data) {
        this.Data = Data;
    }
}
