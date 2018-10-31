package com.xiaobao.zhongrun.module.view.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.model.bean.UserListBean;

import java.util.List;

public class UserDeviceAdapter extends RecyclerView.Adapter<UserDeviceAdapter.ViewHolder> {

    private Activity mActivity;
    private boolean mShowNext = false, mError;
    private List<UserListBean.DataBean.ListsBean> mAdapterDatas;
    private onItemClickClickListener mOnItemClickClickListener;

    public UserDeviceAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setShowNext(boolean showNext) {
        this.mShowNext = showNext;
    }

    public void setError(boolean mError) {
        this.mError = mError;
    }

    public void setAdapterDatas(List<UserListBean.DataBean.ListsBean> mAdapterDatas) {
        this.mAdapterDatas = mAdapterDatas;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickClickListener(onItemClickClickListener mOnItemClickClickListener) {
        this.mOnItemClickClickListener = mOnItemClickClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_adapter_user_device, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mActivity).load(mAdapterDatas.get(i).getPic_id()).into(viewHolder.mImageView);
        viewHolder.mNameTextView.setText(mAdapterDatas.get(i).getDevice_name());
        viewHolder.mTypeTextView.setText("类型:" + mAdapterDatas.get(i).getVersion());
        viewHolder.mAddressTextView.setText("地址:" + mAdapterDatas.get(i).getCard());
        if (mError) {
            viewHolder.mIntroTextView.setText("简介:故障车");
        } else {
            viewHolder.mIntroTextView.setText("简介:" + mAdapterDatas.get(i).getContent());
        }
        viewHolder.mNextImageView.setVisibility(mShowNext ? View.VISIBLE : View.GONE);
        viewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        try {
            return mAdapterDatas.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<UserListBean.DataBean.ListsBean> getmAdapterDatas() {
        return mAdapterDatas;
    }

    private View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnItemClickClickListener != null) {
                mOnItemClickClickListener.itemClick(view, (Integer) view.getTag());
            }
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        ImageView mNextImageView;
        TextView mNameTextView;
        TextView mTypeTextView;
        TextView mAddressTextView;
        TextView mIntroTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_user_device_iv_image);
            mNextImageView = itemView.findViewById(R.id.item_user_device_iv_next);
            mNameTextView = itemView.findViewById(R.id.item_user_device_tv_name);
            mTypeTextView = itemView.findViewById(R.id.item_user_device_tv_type);
            mAddressTextView = itemView.findViewById(R.id.item_user_device_tv_address);
            mIntroTextView = itemView.findViewById(R.id.item_user_device_tv_intro);
            itemView.setOnClickListener(itemOnClickListener);
        }
    }

    public interface onItemClickClickListener {
        void itemClick(View view, int position);
    }

}
