package com.sucheng.gas.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.bean.AddBottleTypeBean;

import java.util.List;

/**
 * 气瓶初始化，气瓶类型适配器
 * @author Sunjianhua
 *
 */
public class AddBottleTypeAdapter extends BaseAdapter {
	
	private List<AddBottleTypeBean> list;
	@SuppressWarnings("unused")
	private Context mContext;
	
	LayoutInflater inflater;

	public AddBottleTypeAdapter(List<AddBottleTypeBean> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
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
		// TODO Auto-generated method stub		
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_airinfo_adapter, null);
			holder = new ViewHolder();
			holder.addBottleTypeTx = (TextView) convertView.findViewById(R.id.addBottleTypeTx);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		AddBottleTypeBean bottleType = list.get(position);
		holder.addBottleTypeTx.setText(bottleType.getAir_bottle_specifications());
		return convertView;
	}
	
	class ViewHolder{
		TextView addBottleTypeTx;
	}

}
