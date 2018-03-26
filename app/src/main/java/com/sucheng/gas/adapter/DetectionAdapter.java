package com.sucheng.gas.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.bean.DetectionUnit;

/**
 * 检测单位适配器
 * @author sunjianhua
 * QiAnEnergyMob
 * 2016-6-17
 *
 */
public class DetectionAdapter extends BaseAdapter {
	
	private List<DetectionUnit> list;
	@SuppressWarnings("unused")
	private Context context;
	
	LayoutInflater inflater;

	public DetectionAdapter(List<DetectionUnit> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
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
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_airinfo_adapter, null);
			holder.tv = (TextView) convertView.findViewById(R.id.addBottleTypeTx);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).getDetection_unit_name());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
	}

}
