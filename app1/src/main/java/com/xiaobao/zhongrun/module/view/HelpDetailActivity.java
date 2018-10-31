package com.xiaobao.zhongrun.module.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.model.bean.HelpDetailBean;
import com.xiaobao.zhongrun.module.model.bean.VideoBean;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.HttpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;
/*
*  帮助中心 ，视屏教学 。
*/
public class HelpDetailActivity extends BaseActivity {

    @BindView(R.id.activity_help_detail_web)
    WebView mWebView;
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard mVideoplayer;

    private String title, type;

    @Override
    protected int getContentView() {
        return R.layout.activity_help_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        title = getIntent().getAction();
        type = getIntent().getStringExtra(title);
        setTitle(title);
        showRight(false);
        setLeftClickFinish();
        update();
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
                setRefreshing(false);
            }
        };
    }

    private void update(){
        if (title.equals(StringConstant.HELP_ITEM_VIDEO)) {
            mWebView.setVisibility(View.GONE);
            mVideoplayer.setVisibility(View.VISIBLE);
            loadVideo();
        } else {
            load(type);
        }
    }

    private void loadVideo() {
        OkHttpUtils.post().url(UrlConstant.VIDEO)
                .params(HttpUtils.getInstance().defaultParams())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    VideoBean bean = new Gson().fromJson(response, VideoBean.class);
                    mVideoplayer.setUp(bean.getData().getLists().get(0).getUrl(),JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
                } catch (Exception e) {
                }
            }
        });
    }

    private void load(String type) {
        Map<String, String> params = new HashMap<>();
        params.putAll(HttpUtils.getInstance().defaultParams());
        params.put("type", type);
        OkHttpUtils.post().url(UrlConstant.ARTICLE_ARTICLEDETAIL)
                .params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                HelpDetailBean bean = new Gson().fromJson(response, HelpDetailBean.class);
                mWebView.loadDataWithBaseURL(null, bean.getData().getContent(), "text/html", "utf-8", null);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(title.equals(StringConstant.HELP_ITEM_VIDEO)){
          JZVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onBackPressed() {
        if(title.equals(StringConstant.HELP_ITEM_VIDEO)){
            if(JZVideoPlayer.backPress()){
                return;
            }
        }
        super.onBackPressed();
    }
}
