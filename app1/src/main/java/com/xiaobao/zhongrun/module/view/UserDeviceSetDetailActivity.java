package com.xiaobao.zhongrun.module.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.constant.UrlConstant;
import com.xiaobao.zhongrun.other.util.BluetoothCommandUtils;
import com.xiaobao.zhongrun.other.util.HttpUtils;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class UserDeviceSetDetailActivity extends BaseActivity implements BaseView {

    @BindView(R.id.item_user_device_set_detail_iv_image)
    ImageView mImageView;
    @BindView(R.id.item_user_device_set_detail_tv_name)
    TextView mNameTextView;
    @BindView(R.id.item_user_device_set_detail_tv_type)
    TextView mTypeTextView;
    @BindView(R.id.item_user_device_set_detail_tv_intro)
    TextView mIntroTextView;
    @BindView(R.id.item_user_device_set_detail_tv_gears)
    TextView mGearsTextView;
    @BindView(R.id.item_user_device_set_detail_ll_gears)
    LinearLayout mGearsLinearLayout;
    @BindView(R.id.item_user_device_set_detail_tv_speed)
    TextView mSpeedTextView;
    @BindView(R.id.item_user_device_set_detail_ll_speed)
    LinearLayout mSpeedLinearLayout;
    @BindView(R.id.item_user_device_set_detail_tv_lj)
    TextView mLjTextView;
    @BindView(R.id.item_user_device_set_detail_ll_lj)
    LinearLayout mLjLinearLayout;
    @BindView(R.id.item_user_device_set_detail_tv_offtime)
    TextView mOfftimeTextView;
    @BindView(R.id.item_user_device_set_detail_ll_offtime)
    LinearLayout mOfftimeLinearLayout;
    @BindView(R.id.item_user_device_set_detail_tv_backlight)
    TextView mBacklightTextView;
    @BindView(R.id.item_user_device_set_detail_ll_backlight)
    LinearLayout mBacklightLinearLayout;
    @BindView(R.id.item_user_device_set_detail_tv_unit)
    TextView mUnitTextView;
    @BindView(R.id.item_user_device_set_detail_ll_unit)
    LinearLayout mUnitLinearLayout;
    @BindView(R.id.item_user_device_set_detail_tv_charge)
    TextView mChargeTextView;
    @BindView(R.id.item_user_device_set_detail_ll_charge)
    LinearLayout mChargeLinearLayout;

    private OptionsPickerView mOptionsPickerView;
    private ArrayList<String> xsList;
    private ArrayList<String> ljList;
    private ArrayList<String> ndwList;
    private ArrayList<String> offtimeList;
    private ArrayList<String> backlightList;
    private ArrayList<String> unitList;
    private ArrayList<String> chargeList;

    private String TYPE_CONSANT_CARD;
    private String TYPE_CONSANT_XS_KM;
    private String TYPE_CONSANT_LJ;
    private String TYPE_CONSANT_NDW;
    private String TYPE_CONSANT_OFFTIME;
    private String TYPE_CONSANT_BACKLIGHT_LEVEL;
    private String TYPE_CONSANT_UNIT_SETTING_ACTIVE;
    private String TYPE_CONSANT_CHARGE;
    private String CURRENT_TYPE = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_device_set_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setLeftClickFinish();
        setTitle(StringConstant.USER_DEVICE_SET_DETAIL_TITLE);
        setRight(StringConstant.CONSTANT_SAVE);
        setRightClickListener(rightOnClickListener);

        mGearsLinearLayout.setOnClickListener(mOnClickListener);
        mSpeedLinearLayout.setOnClickListener(mOnClickListener);
        mLjLinearLayout.setOnClickListener(mOnClickListener);
        mOfftimeLinearLayout.setOnClickListener(mOnClickListener);
        mBacklightLinearLayout.setOnClickListener(mOnClickListener);
        mUnitLinearLayout.setOnClickListener(mOnClickListener);
        mChargeLinearLayout.setOnClickListener(mOnClickListener);

        initDevice(getIntent());
        initPickerView();
    }

    private void initDevice(Intent intent) {
        Glide.with(this).load(intent.getStringExtra(StringConstant.DEVICE_DETAIL_IMAGE)).into(mImageView);
        mNameTextView.setText(intent.getStringExtra(StringConstant.DEVICE_DETAIL_NAME));
        mTypeTextView.setText("类型:"+intent.getStringExtra(StringConstant.DEVICE_DETAIL_TYPE));
        mIntroTextView.setText("地址:"+intent.getStringExtra(StringConstant.DEVICE_DETAIL_INTRO));

        TYPE_CONSANT_CARD = intent.getStringExtra(StringConstant.TYPE_CONSANT_CARD);
        TYPE_CONSANT_XS_KM = intent.getStringExtra(StringConstant.TYPE_CONSANT_XS_KM);
        TYPE_CONSANT_LJ = intent.getStringExtra(StringConstant.TYPE_CONSANT_LJ);
        TYPE_CONSANT_NDW = intent.getStringExtra(StringConstant.TYPE_CONSANT_NDW);
        TYPE_CONSANT_OFFTIME = intent.getStringExtra(StringConstant.TYPE_CONSANT_OFFTIME);
        TYPE_CONSANT_BACKLIGHT_LEVEL = intent.getStringExtra(StringConstant.TYPE_CONSANT_BACKLIGHT_LEVEL);
        TYPE_CONSANT_UNIT_SETTING_ACTIVE = intent.getStringExtra(StringConstant.TYPE_CONSANT_UNIT_SETTING_ACTIVE);
        TYPE_CONSANT_CHARGE = intent.getStringExtra(StringConstant.TYPE_CONSANT_CHARGE);

        TYPE_CONSANT_UNIT_SETTING_ACTIVE = TYPE_CONSANT_UNIT_SETTING_ACTIVE.equals("0") ? "公里" : "英里";

        mGearsTextView.setText(TYPE_CONSANT_NDW);
        mSpeedTextView.setText(TYPE_CONSANT_XS_KM);
        mLjTextView.setText(TYPE_CONSANT_LJ);
        mOfftimeTextView.setText(TYPE_CONSANT_OFFTIME);
        mBacklightTextView.setText(TYPE_CONSANT_BACKLIGHT_LEVEL);
        mUnitTextView.setText(TYPE_CONSANT_UNIT_SETTING_ACTIVE);
        mChargeTextView.setText(TYPE_CONSANT_CHARGE);
    }

    private void initPickerView() {
        xsList = new ArrayList<>();
        ljList = new ArrayList<>();
        ndwList = new ArrayList<>();
        offtimeList = new ArrayList<>();
        backlightList = new ArrayList<>();
        unitList = new ArrayList<>();
        chargeList = new ArrayList<>();

        for (int i = 10; i <= 70; i += 10) {
            xsList.add(i + "");
        }
        for (int i = 6; i <= 34; i += 1) {
            ljList.add(i + "");
        }
        for (int i = 2; i <= 9; i += 1) {
            ndwList.add(i + "");
        }
        for (int i = 10; i <= 60; i += 10) {
            offtimeList.add(i + "");
        }
        for (int i = 1; i <= 9; i += 1) {
            backlightList.add(i + "");
        }
        for (int i = 0; i <= 100; i += 10) {
            chargeList.add(i + "");
        }
        unitList.add("公里");
        unitList.add("英里");
        mOptionsPickerView = new OptionsPickerView.Builder(this, mOnOptionsSelectListener).build();
    }

    private void showPickerView(String type) {
        if (type.equals(StringConstant.TYPE_CONSANT_XS_KM)) {
            mOptionsPickerView.setPicker(xsList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(xsList, TYPE_CONSANT_XS_KM));
        } else if (type.equals(StringConstant.TYPE_CONSANT_LJ)) {
            mOptionsPickerView.setPicker(ljList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(ljList, TYPE_CONSANT_LJ));
        } else if (type.equals(StringConstant.TYPE_CONSANT_NDW)) {
            mOptionsPickerView.setPicker(ndwList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(ndwList, TYPE_CONSANT_NDW));
        } else if (type.equals(StringConstant.TYPE_CONSANT_OFFTIME)) {
            mOptionsPickerView.setPicker(offtimeList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(offtimeList, TYPE_CONSANT_OFFTIME));
        } else if (type.equals(StringConstant.TYPE_CONSANT_BACKLIGHT_LEVEL)) {
            mOptionsPickerView.setPicker(backlightList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(backlightList, TYPE_CONSANT_BACKLIGHT_LEVEL));
        } else if (type.equals(StringConstant.TYPE_CONSANT_UNIT_SETTING_ACTIVE)) {
            mOptionsPickerView.setPicker(unitList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(unitList, TYPE_CONSANT_UNIT_SETTING_ACTIVE));
        } else if (type.equals(StringConstant.TYPE_CONSANT_CHARGE)) {
            mOptionsPickerView.setPicker(chargeList);
            mOptionsPickerView.setSelectOptions(getSelectOptions(chargeList, TYPE_CONSANT_CHARGE));
        }
        CURRENT_TYPE = type;
        mOptionsPickerView.show();
    }

    private int getSelectOptions(ArrayList<String> list, String value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                return i;
            }
        }
        return 0;
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

    private OptionsPickerView.OnOptionsSelectListener mOnOptionsSelectListener = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            if (CURRENT_TYPE == null) {
                return;
            }
            if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_XS_KM)) {
                TYPE_CONSANT_XS_KM = xsList.get(options1);
                mSpeedTextView.setText(TYPE_CONSANT_XS_KM);
            } else if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_LJ)) {
                TYPE_CONSANT_LJ = ljList.get(options1);
                mLjTextView.setText(TYPE_CONSANT_LJ);
            } else if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_NDW)) {
                TYPE_CONSANT_NDW = ndwList.get(options1);
                mGearsTextView.setText(TYPE_CONSANT_NDW);
            } else if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_OFFTIME)) {
                TYPE_CONSANT_OFFTIME = offtimeList.get(options1);
                mOfftimeTextView.setText(TYPE_CONSANT_OFFTIME);
            } else if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_BACKLIGHT_LEVEL)) {
                TYPE_CONSANT_BACKLIGHT_LEVEL = backlightList.get(options1);
                mBacklightTextView.setText(TYPE_CONSANT_BACKLIGHT_LEVEL);
            } else if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_UNIT_SETTING_ACTIVE)) {
                TYPE_CONSANT_UNIT_SETTING_ACTIVE = unitList.get(options1);
                mUnitTextView.setText(TYPE_CONSANT_UNIT_SETTING_ACTIVE);
            } else if (CURRENT_TYPE.equals(StringConstant.TYPE_CONSANT_CHARGE)) {
                TYPE_CONSANT_CHARGE = chargeList.get(options1);
                mChargeTextView.setText(TYPE_CONSANT_CHARGE);
            }
        }
    };

    private View.OnClickListener rightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            postUpdateParams();
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_user_device_set_detail_ll_gears:
                    showPickerView(StringConstant.TYPE_CONSANT_NDW);
                    break;
                case R.id.item_user_device_set_detail_ll_speed:
                    showPickerView(StringConstant.TYPE_CONSANT_XS_KM);
                    break;
                case R.id.item_user_device_set_detail_ll_lj:
                    showPickerView(StringConstant.TYPE_CONSANT_LJ);
                    break;
                case R.id.item_user_device_set_detail_ll_offtime:
                    showPickerView(StringConstant.TYPE_CONSANT_OFFTIME);
                    break;
                case R.id.item_user_device_set_detail_ll_backlight:
                    showPickerView(StringConstant.TYPE_CONSANT_BACKLIGHT_LEVEL);
                    break;
                case R.id.item_user_device_set_detail_ll_unit:
                    showPickerView(StringConstant.TYPE_CONSANT_UNIT_SETTING_ACTIVE);
                    break;
                case R.id.item_user_device_set_detail_ll_charge:
                    showPickerView(StringConstant.TYPE_CONSANT_CHARGE);
                    break;
            }
        }
    };

    private void postUpdateParams() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", SharedPreferencesUtils.getInstance().get(StringConstant.CONSTANT_UID, "").toString());
        params.put("card", TYPE_CONSANT_CARD);
        params.put("speed", mSpeedTextView.getText().toString());
        params.put("diameter", mLjTextView.getText().toString());
        params.put("gears", mGearsTextView.getText().toString());
        params.put("down_time", mOfftimeTextView.getText().toString());
        params.put("backlight", mBacklightTextView.getText().toString());
        params.put("unit", mUnitTextView.getText().toString().equals("公里") ? "0" : "1");
        params.put("voltage", mChargeTextView.getText().toString());
        params.putAll(HttpUtils.getInstance().defaultParams());

        OkHttpUtils.post().url(UrlConstant.SETTING).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Response resp = new Gson().fromJson(response, Response.class);
                    if (resp.getCode().equals("000000")) {
                        showToast("设置成功");
                        BluetoothCommandUtils.getInstance().initCommand();
                        finish();
                        return;
                    }
                    showToast(resp.getMsg());
                } catch (Exception e) {
                    showToast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void showToast(String message) {
        super.showToast(message);
    }

    @Override
    public void showProgress(String progress) {
        super.showProgress(progress);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    class Response {

        private String Code;
        private String Msg;
        private String Time;
        private String ApiUrl;
        private String Data;

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getApiUrl() {
            return ApiUrl;
        }

        public void setApiUrl(String ApiUrl) {
            this.ApiUrl = ApiUrl;
        }

        public String getData() {
            return Data;
        }

        public void setData(String Data) {
            this.Data = Data;
        }
    }
}
