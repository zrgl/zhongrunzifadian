package com.xiaobao.zhongrun.module.model.bean;

import java.util.List;

public class CityBean {

    private List<CitylistBean> citylist;

    public List<CitylistBean> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CitylistBean> citylist) {
        this.citylist = citylist;
    }

    public static class CitylistBean {
        /**
         * p : 北京
         * c : [{"n":"北京市","a":[{"s":"东城区"},{"s":"西城区"},{"s":"崇文区"},{"s":"宣武区"},{"s":"朝阳区"},{"s":"丰台区"},{"s":"石景山区"},{"s":"海淀区"},{"s":"门头沟区"},{"s":"房山区"},{"s":"通州区"},{"s":"顺义区"},{"s":"昌平区"},{"s":"大兴区"},{"s":"平谷区"},{"s":"怀柔区"},{"s":"密云县"},{"s":"延庆县"}]}]
         */

        private String p;
        private List<CBean> c;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public List<CBean> getC() {
            return c;
        }

        public void setC(List<CBean> c) {
            this.c = c;
        }

        public static class CBean {
            /**
             * n : 北京市
             * a : [{"s":"东城区"},{"s":"西城区"},{"s":"崇文区"},{"s":"宣武区"},{"s":"朝阳区"},{"s":"丰台区"},{"s":"石景山区"},{"s":"海淀区"},{"s":"门头沟区"},{"s":"房山区"},{"s":"通州区"},{"s":"顺义区"},{"s":"昌平区"},{"s":"大兴区"},{"s":"平谷区"},{"s":"怀柔区"},{"s":"密云县"},{"s":"延庆县"}]
             */

            private String n;
            private List<ABean> a;

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }

            public List<ABean> getA() {
                return a;
            }

            public void setA(List<ABean> a) {
                this.a = a;
            }

            public static class ABean {
                /**
                 * s : 东城区
                 */

                private String s;

                public String getS() {
                    return s;
                }

                public void setS(String s) {
                    this.s = s;
                }
            }
        }
    }
}
