package com.sucheng.gas.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.bean.ProductUni;

/**
 * 气瓶初始化，生产单位适配器
 * @author sunjianhua
 * QiAnEnergyMob
 * 2016-6-17
 *
 */
public class ProductAdapter extends BaseAdapter {

	private List<ProductUni> list;
	@SuppressWarnings("unused")
	private Context context;
	
	LayoutInflater inflater;

	public ProductAdapter(List<ProductUni> list, Context context) {
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
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_airinfo_adapter, null);
			holder.tv = (TextView) convertView.findViewById(R.id.addBottleTypeTx);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).getProduction_unit_name());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
	}

}
