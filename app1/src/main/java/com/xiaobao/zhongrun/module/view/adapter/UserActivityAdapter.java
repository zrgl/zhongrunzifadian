package com.xiaobao.zhongrun.module.view.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaobao.zhongrun.R;
import com.xiaobao.zhongrun.module.model.bean.UserSetItemBean;

import java.util.List;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.ViewHolder> {

    private Activity mActivity;
    private List<UserSetItemBean> mAdapterDatas;
    private onItemClickListener mOnItemClickListener;

    public UserActivityAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setAdapterDatas(List<UserSetItemBean> mAdapterDatas) {
        this.mAdapterDatas = mAdapterDatas;
        this.notifyDataSetChanged();
    }

    public void setItemOnClickListener(onItemClickListener itemOnClickListener) {
        this.mOnItemClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_adapter_user_set, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserSetItemBean bean = mAdapterDatas.get(i);
        viewHolder.tvName.setText(bean.getItemName());
        viewHolder.tvValue.setText(bean.getItemValue());
        viewHolder.llValue.setTag(i);
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

    private View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvValue;
        LinearLayout llValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_user_set_tv_name);
            tvValue = itemView.findViewById(R.id.item_user_set_tv_value);
            llValue = itemView.findViewById(R.id.item_user_set_ll_value);

            itemView.setOnClickListener(itemOnClickListener);
        }
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }
}
