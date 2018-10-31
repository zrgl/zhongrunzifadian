package com.xiaobao.zhongrun.module.model.bean;

import java.util.List;

public class VideoBean {
    /**
     * Code : 000000
     * Msg : 请求OK
     * Time : 2018-08-30 14:44:30
     * ApiUrl : http://xmap18070034.php.hzxmnet.com/api/Video/listdata
     * Data : {"total":1,"page":1,"limit":10,"remainder":0,"lists":[{"id":1,"title":"视频教学","url":"http://v.kd.qq.com/1116_6X000000000000000000000000eabNMR.f630.mp4"}]}
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
         * total : 1
         * page : 1
         * limit : 10
         * remainder : 0
         * lists : [{"id":1,"title":"视频教学","url":"http://v.kd.qq.com/1116_6X000000000000000000000000eabNMR.f630.mp4"}]
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
             * id : 1
             * title : 视频教学
             * url : http://v.kd.qq.com/1116_6X000000000000000000000000eabNMR.f630.mp4
             */

            private int id;
            private String title;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
