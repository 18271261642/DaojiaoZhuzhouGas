package com.sucheng.gas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.bean.AirBotInfoBean;
import com.sucheng.gas.bean.BotInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class BottleInfoAdapter extends BaseAdapter {

    private List<AirBotInfoBean.DataBean> list;
    private Context mContext;
    private LayoutInflater layoutInflater;


    public BottleInfoAdapter(List<AirBotInfoBean.DataBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_model_layout, parent,false);
            holder = new ViewHolder();
            holder.tv  = (TextView) convertView.findViewById(R.id.item_model_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position).getAir_bottle_seal_code()+"");

        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }

}

