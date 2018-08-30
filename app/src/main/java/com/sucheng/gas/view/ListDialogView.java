package com.sucheng.gas.view;

import java.util.List;

import com.sucheng.gas.R;
import com.sucheng.gas.adapter.BottleInfoAdapter;
import com.sucheng.gas.bean.AirBotInfoBean;
import com.sucheng.gas.bean.BotInfoBean;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListDialogView extends Dialog implements OnItemClickListener{
	
	private List<AirBotInfoBean.DataBean> listData;
	private BottleInfoAdapter botAdater;
	private ListView modelListView;
	private Context mContext;
	private TextView titleTv;
	
	public ListDialogViewItemClickListener dialogListener;
	
	
	public void setDialogListener(ListDialogViewItemClickListener dialogListener) {
		this.dialogListener = dialogListener;
	}

	public ListDialogView(Context context) {
		super(context);
		this.mContext = context;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.model_dialog_layout);
		
		titleTv = (TextView) findViewById(R.id.dialogTitleTv);
		modelListView = (ListView) findViewById(R.id.modelListView);
		modelListView.setOnItemClickListener(this);
		
	}
	
	public void setTitleTv(String title){
		titleTv.setText(title);
	}

	public List<AirBotInfoBean.DataBean> getListData() {
		return listData;
	}

	public void setListData(List<AirBotInfoBean.DataBean> listData) {
		this.listData = listData;
		
		botAdater = new BottleInfoAdapter(listData, mContext);
		modelListView.setAdapter(botAdater);
		
	}
	
	public interface ListDialogViewItemClickListener {
		void position(int position);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(dialogListener != null){
			dialogListener.position(position);
		}
		
	}

}
