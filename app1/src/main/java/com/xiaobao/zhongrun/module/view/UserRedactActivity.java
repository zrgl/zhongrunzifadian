package com.xiaobao.zhongrun.module.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.constant.StringConstant;

import butterknife.BindView;

public class UserRedactActivity extends BaseActivity {

    @BindView(R.id.activity_user_redact_edt_content)
    EditText mContentEditText;
    @BindView(R.id.activity_user_redact_tv_hint)
    TextView mHintTextView;
    private int position;
    private String title, value;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_redact;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        title = getIntent().getAction();
        value = getIntent().getStringExtra(getIntent().getAction());
        position = getIntent().getIntExtra(StringConstant.CONSTANT_DEVICE_NAME, 0);
        setSwipeRefreshLayoutEnabled(false);
        setLeftClickFinish();
        setTitle(title);
        setRight(StringConstant.CONSTANT_SAVE);
        setRightClickListener(rightOnClickListener);
        mContentEditText.setCursorVisible(false);
        mContentEditText.setOnClickListener(onClickListener);
        mContentEditText.setText(value);

        if (title.equals(StringConstant.USER_ITEM_AGE)) {
            mContentEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
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
            mContentEditText.setCursorVisible(true);
        }
    };

    private View.OnClickListener rightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(mContentEditText.getText().toString().trim())) {
                showToast("输入的值不允许为空");
                return;
            }
            if (title.equals(StringConstant.USER_ITEM_PHONE)) {
                if (!isMobileNO(mContentEditText.getText().toString())) {
                    showToast("手机格式不正确，请重新输入");
                    return;
                }
            }
            if (title.equals(StringConstant.USER_ITEM_EMAIL)) {
                if (!isEmail(mContentEditText.getText().toString())) {
                    showToast("邮箱格式不正确，请重新输入");
                    return;
                }
            }

            if (title.equals(StringConstant.USER_ITEM_NAME)||title.equals(StringConstant.USER_ITEM_TRUENAME)) {
                if (isSpecial(mContentEditText.getText().toString())) {
                    showToast("名称不允许包含特殊字符，请重新输入");
                    return;
                }
                if (mContentEditText.getText().toString().trim().length() < 2 || mContentEditText.getText().toString().trim().length() > 6) {
                    showToast("名称长度在2-6之间，请重新输入");
                    return;
                }
            }
            Intent intent = new Intent();
            intent.setAction(title);
            intent.putExtra(title, mContentEditText.getText().toString());
            intent.putExtra(StringConstant.CONSTANT_DEVICE_NAME, position);
            setResult(101, intent);
            finish();
        }
    };

    public boolean isMobileNO(String mobileNums) {

        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    public boolean isEmail(String mobileNums) {

        String telRegex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    public boolean isSpecial(String mobileNums) {

        String telRegex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
}
