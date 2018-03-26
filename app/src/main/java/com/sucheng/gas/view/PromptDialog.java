package com.sucheng.gas.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;


/**
 * 提示框
 * @Describe
 * @author sunjianhua
 * XWGasApp
 * 2017-4-6
 *
 */
public class PromptDialog extends Dialog implements View.OnClickListener {

    public PromptDialog(Context context) {
        super(context);
    }
    
    private OnPromptDialogListener listener;
   
    private TextView content;
    
    private TextView contentMsg;
  
    private Button btnok;
   
    private Button btnno;
    
    private int code;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alert);
        initView();
    }
    
    private void initView() {
        content = (TextView) findViewById(R.id.dialog_prompt_content);
        contentMsg = (TextView) findViewById(R.id.dialog_prompt_content1);
        btnok = (Button) findViewById(R.id.dialog_ok);
        btnno = (Button) findViewById(R.id.dialog_no);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        btnok.setOnClickListener(this);
        btnno.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok:
                if (listener != null) {
                    listener.leftClick(code);
                }
                cancel();
                break;
            case R.id.dialog_no:
                if (listener != null) {
                    listener.rightClick(code);
                }
                cancel();
                break;
            default:
                break;
        }
    }

    
    public void setListener(OnPromptDialogListener listener) {
        this.listener = listener;
    }
    
    public void setTitle(String str) {
        content.setText(str);
    }

    public void setContent(String msg){
        contentMsg.setText(msg);
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public void setNoButtom(boolean bool) {
        if (bool) {
            btnno.setVisibility(View.VISIBLE);
        } else {
            btnno.setVisibility(View.GONE);
        }
    }
    
    
    public void setleftText(String text) {
        btnok.setText(text);
    }
    
   
    public void setrightText(String text) {
        btnno.setText(text);
    }
}
