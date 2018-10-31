package com.xiaobao.zhongrun.module.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaobao.zhongrun.App;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.model.bean.CityBean;
import com.xiaobao.zhongrun.module.model.bean.UserDetailBean;
import com.xiaobao.zhongrun.module.model.bean.UserSetItemBean;
import com.xiaobao.zhongrun.module.presenter.UserActivityPresenter;
import com.xiaobao.zhongrun.module.view.adapter.UserActivityAdapter;
import com.xiaobao.zhongrun.other.base.BaseActivity;
import com.xiaobao.zhongrun.other.base.BaseView;
import com.xiaobao.zhongrun.other.constant.IntegerConstant;
import com.xiaobao.zhongrun.other.constant.StringConstant;
import com.xiaobao.zhongrun.other.util.SharedPreferencesUtils;
import com.xiaobao.zhongrun.other.util.StatusbarUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends BaseActivity implements BaseView.UserActivityView {

    @BindView(R.id.widget_view_recycler)
    XRecyclerView mXRecyclerView;

    private View mHeadView;
    private LinearLayout mHeadLinearLayout;
    private CircleImageView mHeadImageView;
    private UserActivityAdapter mUserActivityAdapter;

    private List<LocalMedia> mLocalMedia;
    private UserDetailBean mUserDetailBean;
    private List<UserSetItemBean> mAdapterDatas;

    private ExecutorService mExecutorService;
    private UserActivityPresenter mUserActivityPresenter;

    private CityBean mCity;

    private ArrayList<String> ageList;
    private ArrayList<String> cityList;
    private ArrayList<String> genderList;
    private ArrayList<String> countryList;
    private ArrayList<String> provinceList;
    private OptionsPickerView mOptionsPickerView;

    private final int TYPE_GENDER = 0;
    private final int TYPE_AGE = 1;
    private final int TYPE_CITY = 2;
    private final int TYPE_COUNTRY = 3;
    private final int TYPE_PROVINCE = 4;
    private int CURRENT_TYPE = 0;

    @Override
    protected int getContentView() {
        return R.layout.widget_view_recycler;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        setLeftClickFinish();
//        showRight(false);
        setTitle(StringConstant.USER_TITLE);
        setRight(StringConstant.CONSTANT_SAVE);
        setRightClickListener(rightOnClickListener);

        mUserActivityAdapter = new UserActivityAdapter(this);
        mUserActivityAdapter.setItemOnClickListener(mOnItemClickListener);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setPullRefreshEnabled(false);
        mXRecyclerView.setAdapter(mUserActivityAdapter);

        mHeadView = LayoutInflater.from(this).inflate(R.layout.activity_user_header, null, false);
        mHeadImageView = mHeadView.findViewById(R.id.activity_user_header_iv_head);
        mHeadLinearLayout = mHeadView.findViewById(R.id.activity_user_header_ll_head);
        mHeadLinearLayout.setOnClickListener(headOnClickListener);
        mXRecyclerView.addHeaderView(mHeadView);

        mUserActivityPresenter = new UserActivityPresenter();
        mUserActivityPresenter.attachView(this);

        mExecutorService = Executors.newSingleThreadExecutor();
        mExecutorService.execute(new InitCity());

        initItem();
        initPickerView();
        mUserActivityPresenter.updateUser();
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mUserActivityPresenter != null) {
                    if (!mUserActivityPresenter.checkDataChange(mAdapterDatas, mUserDetailBean)) {
                        mUserActivityPresenter.updateUser();
                    }
                }
                setRefreshing(false);
            }
        };
    }

    private void initItem() {
        mAdapterDatas = mUserActivityPresenter.initItem();
        mUserActivityAdapter.setAdapterDatas(mAdapterDatas);
    }

    private void initPickerView() {

        ageList = new ArrayList<>();
        cityList = new ArrayList<>();
        genderList = new ArrayList<>();
        countryList = new ArrayList<>();
        provinceList = new ArrayList<>();
        genderList.add("男");
        genderList.add("女");
        genderList.add("保密");
        countryList.add("中国");
        for (int i = 13; i <= 65; i++) {
            ageList.add(i + "");
        }
        mOptionsPickerView = new OptionsPickerView.Builder(this, mOnOptionsSelectListener).build();
    }

    private void resetHeadImageView() {
        if (mLocalMedia != null && mLocalMedia.size() > 0) {
            if (mLocalMedia.get(0).isCut()) {
                mHeadImageView.setImageURI(Uri.parse(mLocalMedia.get(0).getCutPath()));
            } else {
                mHeadImageView.setImageURI(Uri.parse(mLocalMedia.get(0).getPath()));
            }

        }
    }

    private void showPickerView(int type) {
        switch (type) {
            case TYPE_GENDER:
                mOptionsPickerView.setPicker(genderList);
                mOptionsPickerView.setSelectOptions(getSelectOptions(genderList, mAdapterDatas.get(4).getItemValue()));
                break;
            case TYPE_AGE:
                mOptionsPickerView.setPicker(ageList);
                mOptionsPickerView.setSelectOptions(getSelectOptions(genderList, mAdapterDatas.get(5).getItemValue()));
                break;
            case TYPE_CITY:
                if (mCity == null) {
                    showToast("地址文件解析失败");
                    return;
                }
                if (TextUtils.isEmpty(mAdapterDatas.get(7).getItemValue())) {
                    showToast("请先选择省份");
                    return;
                }
                if (provinceList.size() == 0) {
                    for (int i = 0; i < mCity.getCitylist().size(); i++) {
                        provinceList.add(mCity.getCitylist().get(i).getP());
                    }
                }
                cityList.clear();
                int index = getSelectOptions(provinceList, mAdapterDatas.get(7).getItemValue());
                for (int j = 0; j < mCity.getCitylist().get(index).getC().size(); j++) {
                    cityList.add(mCity.getCitylist().get(index).getC().get(j).getN());
                }
                mOptionsPickerView.setPicker(cityList);
                mOptionsPickerView.setSelectOptions(getSelectOptions(cityList, mAdapterDatas.get(8).getItemValue()));
                break;
            case TYPE_COUNTRY:
                mOptionsPickerView.setPicker(countryList);
                mOptionsPickerView.setSelectOptions(0);
                break;
            case TYPE_PROVINCE:
                if (mCity == null) {
                    showToast("地址文件解析失败");
                    return;
                }
                if (provinceList.size() == 0) {
                    for (int i = 0; i < mCity.getCitylist().size(); i++) {
                        provinceList.add(mCity.getCitylist().get(i).getP());
                    }
                }
                mOptionsPickerView.setPicker(provinceList);
                mOptionsPickerView.setSelectOptions(getSelectOptions(provinceList, mAdapterDatas.get(7).getItemValue()));
                break;
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

    private void uploadHeadImage() {
        if (mLocalMedia != null && mLocalMedia.size() > 0) {
            if (mLocalMedia.get(0).isCut()) {
                mUserActivityPresenter.uploadHead(new File(mLocalMedia.get(0).getCutPath()));
            } else {
                mUserActivityPresenter.uploadHead(new File(mLocalMedia.get(0).getPath()));
            }

        }
    }

    //右上角保存点击事件
    private View.OnClickListener rightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showToast("保存成功");
        }
    };

    //选择器回调时间
    private OptionsPickerView.OnOptionsSelectListener mOnOptionsSelectListener = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            Intent intent = new Intent();
            if (CURRENT_TYPE == TYPE_GENDER) {
                intent.setAction(StringConstant.USER_ITEM_GENDER);
                intent.putExtra(StringConstant.USER_ITEM_GENDER, genderList.get(options1));
            } else if (CURRENT_TYPE == TYPE_AGE) {
                intent.setAction(StringConstant.USER_ITEM_AGE);
                intent.putExtra(StringConstant.USER_ITEM_AGE, ageList.get(options1));
            } else if (CURRENT_TYPE == TYPE_COUNTRY) {
                intent.setAction(StringConstant.USER_ITEM_COUNTRIES);
                intent.putExtra(StringConstant.USER_ITEM_COUNTRIES, countryList.get(options1));
                SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_COUNTRY, countryList.get(options1));
            } else if (CURRENT_TYPE == TYPE_CITY) {
                intent.setAction(StringConstant.USER_ITEM_CITY);
                intent.putExtra(StringConstant.USER_ITEM_CITY, cityList.get(options1));
                SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_CITY, cityList.get(options1));
            } else if (CURRENT_TYPE == TYPE_PROVINCE) {
                intent.setAction(StringConstant.USER_ITEM_PROVINCE);
                intent.putExtra(StringConstant.USER_ITEM_PROVINCE, provinceList.get(options1));
                SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_PROVINCE, provinceList.get(options1));
                if (!provinceList.get(options1).equals(mAdapterDatas.get(7).getItemValue())) {
                    Intent intent2 = new Intent();
                    int index = getSelectOptions(provinceList, provinceList.get(options1));
                    String city = mCity.getCitylist().get(index).getC().get(0).getN();
                    intent2.setAction(StringConstant.USER_ITEM_CITY);
                    intent2.putExtra(StringConstant.USER_ITEM_CITY, city);
                    SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_CITY, city);
                    updateData(intent2);
                }
            }
            updateData(intent);
        }
    };

    //头像点击事件
    private View.OnClickListener headOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PictureSelector.create(UserActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .compress(true)
                    .forResult(IntegerConstant.USERACTIVITY_PICTURE_RESULT_CODE);
        }
    };

    //条目点击事件
    private UserActivityAdapter.onItemClickListener mOnItemClickListener = new UserActivityAdapter.onItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (position == 4) {
                showPickerView(TYPE_GENDER);
            } else if (position == 5) {
                showPickerView(TYPE_AGE);
            } else if (position == 6) {
                showPickerView(TYPE_COUNTRY);
            } else if (position == 7) {
                showPickerView(TYPE_PROVINCE);
            } else if (position == 8) {
                showPickerView(TYPE_CITY);
            } else {
                Intent intent = new Intent(UserActivity.this, UserRedactActivity.class);
                intent.setAction(mAdapterDatas.get(position).getItemName());
                intent.putExtra(mAdapterDatas.get(position).getItemName(), mAdapterDatas.get(position).getItemValue());
                intent.putExtra(StringConstant.CONSTANT_DEVICE_NAME, position);
                startActivityForResult(intent, position);
            }
        }
    };

    // 1.media.getPath(); 为原图path 2.media.getCutPath();为裁剪后path 3.media.getCompressPath();为压缩后path
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntegerConstant.USERACTIVITY_PICTURE_RESULT_CODE:
                    mLocalMedia = PictureSelector.obtainMultipleResult(data);
                    uploadHeadImage();
                    break;
            }
        } else if (resultCode == 101) {
            updateData(data);
        }
    }

    private void updateData(Intent intent) {
//        mAdapterDatas.get(intent.getIntExtra(StringConstant.CONSTANT_DEVICE_NAME, 0)).setItemValue(intent.getStringExtra(intent.getAction()));
//        mUserActivityAdapter.setAdapterDatas(mAdapterDatas);
        mUserActivityPresenter.dataChange(intent);
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

    @Override
    public void updateHeadResult(String url) {
        showToast("头像上传成功");
        resetHeadImageView();
    }

    @Override
    public void updateUser(UserDetailBean bean) {
        try {
            //用户名
            mUserDetailBean = bean;
            if (TextUtils.isEmpty(bean.getData().getNickname())) {
                mUserDetailBean.getData().setNickname(App.getContext().Mac());
            }
            if (bean.getData().getSex().equals("1")) {
                bean.getData().setSex("女");
            } else if (bean.getData().getSex().equals("2")) {
                bean.getData().setSex("男");
            } else if (bean.getData().getSex().equals("3")) {
                bean.getData().setSex("保密");
            }
            if (bean.getData().getFace_path() != null && !TextUtils.isEmpty(bean.getData().getFace_path())) {
                Glide.with(this).load(bean.getData().getFace_path()).into(mHeadImageView);
            }
            SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_COUNTRY, bean.getData().getCountry());
            SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_PROVINCE, bean.getData().getProvince());
            SharedPreferencesUtils.getInstance().put(StringConstant.CONSTANT_CITY, bean.getData().getCity());
            mAdapterDatas.get(0).setItemValue(bean.getData().getNickname());
            mAdapterDatas.get(1).setItemValue(bean.getData().getMobile());
            mAdapterDatas.get(2).setItemValue(bean.getData().getEmail());
            mAdapterDatas.get(3).setItemValue(bean.getData().getRealname());
            mAdapterDatas.get(4).setItemValue(bean.getData().getSex());
            mAdapterDatas.get(5).setItemValue(bean.getData().getAge());
            mAdapterDatas.get(6).setItemValue(bean.getData().getCountry());
            mAdapterDatas.get(7).setItemValue(bean.getData().getProvince());
            mAdapterDatas.get(8).setItemValue(bean.getData().getCity());
            mUserActivityAdapter.setAdapterDatas(mAdapterDatas);
        } catch (Exception e) {
        }
    }

    private String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    class InitCity implements Runnable {
        @Override
        public void run() {
            mCity = new Gson().fromJson(getJson("city.json"), CityBean.class);
            mCity.getCitylist();

        }
    }
}
