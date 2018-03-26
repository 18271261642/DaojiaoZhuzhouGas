package com.sucheng.gas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sucheng.gas.R;
import com.sucheng.gas.bean.DeliverOrderBean;
import com.sucheng.gas.interfacepack.ItemClickListener;
import com.sucheng.gas.utils.Utils;

import java.util.List;


/**
 * Created by Administrator on 2018/1/27.
 */

public class DeliverOrderAdapter extends RecyclerView.Adapter<DeliverOrderAdapter.DeliverOrderViewHolder> {


    private Context mContext;
    private List<DeliverOrderBean> list;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DeliverOrderAdapter(Context mContext, List<DeliverOrderBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public DeliverOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View views = LayoutInflater.from(mContext).inflate(R.layout.item_deliver_order, parent, false);
        return new DeliverOrderViewHolder(views);
    }

    @Override
    public void onBindViewHolder(final DeliverOrderViewHolder holder, int position) {
        holder.itemIcmynosendClientNameTv.setText(list.get(position).getClient_name()); //客户名
        holder.itemIcmynosendClientPhoneTv.setText(list.get(position).getOrder_tel_number()); //客户电话
        holder.item_bottypeTv.setText(list.get(position).getAir_bottle_specifications());
        holder.itemIcmynosendOrderNumTv.setText(list.get(position).getOrder_number()+"瓶");
        holder.item_botprieTv.setText(list.get(position).getPrice()+"元");
        holder.item_deliverPriceTv.setText(list.get(position).getDelivery_fee()+"元");
        holder.item_checkPriceTv.setText(list.get(position).getCheck_fee()+"元");
        holder.itemIcmynosendTotalAmountTv.setText(list.get(position).getTotal_amount()+"元");
        holder.itemIcmynosendCreateTimeTv.setText(Utils.longToDate(list.get(position).getOrder_time()));
        holder.itemIcmynosendAddressTv.setText(list.get(position).getOrder_address());
        holder.itemIcmynosendOrderStateTv.setText(list.get(position).getState_description());
        holder.item_paytypeTv.setText(list.get(position).getPay_type_name());
        holder.itemIcmynosendRemarkTv.setText(list.get(position).getRemark()+"");

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

    class DeliverOrderViewHolder extends RecyclerView.ViewHolder {

        TextView itemIcmynosendClientNameTv;
        TextView itemIcmynosendClientPhoneTv;
        TextView itemIcmynosendOrderNumTv;
        TextView itemIcmynosendTotalAmountTv;
        TextView itemIcmynosendCreateTimeTv;
        TextView itemIcmynosendAddressTv;
        TextView itemIcmynosendOrderStateTv;
        TextView itemIcmynosendRemarkTv;
        TextView item_botprieTv,item_deliverPriceTv,item_checkPriceTv,item_bottypeTv,item_paytypeTv;




        public DeliverOrderViewHolder(View itemView) {
            super(itemView);
            itemIcmynosendClientNameTv = (TextView) itemView.findViewById(R.id.item_icmynosend_clientNameTv);
            itemIcmynosendClientPhoneTv = (TextView) itemView.findViewById(R.id.item_icmynosend_clientPhoneTv);
            itemIcmynosendOrderNumTv = (TextView) itemView.findViewById(R.id.item_icmynosend_orderNumTv);
            itemIcmynosendTotalAmountTv = (TextView) itemView.findViewById(R.id.item_icmynosend_totalAmountTv);
            itemIcmynosendCreateTimeTv = (TextView) itemView.findViewById(R.id.item_icmynosend_createTimeTv);
            itemIcmynosendAddressTv = (TextView) itemView.findViewById(R.id.item_icmynosend_addressTv);
            itemIcmynosendOrderStateTv = (TextView) itemView.findViewById(R.id.item_icmynosend_orderStateTv);
            itemIcmynosendRemarkTv = (TextView) itemView.findViewById(R.id.item_icmynosend_remarkTv);
            item_botprieTv = (TextView) itemView.findViewById(R.id.item_botprieTv);
            item_deliverPriceTv = (TextView) itemView.findViewById(R.id.item_deliverPriceTv);
            item_checkPriceTv = (TextView) itemView.findViewById(R.id.item_checkPriceTv);
            item_bottypeTv = (TextView) itemView.findViewById(R.id.item_bottypeTv);
            item_paytypeTv = (TextView) itemView.findViewById(R.id.item_paytypeTv);
        }
    }
}
