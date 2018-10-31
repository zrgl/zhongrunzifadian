package com.xiaobao.zhongrun.module.presenter;

import android.content.Intent;

import com.google.gson.Gson;
import com.xiaobao.zhongrun.module.model.MainActivityModel;
import com.xiaobao.zhongrun.module.model.UserActivityModel;
import com.xiaobao.zhongrun.module.model.bean.UploadHeadBean;
import com.xiaobao.zhongrun.module.model.bean.UserDetailBean;
import com.xiaobao.zhongrun.module.model.bean.UserSetItemBean;
import com.xiaobao.zhongrun.other.base.BasePresenter;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class UserActivityPresenter implements BasePresenter<BaseView.UserActivityView> {

    private BaseView.UserActivityView mUserActivityView;

    @Override
    public void attachView(BaseView.UserActivityView view) {
        mUserActivityView = view;
    }

    @Override
    public void detachView() {
        mUserActivityView = null;
    }

    public boolean checkDataChange(List<UserSetItemBean> mAdapterDatas, UserDetailBean mUserDetailBean) {
        if (mUserDetailBean == null) {
            return false;
        }
        if (!mAdapterDatas.get(0).getItemValue().equals(mUserDetailBean.getData().getNickname())) {
            return true;
        } else if (!mAdapterDatas.get(1).getItemValue().equals(mUserDetailBean.getData().getMobile())) {
            return true;
        } else if (!mAdapterDatas.get(2).getItemValue().equals(mUserDetailBean.getData().getEmail())) {
            return true;
        } else if (!mAdapterDatas.get(3).getItemValue().equals(mUserDetailBean.getData().getRealname())) {
            return true;
        } else if (!mAdapterDatas.get(4).getItemValue().equals(mUserDetailBean.getData().getSex())) {
            return true;
        } else if (!mAdapterDatas.get(5).getItemValue().equals(mUserDetailBean.getData().getAge())) {
            return true;
        }
        return false;
    }

    public List<UserSetItemBean> initItem() {
        List<UserSetItemBean> mAdapterDatas = new ArrayList<>();
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_NAME, ""));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_PHONE, ""));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_EMAIL, StringConstant.CONSTANT_EMPTY));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_TRUENAME, StringConstant.CONSTANT_EMPTY));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_GENDER, ""));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_AGE, ""));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_COUNTRIES, ""));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_PROVINCE, ""));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.USER_ITEM_CITY, ""));
        return mAdapterDatas;
    }

    public void updateUser() {
        MainActivityModel.getInstance().getUserDetail(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mUserActivityView.showToast(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    UserDetailBean bean = new Gson().fromJson(response, UserDetailBean.class);
                    if (bean.getCode().equals(StringConstant.HTTP_CODE_SUCCESS)) {
                        mUserActivityView.updateUser(bean);
                        SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_UID, bean.getData().getId() + "");
                    } else {
                        mUserActivityView.showToast(bean.getMsg());
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void uploadHead(File file) {
        mUserActivityView.showProgress("正在上传头像");
        UserActivityModel.getInstance().uploadHead(file, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mUserActivityView.hideProgress();
            }

            @Override
            public void onResponse(String response, int id) {
                mUserActivityView.hideProgress();
                try {
                    UploadHeadBean bean = new Gson().fromJson(response, UploadHeadBean.class);
                    if (bean.getCode().equals(StringConstant.HTTP_CODE_SUCCESS)) {
                        mUserActivityView.updateHeadResult(bean.getUrl());
                    } else {
                        mUserActivityView.showToast(bean.getMsg());
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void dataChange(Intent intent) {
        String fieldName = "", updata = intent.getStringExtra(intent.getAction());
        if (intent.getAction().equals(StringConstant.USER_ITEM_NAME)) {
            fieldName = "nickname";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_PHONE)) {
            fieldName = "mobile";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_EMAIL)) {
            fieldName = "email";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_TRUENAME)) {
            fieldName = "realname";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_GENDER)) {
            if ("女".equals(updata)) {
                updata = "1";
            } else if ("男".equals(updata)) {
                updata = "2";
            } else if ("保密".equals(updata)) {
                updata = "3";
            }
            fieldName = "sex";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_AGE)) {
            fieldName = "age";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_COUNTRIES)) {
            fieldName = "country";
        } else if (intent.getAction().equals(StringConstant.USER_ITEM_CITY)) {
            fieldName = "city";
        }else if (intent.getAction().equals(StringConstant.USER_ITEM_PROVINCE)) {
            fieldName = "province";
        }
        UserActivityModel.getInstance().dataChange(fieldName, updata, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mUserActivityView.showToast(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                updateUser();
            }
        });
    }
}
