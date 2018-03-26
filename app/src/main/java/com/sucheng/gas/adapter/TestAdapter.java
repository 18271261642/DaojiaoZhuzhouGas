package com.sucheng.gas.adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sucheng.gas.R;

/**
 * 
 * @author:CaiJian
 * @data��  2016-8-25
 */
public class TestAdapter extends BaseAdapter {

	private Context context;
//	private List<Map<String, Integer>> listTypeCount;
	private List<String> listType;
	private Map<String, Integer> mapCount;
	private LayoutInflater flater;

	public TestAdapter(Context context, List<String> listType,Map<String, Integer> mapCount) {
		super();
		this.context = context;
		this.listType=listType;
		this.mapCount=mapCount;
		flater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listType.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listType.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView==null) {
			convertView = flater.inflate( 
					R.layout.testitem, parent, false);//
			holder = new Holder();
			convertView.setTag(getHolderInit(convertView, holder));
		}else {
			holder = (Holder) convertView.getTag();//
		}
		holder.tvType.setText(listType.get(position));
		holder.tvCount.setText(mapCount.get(listType.get(position))+"");
		
		
		return convertView;
	}

	
	private Object getHolderInit(View convertView, Holder holder) {
		
		holder.tvCount=(TextView) convertView.findViewById(R.id.testitem_tv_shuliang);
		holder.tvType=(TextView) convertView.findViewById(R.id.testitem_tv_leixing);
		
		return holder;
	}


	class Holder{
		
		TextView tvType,tvCount;
		
	}
	
	
}
