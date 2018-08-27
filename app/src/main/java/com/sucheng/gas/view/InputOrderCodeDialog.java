package com.sucheng.gas.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sucheng.gas.R;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;


/**
 * Created by Administrator on 2017/5/8.
 */

public class InputOrderCodeDialog extends Dialog implements View.OnClickListener{

    private Context mContext;

    private EditText inputEdit;
    private Button subBtn;
    private TextView titleTv;
    String inputTitle;
    String hitData;	//

    private String getInputData;
    
    
    public void setHitData(String hitData) {
		this.hitData = hitData;
		inputEdit.setHint(hitData);
	}

	//设置显示标题
	public void setInputTitle(String inputTitle) {
		//this.inputTitle = inputTitle;
		titleTv.setText(inputTitle);
	}

	public String getGetInputData() {
		return getInputData;
	}

	public void setGetInputData(String getInputData) {
		this.getInputData = getInputData;
	}

	public InputOrderCodeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    private GetInputOrderCodeListener orderListener;


    public void setOrderListener(GetInputOrderCodeListener orderListener) {
        this.orderListener = orderListener;
    }
    
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.input_ordercode_layout);

        initViews();
        
    }
    
    /**
     * 设置输入的类型
     * @param type
     */
    public void setInputType(int type){
    	inputEdit.setInputType(type);
    }
    
    public void setOrderCodeShow(){

    }
    
    /**
     *
     * @param msg
     */
    public void setInputData(String msg){
    	inputEdit.setText(msg);
    }

  
	private void initViews() {
        inputEdit = (EditText) findViewById(R.id.inputOrderCodeEdit);
        subBtn = (Button) findViewById(R.id.submitOrderCodeBtn);
        titleTv = (TextView) findViewById(R.id.inputTitleTv);
        subBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitOrderCodeBtn:
                String inputD = inputEdit.getText().toString().trim();
                if(!Utils.isEmpty(inputD)){
                    if(orderListener != null){
                        orderListener.getInputData(inputD);
                    }                    
                }else{
                    VoiceUtils.showToast(mContext,"请输入订单编码!");
                }
                break;
        }
    }



    public interface GetInputOrderCodeListener{
        void getInputData(String inputD);
    }
}
