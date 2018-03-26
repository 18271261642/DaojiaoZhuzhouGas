package com.sucheng.gas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sucheng.gas.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/1.
 */

/**
 * 门店列表适配器
 */
public class StoryListAdapter extends BaseAdapter {

    private List<Map<String,Object>> mapList;
    private Context mContext;
    private LayoutInflater layoutInflater;


    public StoryListAdapter(List<Map<String, Object>> mapList, Context mContext) {
        this.mapList = mapList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_airinfo_adapter,parent,false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.addBottleTypeTx);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(""+mapList.get(position).get("addBottleTypeTx"));
        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}
