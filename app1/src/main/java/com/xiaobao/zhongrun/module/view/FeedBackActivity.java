package com.xiaobao.zhongrun.module.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.HttpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.activity_feed_back_edt_content)
    EditText mContentEditText;
    @BindView(R.id.activity_feed_back_edt_name)
    EditText mNameEditText;
    @BindView(R.id.activity_feed_back_edt_phone)
    EditText mPhoneEditText;
    @BindView(R.id.activity_feed_back_btn_submit)
    Button mSubmitButton;

    @Override
    protected int getContentView() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setLeftClickFinish();
        setTitle(StringConstant.FEEFBACK_TITLE);

        mContentEditText.setCursorVisible(false);
        mNameEditText.setCursorVisible(false);
        mPhoneEditText.setCursorVisible(false);
        mContentEditText.setOnClickListener(onClickListener);
        mNameEditText.setOnClickListener(onClickListener);
        mPhoneEditText.setOnClickListener(onClickListener);
        mSubmitButton.setOnClickListener(onClickListener);
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_feed_back_edt_content:
                    mContentEditText.setCursorVisible(true);
                    break;
                case R.id.activity_feed_back_edt_name:
                    mNameEditText.setCursorVisible(true);
                    break;
                case R.id.activity_feed_back_edt_phone:
                    mPhoneEditText.setCursorVisible(true);
                    break;
                case R.id.activity_feed_back_btn_submit:
                    if (TextUtils.isEmpty(mContentEditText.getText().toString().trim())) {
                        showToast("请输入您要反馈的内容");
                        return;
                    }
                    if (!isMobileNO(mPhoneEditText.getText().toString())) {
                        showToast("手机号格式不正确，请重新输入");
                        return;
                    }
                    if (isSpecial(mNameEditText.getText().toString())) {
                        showToast("名称不允许包含特殊字符，请重新输入");
                        return;
                    }
                    if (mNameEditText.getText().toString().trim().length() < 2 || mNameEditText.getText().toString().trim().length() > 6) {
                        showToast("名称长度在2-6之间，请重新输入");
                        return;
                    }
                    submitClickEvent();
                    break;

            }
        }
    };

    public boolean isSpecial(String mobileNums) {

        String telRegex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    public boolean isMobileNO(String mobileNums) {

        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    private void submitClickEvent() {  //提交意见反馈的请求
        Map<String, String> params = new HashMap<>();
        params.putAll(HttpUtils.getInstance().defaultParams());
        params.put("realname", mNameEditText.getText().toString().trim());
        params.put("mobile", mPhoneEditText.getText().toString().trim());
        params.put("content", mContentEditText.getText().toString().trim());
        OkHttpUtils.post().url(UrlConstant.FEEDBACK_SAVEDATA)
                .params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                showToast("提交成功，感谢您的反馈");
                finish();
            }
        });
    }
}
