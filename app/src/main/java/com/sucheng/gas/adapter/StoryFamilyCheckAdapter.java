package com.sucheng.gas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sucheng.gas.R;
import com.sucheng.gas.bean.FamilyCheckOrderBean;
import com.sucheng.gas.interfacepack.ItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */

public class StoryFamilyCheckAdapter extends RecyclerView.Adapter<StoryFamilyCheckAdapter.MyViewHolder>{

    private List<FamilyCheckOrderBean> list;
    private Context mContext;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StoryFamilyCheckAdapter(List<FamilyCheckOrderBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_family_check,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getClient_name());
        holder.tvPhone.setText(list.get(position).getFamily_check_tel_number());
        holder.tvAddress.setText(list.get(position).getFamily_check_address());
        holder.tvApp.setText(list.get(position).getAppointment_check_time());
        holder.tvRemark.setText(list.get(position).getRemark()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    int position = holder.getLayoutPosition();
                    itemClickListener.itemPosition(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvPhone,tvAddress,tvApp,tvRemark;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.itemFamilyNameTv);
            tvPhone = (TextView) itemView.findViewById(R.id.itemFamilyPhoneTv);
            tvAddress = (TextView) itemView.findViewById(R.id.itemFamilyAddressTv);
            tvApp = (TextView) itemView.findViewById(R.id.itemFamilyAppointmentTv);
            tvRemark = (TextView) itemView.findViewById(R.id.itemFamilyRemarkTv);
        }
    }
}
