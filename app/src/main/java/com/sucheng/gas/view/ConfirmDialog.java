package com.sucheng.gas.view;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sucheng.gas.R;


/**
 * Created by Administrator on 2017/1/10.
 */

public class ConfirmDialog extends Dialog implements View.OnClickListener{

    private Button btn;

    private TextView titleTv;  //title

    private TextView contentTv;

    private ClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show);
        initView();
    }

    public void initView() {
        titleTv = (TextView) findViewById(R.id.dialog_show_title);
        contentTv = (TextView) findViewById(R.id.dialog_show_content);
        btn = (Button) findViewById(R.id.dialog_show_btn);

        btn.setOnClickListener(this);
    }
    

    public ConfirmDialog(Context context) {
        super(context);

    }

    public ConfirmDialog(Context context, boolean isShow) {
		super(context);
		if(isShow){
			setBtnShow();
		}else{
            setBtnGone();
		}
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_show_btn:
                if(listener!= null){
                    listener.doDismiss();
                }
                cancel();
                break;
        }
    }

    public void setBtnShowOrGone(boolean isShow){
        if(isShow){
            btn.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.GONE);
        }
    }
    
    //显示按钮
    public void setBtnShow(){
    	btn.setVisibility(View.VISIBLE);
    }
    

    public void setBtnGone(){
    	btn.setVisibility(View.GONE);
    }
    

    public void setBtnText(String btnText){
    	btn.setText(btnText);
    }

    /**
     * 显示标题
     * @param str
     */
    public void setTitle(String str) {
        titleTv.setText(str);
    }

    /**
     *
     * 设置内容
     * @param msg
     */
    public void setContent(String msg){
        contentTv.setText(msg);
    }

    public interface ClickListener{
        public void doDismiss();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
