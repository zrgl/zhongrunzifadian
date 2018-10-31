package com.xiaobao.zhongrun.other.base;

import com.xiaobao.zhongrun.module.model.bean.UserDetailBean;

public interface BaseView {

    void showToast(String message);

    void showProgress(String progress);

    void hideProgress();


    interface MainActivityView extends BaseView {

        void deviceParams(int battery, boolean charging, float speed, int gears,int unit);

        void deviceStatus(boolean light, boolean push, boolean charging);

        void deviceDistance(String distance, String total, String time);

        void updateConnect(String connect);

        void updateUser(UserDetailBean userDetailBean);

        void updateParams();

        void setMaxParams(int dw,int speed);

    }

    interface ShareActivityView extends BaseView {
        void updateBackground(int color);

        void updateBarAlpha(int alpha);
    }

    interface UserActivityView extends BaseView {
        void updateHeadResult(String url);

        void updateUser(UserDetailBean userDetailBean);
    }

}
