package com.xiaobao.zhongrun.module.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.model.bean.UserListBean;
import com.xiaobao.zhongrun.module.view.adapter.UserDeviceAdapter;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.HttpUtils;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class UserFaultActivity extends BaseActivity {

    @BindView(R.id.widget_view_recycler)
    XRecyclerView mXRecyclerView;

    private UserDeviceAdapter mUserDeviceAdapter;

    @Override
    protected int getContentView() {
        return R.layout.widget_view_recycler;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setLeftClickFinish();
        setTitle(StringConstant.USER_FAULT_TITLE);

        mUserDeviceAdapter = new UserDeviceAdapter(this);
        mUserDeviceAdapter.setError(true);
        mUserDeviceAdapter.setOnItemClickClickListener(onItemClickClickListener);

        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setPullRefreshEnabled(false);
        mXRecyclerView.setAdapter(mUserDeviceAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDevice();
    }

    //列表Item点击事件
    private UserDeviceAdapter.onItemClickClickListener onItemClickClickListener = new UserDeviceAdapter.onItemClickClickListener() {
        @Override
        public void itemClick(View view, int position) {

        }
    };

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDevice();
                setRefreshing(false);
            }
        };
    }


    private void updateDevice(){
        Map<String, String> params = new HashMap<>();
        params.put("status", "3");
        params.put("uid", SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_UID, "").toString());
        params.putAll(HttpUtils.getInstance().defaultParams());
        OkHttpUtils.post().url(UrlConstant.USERLISTDATA).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    UserListBean listBean = new Gson().fromJson(response,UserListBean.class);
                    mUserDeviceAdapter.setAdapterDatas(listBean.getData().getLists());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
