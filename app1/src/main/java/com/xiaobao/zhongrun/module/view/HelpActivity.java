package com.xiaobao.zhongrun.module.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.model.bean.UserSetItemBean;
import com.xiaobao.zhongrun.module.view.adapter.UserActivityAdapter;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.constant.StringConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HelpActivity extends BaseActivity {
    @BindView(R.id.widget_view_recycler)
    XRecyclerView mXRecyclerView;

    private UserActivityAdapter mUserActivityAdapter;
    private List<UserSetItemBean> mAdapterDatas;

    @Override
    protected int getContentView() {
        return R.layout.widget_view_recycler;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setLeftClickFinish();
        setTitle(StringConstant.HELP_TITLE);

        mUserActivityAdapter = new UserActivityAdapter(this);
        mUserActivityAdapter.setItemOnClickListener(mOnItemClickListener);

        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setPullRefreshEnabled(false);
        mXRecyclerView.setAdapter(mUserActivityAdapter);

        initItem();
    }

    private void initItem() {
        mAdapterDatas = new ArrayList<>();
        mAdapterDatas.add(new UserSetItemBean(StringConstant.HELP_ITEM_INTRO, StringConstant.CONSTANT_EMPTY));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.HELP_ITEM_NOVICE, StringConstant.CONSTANT_EMPTY));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.HELP_ITEM_VIDEO, StringConstant.CONSTANT_EMPTY));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.HELP_ITEM_QUESTION, StringConstant.CONSTANT_EMPTY));
        mAdapterDatas.add(new UserSetItemBean(StringConstant.HELP_ITEM_AFTER, StringConstant.CONSTANT_EMPTY));
        mUserActivityAdapter.setAdapterDatas(mAdapterDatas);
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefreshing(false);
            }
        };
    }

    //条目点击事件
    private UserActivityAdapter.onItemClickListener mOnItemClickListener = new UserActivityAdapter.onItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
            if (mAdapterDatas.get(position).getItemName().equals(StringConstant.HELP_ITEM_INTRO)) {
                intent.setAction(StringConstant.HELP_ITEM_INTRO);
                intent.putExtra(StringConstant.HELP_ITEM_INTRO, "1");
            } else if (mAdapterDatas.get(position).getItemName().equals(StringConstant.HELP_ITEM_NOVICE)) {
                intent.setAction(StringConstant.HELP_ITEM_NOVICE);
                intent.putExtra(StringConstant.HELP_ITEM_NOVICE, "2");
            } else if (mAdapterDatas.get(position).getItemName().equals(StringConstant.HELP_ITEM_VIDEO)) {
                intent.setAction(StringConstant.HELP_ITEM_VIDEO);
                intent.putExtra(StringConstant.HELP_ITEM_VIDEO, "3");
            } else if (mAdapterDatas.get(position).getItemName().equals(StringConstant.HELP_ITEM_QUESTION)) {
                intent.setAction(StringConstant.HELP_ITEM_QUESTION);
                intent.putExtra(StringConstant.HELP_ITEM_QUESTION, "4");
            } else if (mAdapterDatas.get(position).getItemName().equals(StringConstant.HELP_ITEM_AFTER)) {
                intent.setAction(StringConstant.HELP_ITEM_AFTER);
                intent.putExtra(StringConstant.HELP_ITEM_AFTER, "5");
            }
            startActivity(intent);
        }
    };
}
