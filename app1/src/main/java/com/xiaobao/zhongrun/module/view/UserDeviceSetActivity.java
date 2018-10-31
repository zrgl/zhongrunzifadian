package com.xiaobao.zhongrun.module.view;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class UserDeviceSetActivity extends BaseActivity {

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
        setTitle(StringConstant.USER_DEVICE_SET_TITLE);

        mUserDeviceAdapter = new UserDeviceAdapter(this);
        mUserDeviceAdapter.setOnItemClickClickListener(onItemClickClickListener);
        mUserDeviceAdapter.setShowNext(true);

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
            UserListBean.DataBean.ListsBean bean = mUserDeviceAdapter.getmAdapterDatas().get(position);
            Intent intent = new Intent(UserDeviceSetActivity.this, UserDeviceSetDetailActivity.class);
            intent.putExtra(StringConstant.DEVICE_DETAIL_NAME, bean.getDevice_name());
            intent.putExtra(StringConstant.DEVICE_DETAIL_IMAGE, bean.getPic_id());
            intent.putExtra(StringConstant.DEVICE_DETAIL_TYPE, bean.getVersion());
            intent.putExtra(StringConstant.DEVICE_DETAIL_INTRO, bean.getCard());
            intent.putExtra(StringConstant.TYPE_CONSANT_CARD, bean.getCard());
            intent.putExtra(StringConstant.TYPE_CONSANT_XS_KM, bean.getSpeed());
            intent.putExtra(StringConstant.TYPE_CONSANT_LJ, bean.getDiameter());
            intent.putExtra(StringConstant.TYPE_CONSANT_NDW, bean.getGears());
            intent.putExtra(StringConstant.TYPE_CONSANT_OFFTIME, bean.getDown_time());
            intent.putExtra(StringConstant.TYPE_CONSANT_BACKLIGHT_LEVEL, bean.getBacklight());
            intent.putExtra(StringConstant.TYPE_CONSANT_UNIT_SETTING_ACTIVE, bean.getUnit());
            intent.putExtra(StringConstant.TYPE_CONSANT_CHARGE, bean.getVoltage());
            startActivity(intent);
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

    private void updateDevice() {
        Map<String, String> params = new HashMap<>();
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
                    UserListBean listBean = new Gson().fromJson(response, UserListBean.class);
                    mUserDeviceAdapter.setAdapterDatas(listBean.getData().getLists());
                } catch (Exception e) {
                }
            }
        });
    }
}
