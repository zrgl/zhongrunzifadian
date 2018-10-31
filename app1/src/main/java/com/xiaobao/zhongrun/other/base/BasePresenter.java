package com.xiaobao.zhongrun.other.base;

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
